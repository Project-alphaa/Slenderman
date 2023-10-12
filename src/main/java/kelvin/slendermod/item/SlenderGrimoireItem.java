package kelvin.slendermod.item;

import kelvin.slendermod.client.item.renderers.SlenderGrimoireRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.resource.Resource;
import net.minecraft.util.ActionResult;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static kelvin.slendermod.SlenderMod.LOGGER;
import static kelvin.slendermod.SlenderMod.id;

public class SlenderGrimoireItem extends WrittenBookItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public SlenderGrimoireItem() {
        super(new Settings());
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    public static boolean writeCustomNBT(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();

        if (nbtCompound != null && !nbtCompound.getBoolean(RESOLVED_KEY)) {
            NbtList nbtList = new NbtList();
            String[] pageNames = getBookFileLines("pages.txt", false);
            for (String pageName : pageNames) {
                String[] pageLines = getBookFileLines(pageName, true);
                StringBuilder pageText = new StringBuilder();

                for (String line : pageLines) {
                    pageText.append(line.replaceAll("\r", ""));
                }

                if (pageText.length() > MAX_PAGE_VIEW_LENGTH) {
                    return false;
                }

                nbtList.add(NbtString.of(pageText.toString()));
            }

            nbtCompound.putString(TITLE_KEY, "Grimoire");
            nbtCompound.putString(AUTHOR_KEY, "Unknown");
            nbtCompound.putInt(GENERATION_KEY, 0);
            stack.setSubNbt(PAGES_KEY, nbtList);
            return true;
        }
        else {
            return false;
        }
    }

    private static String[] getBookFileLines(String fileName, boolean isPage) {
        try {
            Optional<Resource> file = MinecraftClient.getInstance().getResourceManager().getResource(id("book/" + (isPage ? "pages/" : "") + fileName.trim()));
            InputStream stream = file.orElseThrow(FileNotFoundException::new).getInputStream();
            String text = new String(stream.readAllBytes());
            return text.split("\\|");
        }
        catch (Exception e) {
            LOGGER.warn("Failed to read book text file: " + fileName, e);
        }
        return new String[]{};
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {

            private SlenderGrimoireRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new SlenderGrimoireRenderer();
                }

                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, state -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
