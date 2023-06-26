package kelvin.slendermod.block;

import kelvin.slendermod.blockentity.RotatableBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RotatableBlockEntityBlock<T extends RotatableBlockEntity> extends CustomFacingBlock implements BlockEntityProvider {

    private final String resourceId;

    public RotatableBlockEntityBlock(Settings settings, String resourceId) {
        super(settings);
        this.resourceId = resourceId;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RotatableBlockEntity(pos, state);
    }

    public String getResourceId() {
        return resourceId;
    }
}
