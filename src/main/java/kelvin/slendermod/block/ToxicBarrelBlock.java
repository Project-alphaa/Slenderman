package kelvin.slendermod.block;

import net.minecraft.block.Block;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.stream.Stream;

public class ToxicBarrelBlock extends Block {
    private static final VoxelShape SHAPE = Stream.of(Block.createCuboidShape(0, 0, 5, 16, 24, 11), Block.createCuboidShape(5, 0, 11, 11, 24, 16), Block.createCuboidShape(5, 0, 0, 11, 24, 5), Block.createCuboidShape(4.46447, 0.005, 0.22183, 11.53553, 23.995, 15.77817), Block.createCuboidShape(4.46447, 0.005, 0.22183, 11.53553, 23.995, 15.77817)).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    
    public ToxicBarrelBlock(Settings settings) {
        super(settings);
    }
}
