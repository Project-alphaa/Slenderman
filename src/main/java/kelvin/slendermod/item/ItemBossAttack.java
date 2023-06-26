package kelvin.slendermod.item;

import kelvin.slendermod.entity.EntitySlenderBoss;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBossAttack extends Item {


    public ItemBossAttack() {
        super(new Item.Settings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            if (user.hasVehicle()) {
                var entity = user.getVehicle();
                if (entity instanceof EntitySlenderBoss boss) {
                    if (boss.getCurrentState() == EntitySlenderBoss.State.DEFAULT) {
                        boss.changeState(EntitySlenderBoss.State.ATTACK);
                        user.getItemCooldownManager().set(this, 30);
                    }
                }
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }
}
