package kelvin.slendermod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DeadEntityBlock extends Block {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final IntProperty FLESH_REMOVED;
    public int shearsBeforeBreak;

    public DeadEntityBlock(Settings settings, int shearsBeforeBreak) {
        super(settings);
        this.shearsBeforeBreak = ensureRange(shearsBeforeBreak-1, 0, 99);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
        );
    }
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getMainHandStack().getItem() == Items.SHEARS && hand == Hand.MAIN_HAND) {
            ItemStack shears = player.getMainHandStack();
            double d = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448;
            double e = (double) (world.random.nextFloat() * 0.7F) + 0.06000000238418579 + 0.6;
            double g = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448;
            ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + g, Items.ROTTEN_FLESH.getDefaultStack());
            world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.playSound(player, pos, SoundEvents.BLOCK_SLIME_BLOCK_STEP, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.spawnEntity(itemEntity);
            shears.damage(1, player, (p) -> {
                player.sendToolBreakStatus(hand);
            });
            world.setBlockState(pos, state.cycle(FLESH_REMOVED));
            if (state.get(FLESH_REMOVED) == this.shearsBeforeBreak) {
                world.removeBlock(pos, false);
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    int ensureRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }


    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, FLESH_REMOVED});
    }

    static {
        FLESH_REMOVED = IntProperty.of("flesh_removed", 0, 99);
    }
}
