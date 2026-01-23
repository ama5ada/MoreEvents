package plugin.events;

import com.hypixel.hytale.component.system.CancellableEcsEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GenericUseBlockEvent extends CancellableEcsEvent {
    @Nullable
    private final ItemStack itemInHand;
    @Nonnull
    private Vector3i targetBlock;
    @Nonnull
    private final BlockType blockType;

    public GenericUseBlockEvent(@Nullable ItemStack itemInHand, @Nonnull Vector3i targetBlock, BlockType blockType) {
        this.itemInHand = itemInHand;
        this.targetBlock = targetBlock;
        this.blockType = blockType;
    }

    @Nullable
    public ItemStack getItemInHand() {
        return itemInHand;
    }

    @Nonnull
    public Vector3i getTargetBlock() {
        return targetBlock;
    }

    @Nonnull
    public BlockType getBlockType() { return blockType; }

    public void setTargetBlock(@Nonnull Vector3i targetBlock) {
        this.targetBlock = targetBlock;
    }
}
