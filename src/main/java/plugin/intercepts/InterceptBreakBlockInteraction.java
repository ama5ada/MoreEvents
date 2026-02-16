package plugin.intercepts;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.system.CancellableEcsEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.events.HarvestBlockEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.BreakBlockInteraction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.hypixel.hytale.builtin.hytalegenerator.LoggerUtil.getLogger;

public class InterceptBreakBlockInteraction extends BreakBlockInteraction {
    @Nonnull
    public static final BuilderCodec<InterceptBreakBlockInteraction> CODEC;

    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldownHandler) {
        BlockType targetBlockType = world.getBlockType(targetBlock);

        // Make sure that a player event is being intercepted
        Ref<EntityStore> ref = context.getEntity();
        Player playerComponent = (Player) commandBuffer.getComponent(ref, Player.getComponentType());

        if (this.harvest && playerComponent != null) {
            CancellableEcsEvent event = new HarvestBlockEvent(itemInHand, targetBlock, targetBlockType);
            context.getEntity().getStore().invoke(context.getEntity(), event);
            getLogger().info("Intercepted a BreakBlockInteraction and added a Harvest Block Event");
            if (event.isCancelled()) {
                context.getState().state = InteractionState.Failed;
                InteractionManager manager = context.getInteractionManager();
                if (manager != null && context.getChain() != null) {
                    manager.cancelChains(context.getChain());
                }
                return;
            }
        }

        super.interactWithBlock(world, commandBuffer, type, context, itemInHand,targetBlock, cooldownHandler);
    }

    static {
        CODEC = BuilderCodec.builder(
                        InterceptBreakBlockInteraction.class,
                        InterceptBreakBlockInteraction::new,
                        BreakBlockInteraction.CODEC)
                .documentation("Custom block use logic.")
                .build();
    }
}
