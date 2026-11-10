package com.clappingcat.entity;

import com.clappingcat.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * A cat that stands on its hind legs, hovers a little and claps forever.
 * It remembers the original cat (all NBT) so it can be turned back.
 */
public class ClappingCatEntity extends PathfinderMob {
    /** One full clap cycle, in ticks. */
    public static final int CLAP_PERIOD = 7;
    public static final float CLAP_OMEGA = (float) (2.0D * Math.PI / CLAP_PERIOD);

    private static final String DEFAULT_TEXTURE = "minecraft:textures/entity/cat/all_black.png";
    private static final EntityDataAccessor<String> TEXTURE =
            SynchedEntityData.defineId(ClappingCatEntity.class, EntityDataSerializers.STRING);

    private CompoundTag originalCat = new CompoundTag();

    public ClappingCatEntity(EntityType<? extends ClappingCatEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TEXTURE, DEFAULT_TEXTURE);
    }

    public void setOriginalCat(CompoundTag tag, ResourceLocation texture) {
        this.originalCat = tag.copy();
        this.entityData.set(TEXTURE, texture.toString());
    }

    public ResourceLocation getTextureLocation() {
        ResourceLocation loc = ResourceLocation.tryParse(this.entityData.get(TEXTURE));
        return loc != null ? loc : new ResourceLocation(DEFAULT_TEXTURE);
    }

    @Override
    public void tick() {
        super.tick();
        // Client-side only, so the clap sound is synced with the animation (which uses tickCount).
        if (this.level().isClientSide && this.tickCount % CLAP_PERIOD == 2) {
            this.level().playLocalSound(this.getX(), this.getY() + 0.8D, this.getZ(),
                    ModSounds.CLAP.get(), SoundSource.NEUTRAL, 0.6F, 0.95F + this.random.nextFloat() * 0.1F, false);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // Shift + empty hand turns it back into a normal cat.
        if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty()) {
            if (!this.level().isClientSide) {
                this.revertToCat();
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    private void revertToCat() {
        Cat cat = EntityType.CAT.create(this.level());
        if (cat == null) {
            return;
        }
        if (!this.originalCat.isEmpty()) {
            cat.load(this.originalCat.copy());
        }
        cat.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
        if (this.hasCustomName()) {
            cat.setCustomName(this.getCustomName());
        }
        this.level().addFreshEntity(cat);
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HEART, this.getX(), this.getY() + 1.0D, this.getZ(),
                    5, 0.3D, 0.3D, 0.3D, 0.02D);
        }
        this.discard();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(4) == 0 ? SoundEvents.CAT_PURR : SoundEvents.CAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("CatTexture", this.entityData.get(TEXTURE));
        tag.put("OriginalCat", this.originalCat.copy());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("CatTexture")) {
            this.entityData.set(TEXTURE, tag.getString("CatTexture"));
        }
        if (tag.contains("OriginalCat")) {
            this.originalCat = tag.getCompound("OriginalCat");
        }
    }
}
