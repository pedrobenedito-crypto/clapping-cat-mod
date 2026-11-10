package com.clappingcat.registry;

import com.clappingcat.ClappingCatMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ClappingCatMod.MOD_ID);

    public static final RegistryObject<SoundEvent> CLAP = SOUNDS.register("clap",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ClappingCatMod.MOD_ID, "clap")));
}
