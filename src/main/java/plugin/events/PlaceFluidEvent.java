package plugin.events;

import com.hypixel.hytale.component.system.CancellableEcsEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlaceFluidEvent extends CancellableEcsEvent {
    @Nullable
    private final ItemStack itemInHand;
    @Nonnull
    private Vector3i targetBlock;

    public PlaceFluidEvent(@Nullable ItemStack itemInHand, @Nonnull Vector3i targetBlock) {
        this.itemInHand = itemInHand;
        this.targetBlock = targetBlock;
    }

    @Nullable
    public ItemStack getItemInHand() {
        return itemInHand;
    }

    @Nonnull
    public Vector3i getTargetBlock() {
        return targetBlock;
    }

    public void setTargetBlock(@Nonnull Vector3i targetBlock) {
        this.targetBlock = targetBlock;
    }
}
