package kelvin.slendermod.client.screen.handler;

import kelvin.slendermod.blockentity.SafeBlockEntity;
import kelvin.slendermod.registry.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class SafeScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    private final Inventory inventory;
    private final Slot slot;

    public SafeScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1), ScreenHandlerContext.EMPTY);
    }

    public SafeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerRegistry.SAFE_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.context = context;

        slot = addSlot(new Slot(inventory, 0, 80, 20));

        // Player's Inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, y * 18 + 51));
            }
        }

        // Player's Hotbar
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 109));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            int containerSize = 1;
            int inventoryEnd = containerSize + 27;
            int hotbarEnd = inventoryEnd + 9;

            if (slotIndex == this.slot.getIndex()) {
                if (!insertItem(stack1, containerSize, hotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (slotIndex >= containerSize) {
                if (!insertItem(stack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
                else if (slotIndex < inventoryEnd) {
                    if (!insertItem(stack1, inventoryEnd, hotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (slotIndex < hotbarEnd && !insertItem(stack1, containerSize, inventoryEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!insertItem(stack1, containerSize, hotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }
            else {
                slot.markDirty();
            }

            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, stack1);
        }
        return stack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        context.run((world, pos) -> {
            if (world.getBlockEntity(pos) instanceof SafeBlockEntity safeBlockEntity) {
                safeBlockEntity.setOpen(false);
            }
        });
    }
}
