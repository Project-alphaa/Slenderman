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
        super(settings, positioner, 0);
        this.setDefaultState(this.stateManager.getDefaultState().with(IS_OPEN, false));
    }


    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.get(IS_OPEN)) {
            Direction currentFacing = state.get(FACING);
            Direction originalFacing = currentFacing.rotateYClockwise();

            MultiBlockEntity blockEntity = (MultiBlockEntity) world.getBlockEntity(pos);
            if (blockEntity != null) {
                BlockPos mainPos = blockEntity.getMainBlock();
                preformOnAll(world, state.with(FACING, originalFacing), mainPos, (worldPos, relativePos) -> {
                    BlockPos newPos = worldPos.offset(originalFacing.getOpposite(), relativePos.getX()).offset(originalFacing.rotateYCounterclockwise(), relativePos.getZ() - relativePos.getX());
                    world.removeBlock(newPos, false);
                    return true;
                });
            }
        } else {
            super.onBreak(world, pos, state, player);
        }
    }


    public boolean canOpenDoor(World world, BlockState state, BlockPos pos) {
        Direction currentFacing = state.get(FACING);
        Direction originalFacing = !state.get(IS_OPEN) ? currentFacing : currentFacing.rotateYClockwise();

        return this.preformOnAll(world, state.with(FACING, originalFacing), pos, (worldPos, relativePos) -> {
            BlockPos newPos = worldPos.offset(originalFacing.getOpposite(), relativePos.getX()).offset(originalFacing.rotateYCounterclockwise(), relativePos.getZ() - relativePos.getX());
            if (relativePos.getX() != 0) {
                return world.getBlockState(state.get(IS_OPEN) ? worldPos : newPos).isAir();
            }
            return true;
        });
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand == Hand.MAIN_HAND) {
            Direction currentFacing = state.get(FACING);
            Direction originalFacing = !state.get(IS_OPEN) ? currentFacing : currentFacing.rotateYClockwise();

            System.out.println(originalFacing);
            Direction clockwise = originalFacing.rotateYCounterclockwise();

            MultiBlockEntity blockEntity = (MultiBlockEntity) world.getBlockEntity(pos);
            if (blockEntity != null) {
                BlockPos mainPos = blockEntity.getMainBlock();

                if (this.canOpenDoor(world, state, mainPos)) {
                    preformOnAll(world, state.with(FACING, originalFacing), mainPos, (worldPos, relativePos) -> {
                        BlockPos newPos = worldPos.offset(originalFacing.getOpposite(), relativePos.getX()).offset(originalFacing.rotateYCounterclockwise(), relativePos.getZ() - relativePos.getX());

                        BlockPos currPos = !state.get(IS_OPEN) ? newPos : worldPos;
                        BlockPos oppositePos = state.get(IS_OPEN) ? newPos : worldPos;
                        Direction currFacing = !state.get(IS_OPEN) ? clockwise : originalFacing;


                        boolean isModelBlock = world.getBlockState(oppositePos).get(MODEL_BLOCK);
                        world.removeBlock(oppositePos, false);

                        world.setBlockState(currPos, state.with(IS_OPEN, !state.get(IS_OPEN)).with(FACING, currFacing).with(MAIN_BLOCK, worldPos.equals(mainPos)).with(MODEL_BLOCK, isModelBlock));
                        MultiBlockEntity dummyEntity = (MultiBlockEntity) world.getBlockEntity(currPos);

                        if (dummyEntity != null) {
                            dummyEntity.setMainBlock(blockEntity.getMainBlock());
                        }
                        return true;
                    });
                }
            }
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
