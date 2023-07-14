package kelvin.slendermod.multiblock;

import kelvin.slendermod.util.MultiblockPositioner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MultiBlockDoor extends MultiBlock {
    public static final BooleanProperty IS_OPEN = BooleanProperty.of("is_open");

    public MultiBlockDoor(Settings settings, MultiblockPositioner positioner) {
        super(settings, positioner, 0, 1);
        this.setDefaultState(this.stateManager.getDefaultState().with(IS_OPEN, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Direction facing = state.get(FACING);
        Direction clockwise = facing.rotateYCounterclockwise();

        MultiBlockEntity blockEntity = (MultiBlockEntity) world.getBlockEntity(pos);
        BlockPos modelPos = new BlockPos((WIDTH-1)-MODEL_OFFSET_X, 0, (DEPTH-1)-MODEL_OFFSET_Z);
        if (blockEntity != null) {
            BlockPos mainPos = blockEntity.getMainBlock();
            preformOnAll(world, state, mainPos, (worldPos, relativePos) -> {
                BlockPos newPos = worldPos.offset(facing.getOpposite(), relativePos.getX()).offset(clockwise, relativePos.getZ() - relativePos.getX());
                if (!state.get(IS_OPEN)) {
                    world.removeBlock(worldPos, false);

                    world.setBlockState(newPos, state.with(IS_OPEN, true).with(MAIN_BLOCK, worldPos.equals(mainPos)));
                    MultiBlockEntity dummyEntity = (MultiBlockEntity) world.getBlockEntity(newPos);

                    if (dummyEntity != null) {
                        dummyEntity.setMainBlock(blockEntity.getMainBlock());
                    }
                } else {
                    world.removeBlock(newPos, false);
                    world.setBlockState(worldPos, state.with(IS_OPEN, false).with(MAIN_BLOCK, worldPos.equals(mainPos)));

                    MultiBlockEntity dummyEntity = (MultiBlockEntity) world.getBlockEntity(worldPos);

                    if (dummyEntity != null) {
                        dummyEntity.setMainBlock(blockEntity.getMainBlock());
                    }
                }
                return true;
            });
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_OPEN);
        super.appendProperties(builder);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MultiBlockDoorEntity(pos, state);
    }
}
