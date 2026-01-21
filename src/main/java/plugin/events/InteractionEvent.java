package plugin.events;

import com.hypixel.hytale.component.system.CancellableEcsEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.inventory.ItemStack;

public enum InteractionEvent {
    HARVEST(HarvestBlockEvent::new),
    GATHER(GatherBlockEvent::new),
    NONE(null);

    private final InteractionEventFactory factory;

    InteractionEvent(InteractionEventFactory factory) {
        this.factory = factory;
    }

    public CancellableEcsEvent createEvent(
            ItemStack item, Vector3i pos, BlockType block) {
        return factory == null ? null : factory.create(item, pos, block);
    }

    public static InteractionEvent fromHint(String hint) {
        if (hint == null || hint.isEmpty()) {
            return NONE;
        }

        String[] parts = hint.split("\\.");
        String suffix = parts[parts.length - 1];

        try {
            return InteractionEvent.valueOf(suffix.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }

    public static CancellableEcsEvent createEventFromHint(
            String hint,
            ItemStack item,
            Vector3i pos,
            BlockType block) {

        return fromHint(hint).createEvent(item, pos, block);
    }
}


@FunctionalInterface
interface InteractionEventFactory {
    CancellableEcsEvent create(
            ItemStack item,
            Vector3i pos,
            BlockType block
    );
}


