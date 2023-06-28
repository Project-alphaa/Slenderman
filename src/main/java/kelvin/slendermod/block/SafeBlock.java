package kelvin.slendermod.block;

import kelvin.slendermod.blockentity.SafeBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class SafeBlock extends BlockWithEntity {
    private static final VoxelShape SHAPE = Stream.of(Block.createCuboidShape(1, 0, 4, 3, 16, 14), Block.createCuboidShape(3, 14, 4, 13, 16, 14), Block.createCuboidShape(13, 0, 4, 15, 16, 14), Block.createCuboidShape(3, 0, 4, 13, 2, 14), Block.createCuboidShape(3, 2, 12, 13, 14, 14), Block.createCuboidShape(3, 2, 4, 13, 14, 6)).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

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

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
