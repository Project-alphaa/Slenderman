package kelvin.slendermod.block;

import kelvin.slendermod.blockentity.SafeBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SafeBlock extends BlockWithEntity {
    private static final VoxelShape SHAPE_N = Block.createCuboidShape(1, 0, 4, 15, 16, 14);
    private static final VoxelShape SHAPE_E = Block.createCuboidShape(4, 0, 1, 14, 16, 15);
    private static final VoxelShape SHAPE_S = Block.createCuboidShape(1, 0, 2, 15, 16, 12);
    private static final VoxelShape SHAPE_W = Block.createCuboidShape(2, 0, 1, 12, 16, 15);
    private static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public SafeBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SafeBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            //            this.triggerAnim("controller", "open"); // TODO implement opening animation
        }

        return ActionResult.CONSUME;
    }
    
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getPlayerFacing().getOpposite());
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> SHAPE_N;
            case EAST -> SHAPE_W;
            case SOUTH -> SHAPE_S;
            case WEST -> SHAPE_E;
            case UP, DOWN -> throw new RuntimeException("Unsupported block state type");
        };
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
