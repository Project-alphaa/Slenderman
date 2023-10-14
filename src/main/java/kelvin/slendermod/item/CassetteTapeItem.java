package kelvin.slendermod.item;

import kelvin.slendermod.block.WalkmanBlock;
import kelvin.slendermod.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CassetteTapeItem extends MusicDiscItem {
    public CassetteTapeItem(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds) {
        super(comparatorOutput, sound, settings, lengthInSeconds);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(BlockRegistry.WALKMAN) && !(Boolean)blockState.get(WalkmanBlock.HAS_CASSETTE)) {
            ItemStack itemStack = context.getStack();
            if (!world.isClient) {
                ((WalkmanBlock) BlockRegistry.WALKMAN).setCassette(context.getPlayer(), world, blockPos, blockState, itemStack);
                itemStack.decrement(1);
            }

            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    public int getComparatorOutput() {
        return super.getComparatorOutput();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    }

    @Override
    public MutableText getDescription() {
        return super.getDescription();
    }

    @Override
    public SoundEvent getSound() {
        return super.getSound();
    }

    @Override
    public int getSongLengthInTicks() {
        return super.getSongLengthInTicks();
    }
}

