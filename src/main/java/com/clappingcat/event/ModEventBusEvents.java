package com.clappingcat.event;

import com.clappingcat.ClappingCatMod;
import com.clappingcat.entity.ClappingCatEntity;
import com.clappingcat.registry.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClappingCatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CLAPPING_CAT.get(), ClappingCatEntity.createAttributes().build());
    }
}
