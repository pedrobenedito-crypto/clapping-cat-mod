package com.clappingcat;

import com.clappingcat.registry.ModEntities;
import com.clappingcat.registry.ModSounds;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ClappingCatMod.MOD_ID)
public class ClappingCatMod {
    public static final String MOD_ID = "clappingcat";

    public ClappingCatMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(modBus);
        ModSounds.SOUNDS.register(modBus);
    }
}
