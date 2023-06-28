package kelvin.slendermod.mixin;

import kelvin.slendermod.SlenderModClient;
import kelvin.slendermod.network.server.ServerPacketHandler;
import kelvin.slendermod.registry.ItemRegistry;
import kelvin.slendermod.util.IForceCrawlingPlayer;
import kelvin.slendermod.util.NoteScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends LivingEntity {

    @Final
    @Shadow
    protected MinecraftClient client;

    private boolean isCrawlKeyPressed;

    protected ClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isCrawling() {
        return super.isCrawling() || ((IForceCrawlingPlayer) this).isForcedCrawling();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        if (SlenderModClient.CRAWL_KEY.isPressed()) {
            if (!this.isCrawlKeyPressed) {
                this.isCrawlKeyPressed = true;
                ((IForceCrawlingPlayer) this).toggleForcedCrawling();
                ClientPlayNetworking.send(ServerPacketHandler.TOGGLED_FORCED_CRAWLING_ID, PacketByteBufs.create());
            }
        } else {
            this.isCrawlKeyPressed = false;
        }
    }

    @Inject(at = @At("HEAD"), method = "useBook")
    public void useBook(ItemStack book, Hand hand, CallbackInfo ci) {
        if (book.isOf(ItemRegistry.WRITABLE_NOTE)) {
            NoteScreen screen = (NoteScreen) new BookEditScreen((PlayerEntity) (Object) this, book, hand);
            screen.isNote();
            this.client.setScreen((Screen) screen);
        }
    }
}
