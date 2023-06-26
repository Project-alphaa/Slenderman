package kelvin.slendermod.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static kelvin.slendermod.SlenderMod.id;

public class ParticleRegistry {

    public static final DefaultParticleType SLENDERMAN_MAGIC = register("slenderman_magic");

    public static void register() {
    }

    private static DefaultParticleType register(String name) {
        return Registry.register(Registries.PARTICLE_TYPE, id(name), FabricParticleTypes.simple());
    }
}
