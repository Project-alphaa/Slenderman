package kelvin.slendermod.registry;

import kelvin.slendermod.SlenderMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;


@SuppressWarnings("unused")
public class SoundRegistry {

    public static final SoundEvent HORROR_ROAR = register("horror_roar");
    public static final SoundEvent HORROR_GROWL = register("horror_growl");
    public static final SoundEvent SMALL_SLENDER_LOOKING = register("small_slender_looking");
    public static final SoundEvent SMALL_SLENDER_CHASING = register("small_slender_chasing");
    public static final SoundEvent SHOCK = register("shock");
    public static final SoundEvent BREATHING = register("breathing");
    public static final SoundEvent HEARTBEAT = register("heartbeat");
    public static final SoundEvent SOMETHING_APPROACHES = register("something_approaches");
    public static final SoundEvent WIND = register("wind");
    public static final SoundEvent BUZZING = register("buzzing");
    public static final SoundEvent BOSS_DASH = register("boss_dash");
    public static final SoundEvent BOSS_ATTACK = register("boss_attack");
    public static final SoundEvent BOSS_IDLE = register("boss_idle");
    public static final SoundEvent FLASHLIGHT_SWITCH = register("flashlight_switch");
    public static final SoundEvent ACCESS_GRANTED = register("access_granted");
    public static final SoundEvent RADIO_STATIC = register("radio_static");
    public static final SoundEvent RADIO_BUTTON_CLICK = register("radio_button_click");
    public static final SoundEvent SLENDERMAN_CRYING = register("slenderman_crying");
    public static final SoundEvent TAPE = register("tape");
    public static final SoundEvent MEDICAL_KIT_USE = register("medical_kit_use");


    public static void register() {
    }

    public static SoundEvent register(String location, float distance) {
        Identifier id = SlenderMod.id(location);
        SoundEvent sound = SoundEvent.of(id, distance);
        Registry.register(Registries.SOUND_EVENT, id, sound);
        return sound;
    }

    public static SoundEvent register(String location) {
        Identifier id = SlenderMod.id(location);
        SoundEvent sound = SoundEvent.of(id);
        Registry.register(Registries.SOUND_EVENT, id, sound);
        return sound;
    }
}
