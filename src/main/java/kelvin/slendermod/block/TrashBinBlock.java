package kelvin.slendermod.block;

import kelvin.slendermod.blockentity.TrashBinBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class TrashBinBlock extends BlockWithEntity {

    private static final VoxelShape BASE_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(2, 0, 2, 14, 12, 14),
            Block.createCuboidShape(1, 12, 1, 15, 16, 15)
    );
    private static final VoxelShape NORTH_SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(4.5, 16, 6.5, 11.5, 18, 9.5), BASE_SHAPE);
    private static final VoxelShape EAST_WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(6.5, 16, 4.5, 9.5, 18, 11.5), BASE_SHAPE);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public TrashBinBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        else {
            if (world.getBlockEntity(pos) instanceof TrashBinBlockEntity trashBinBlockEntity) {
                player.openHandledScreen(new NamedScreenHandlerFactory() {

                    @Override
                    public Text getDisplayName() {
                        return trashBinBlockEntity.getDisplayName();
                    }

                    @Nullable
                    @Override
                    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                        return trashBinBlockEntity.createMenu(syncId, inv, player);
                    }
                });
            }
            return ActionResult.CONSUME;
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST, WEST -> EAST_WEST_SHAPE;
            default -> NORTH_SOUTH_SHAPE;
        };
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrashBinBlockEntity(pos, state);
    }
}
