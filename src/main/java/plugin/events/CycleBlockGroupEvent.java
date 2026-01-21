package plugin.events;

import com.hypixel.hytale.component.system.CancellableEcsEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;

import javax.annotation.Nonnull;

public class CycleBlockGroupEvent extends CancellableEcsEvent {
    @Nonnull
    private Vector3i targetBlock;
    @Nonnull
    private final BlockType blockType;

    public CycleBlockGroupEvent(@Nonnull Vector3i targetBlock, BlockType blockType) {
        this.targetBlock = targetBlock;
        this.blockType = blockType;
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
