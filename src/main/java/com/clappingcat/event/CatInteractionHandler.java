package com.clappingcat.event;

import com.clappingcat.ClappingCatMod;
import com.clappingcat.entity.ClappingCatEntity;
import com.clappingcat.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Right-click a cat with raw salmon -> it becomes a Clapping Cat. */
@Mod.EventBusSubscriber(modid = ClappingCatMod.MOD_ID)
public class CatInteractionHandler {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Cat cat) || !cat.isAlive()) {
            return;
        }
        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.SALMON)) {
            return;
        }

        Level level = event.getLevel();
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
        if (level.isClientSide()) {
            return;
        }

        Player player = event.getEntity();
        ClappingCatEntity clapper = ModEntities.CLAPPING_CAT.get().create(level);
        if (clapper == null) {
            return;
        }

        CompoundTag catTag = new CompoundTag();
        cat.saveWithoutId(catTag);
        clapper.setOriginalCat(catTag, cat.getResourceLocation());
        clapper.moveTo(cat.getX(), cat.getY(), cat.getZ(), cat.getYRot(), 0.0F);
        if (cat.hasCustomName()) {
            clapper.setCustomName(cat.getCustomName());
            clapper.setCustomNameVisible(cat.isCustomNameVisible());
        }
        clapper.setPersistenceRequired();

        level.addFreshEntity(clapper);
        cat.discard();

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HEART, clapper.getX(), clapper.getY() + 1.2D, clapper.getZ(),
                    7, 0.4D, 0.4D, 0.4D, 0.02D);
        }
    }
}
