package kelvin.slendermod.registry;

import kelvin.slendermod.SlenderMod;
import kelvin.slendermod.block.*;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.LinkedList;

import static kelvin.slendermod.SlenderMod.id;


@SuppressWarnings("unused")
public class BlockRegistry {

    public static final SCPSlendermanHeadBlock SCP_SLENDERMAN_HEAD = register("scp_slenderman_head", new SCPSlendermanHeadBlock(AbstractBlock.Settings.copy(Blocks.SKELETON_SKULL)));

    public static final LeavesBlock DEAD_LEAVES = register("dead_leaves", new LeavesBlock(AbstractBlock.Settings.copy(Blocks.OAK_LEAVES)));

    public static final Block EXIT_SIGN = register("exit_sign", new ExitSignBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_SIGN).mapColor(MapColor.WHITE)));

    public static final Block SHELF_CONS = register("shelf_cons", new ShelfConsBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).noCollision()));

    public static final Block BED = register("bed", new SubBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.RED_BED)));

    public static final Block HOSPITAL_BED = register("hospital_bed", new HospitalBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.RED_BED)));

    public static final Block DEAD_TREE = register("dead_tree", new RotatableBlockEntityBlock<>(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).nonOpaque().mapColor(MapColor.SPRUCE_BROWN), "dead_tree"));

    public static final Block SCRATCHED_DEAD_TREE = register("scratched_dead_tree", new RotatableBlockEntityBlock<>(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).nonOpaque().mapColor(MapColor.SPRUCE_BROWN), "scratched_dead_tree"));

    public static final Block CAR_BODY = register("car_body", new RotatableBlockEntityBlock<>(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque(), "car_body"));

    public static final Block JUNK_PILE = register("junk_pile", new RotatableBlockEntityBlock<>(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).nonOpaque(), "junk_pile"));

    public static final Block DEBRIS = register("debris", new RotatableBlockEntityBlock<>(AbstractBlock.Settings.copy(Blocks.STONE_BUTTON).nonOpaque(), "debris"));

    public static final Block UFO_INTERIOR = register("ufo_interior", new RotatableBlockEntityBlock<>(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque().noCollision(), "ufo_interior"));

    public static final Block BARBED_WIRE_FENCE = register("barbed_wire_fence", new CustomFenceBlock(AbstractBlock.Settings.copy(Blocks.SPRUCE_FENCE)));

    public static final Block BONES = register("bones", new BonesBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.PALE_YELLOW).strength(2.0F).sounds(BlockSoundGroup.BONE).noCollision()));

    public static final Block DEAD_GRASS_BLOCK = register("dead_grass_block", new Block(AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK)));

    public static final Block MANHOLE_COVER = register("manhole_cover", new ManholeCoverBlock(AbstractBlock.Settings.copy(Blocks.IRON_TRAPDOOR)));

    public static final Block ACCESS_READER = register("access_reader", new AccessReaderBlock(AbstractBlock.Settings.copy(Blocks.STONE)));

    public static final Block RADIO = register("radio", new RadioBlock(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.METAL)));

    public static final Block SAFE = Registry.register(Registries.BLOCK, new Identifier(SlenderMod.MOD_ID, "safe"), new SafeBlock(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.METAL)));


    public static final LinkedList<Block> PAGES = registerPages();

    public static final Block TRASH_BIN = register("trash_bin", new TrashBinBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).strength(2, 3).sounds(BlockSoundGroup.COPPER)));

    public static final Block MISSING_PERSON_POSTER = register("missing_person_poster", new PageBlock());

    public static final Block DUMPSTER = register("dumpster", new DumpsterBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)));

    public static final Block NO_ENTRY_SIGN = register("no_entry_sign", new NoEntrySignBlock(AbstractBlock.Settings.copy(Blocks.STONE)));

    public static final Block CEMENT_BRICKS = register("cement_bricks", new Block(AbstractBlock.Settings.copy(Blocks.STONE)));

    public static final Block BARBED_WIRE_ROLL = register("barbed_wire_roll", new BarbedWireBlock(AbstractBlock.Settings.of(Material.METAL).sounds(BlockSoundGroup.WOOD).strength(2, 3).nonOpaque()));

    public static final Block AIR_CONDITIONER = register("air_conditioner", new AirConditionerBlock(AbstractBlock.Settings.of(Material.METAL).sounds(BlockSoundGroup.COPPER).nonOpaque()));

    public static final Block BLOOD = register("blood", new CarpetBlock(AbstractBlock.Settings.of(Material.ORGANIC_PRODUCT, MapColor.RED).breakInstantly().sounds(BlockSoundGroup.SLIME).nonOpaque().noCollision()));

    public static final Block CCTV_CAMERA = register("cctv_camera", new CCTVCameraBlock(AbstractBlock.Settings.of(Material.METAL).strength(4).nonOpaque().noCollision()));

    public static final Block TOXIC_BARREL = register("toxic_barrel", new ToxicBarrelBlock(AbstractBlock.Settings.of(Material.METAL).strength(4).nonOpaque()));

    public static void register() {
    }

    private static <T extends Block> T register(String name, T block) {
        T instance = Registry.register(Registries.BLOCK, id(name), block);
        Registry.register(Registries.ITEM, id(name), new BlockItem(instance, new Item.Settings()));
        return instance;
    }

    public static LinkedList<Block> registerPages() {
        LinkedList<Block> list = new LinkedList<>();
        for (int i = 1; i < 9; i++)
            list.add(register("page_" + i, new PageBlock()));
        return list;
    }
}
