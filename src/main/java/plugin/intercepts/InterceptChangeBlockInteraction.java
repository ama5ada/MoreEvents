package plugin.intercepts;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.ChangeBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.events.ChangeBlockEvent;

import static com.hypixel.hytale.builtin.hytalegenerator.LoggerUtil.getLogger;

public class InterceptChangeBlockInteraction extends ChangeBlockInteraction {
    @Override
    protected void interactWithBlock(
            World world,
            CommandBuffer<EntityStore> buffer,
            InteractionType type,
            InteractionContext context,
            ItemStack stack,
            Vector3i blockPos,
            CooldownHandler cooldown
    ) {
        BlockType targetBlockType = world.getBlockType(blockPos);
        ChangeBlockEvent event = new ChangeBlockEvent(blockPos, targetBlockType);
        context.getEntity().getStore().invoke(context.getEntity(), event);
        getLogger().info("Intercepted ChangeBlockInteraction and added ChangeBlockEvent");

        if (event.isCancelled()) {
            context.getState().state = InteractionState.Failed;
            InteractionManager manager = context.getInteractionManager();
            if (manager != null && context.getChain() != null) {
                manager.cancelChains(context.getChain());
            }
            return;
        }

        super.interactWithBlock(world, buffer, type, context, stack, blockPos, cooldown);
    }

    public static final BuilderCodec<InterceptChangeBlockInteraction> CODEC;

    static {
        CODEC = BuilderCodec.builder(
                        InterceptChangeBlockInteraction.class,
                        InterceptChangeBlockInteraction::new,
                        ChangeBlockInteraction.CODEC)
                .build();
    }
}
