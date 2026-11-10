package com.clappingcat.registry;

import com.clappingcat.ClappingCatMod;
import com.clappingcat.entity.ClappingCatEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ClappingCatMod.MOD_ID);

    public static final RegistryObject<EntityType<ClappingCatEntity>> CLAPPING_CAT = ENTITIES.register("clapping_cat",
            () -> EntityType.Builder.of(ClappingCatEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.2F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(ClappingCatMod.MOD_ID, "clapping_cat").toString()));
}
