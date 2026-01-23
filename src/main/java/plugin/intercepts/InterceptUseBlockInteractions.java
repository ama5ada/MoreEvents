package plugin.intercepts;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.CancellableEcsEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.UseBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.events.GenericHitBlockEvent;
import plugin.events.GenericUseBlockEvent;
import plugin.events.InteractionEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.hypixel.hytale.builtin.hytalegenerator.LoggerUtil.getLogger;

public class InterceptUseBlockInteractions extends UseBlockInteraction {
    @Nonnull
    public static final BuilderCodec<InterceptUseBlockInteractions> CODEC;

    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldownHandler) {
        BlockType targetBlockType = world.getBlockType(targetBlock);

        if (targetBlockType != null) {
            CancellableEcsEvent event = null;
            if (type.equals(InteractionType.Use)) {
                event = InteractionEvent.createEventFromHint(targetBlockType.getInteractionHint(), itemInHand, targetBlock, targetBlockType);
                if (event == null) {
                    event = new GenericUseBlockEvent(itemInHand, targetBlock, targetBlockType);
                }
            } else if (type.equals(InteractionType.Primary)) {
                event = new GenericHitBlockEvent(itemInHand, targetBlock, targetBlockType);
            }
            if (event != null) {
                Ref<EntityStore> ref = context.getEntity();
                Store<EntityStore> store = ref.getStore();
                store.invoke(ref, event);
                getLogger().info(String.format("Intercept a UseBlockInteraction and added a %s", event.getClass().getSimpleName()));
                if (event.isCancelled()) {
                    context.getState().state = InteractionState.Finished;
                    InteractionManager manager = context.getInteractionManager();
                    if (manager != null && context.getChain() != null) {
                        manager.cancelChains(context.getChain());
                    }
                    return;
                }
            }
        }

        super.interactWithBlock(world, commandBuffer, type, context, itemInHand,targetBlock, cooldownHandler);
    }

    @Nonnull
    protected Interaction generatePacket() {
        return new com.hypixel.hytale.protocol.UseBlockInteraction();
    }

    @Nonnull
    public String toString() {
        return "UseBlockInteraction{} " + super.toString();
    }

    static {
        CODEC = BuilderCodec.builder(
                        InterceptUseBlockInteractions.class,
                        InterceptUseBlockInteractions::new,
                        UseBlockInteraction.CODEC)
                .documentation("Custom block use logic.")
                .build();
    }
}
