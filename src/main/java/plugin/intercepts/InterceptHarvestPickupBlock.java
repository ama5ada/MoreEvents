package plugin.intercepts;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.system.CancellableEcsEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionChain;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.UseBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import plugin.events.InteractionEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.hypixel.hytale.builtin.hytalegenerator.LoggerUtil.getLogger;

public class InterceptHarvestPickupBlock extends UseBlockInteraction {
    @Nonnull
    public static final BuilderCodec<InterceptHarvestPickupBlock> CODEC;

    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldownHandler) {
        BlockType targetBlockType = world.getBlockType(targetBlock);

        if (targetBlockType != null) {
            CancellableEcsEvent event = InteractionEvent.createEventFromHint(targetBlockType.getInteractionHint(), itemInHand, targetBlock, targetBlockType);
            if (event != null) {
                context.getEntity().getStore().invoke(context.getEntity(), event);
                getLogger().info("Intercepted UseBlockInteraction and created a Harvest/GatherBlockEvent");
                if (event.isCancelled()) {
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
                        InterceptHarvestPickupBlock.class,
                        InterceptHarvestPickupBlock::new,
                        UseBlockInteraction.CODEC)
                .documentation("Custom block use logic.")
                .build();
    }
}
