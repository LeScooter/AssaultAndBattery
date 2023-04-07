package com.boehmod.battery.common.entity;

import com.boehmod.battery.common.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class BatteryProjectile extends ThrowableItemProjectile {

    private float damage = 0F;
    private float zapDamage = 0F;
    private float zapRadius = -1.F;
    private int zapTimer = 0;

    public BatteryProjectile(EntityType<? extends BatteryProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        final boolean canZap = zapRadius > -1.F && isInWater();

        if (!level.isClientSide) {
            if (canZap) {

                if (zapTimer-- <= 0) {
                    zapTimer = 10;

                    final var damageSource = (new IndirectEntityDamageSource("battery_zap", this, getOwner())).setProjectile();
                    final var entities = level.getEntities(this, getBoundingBox().inflate(zapRadius), entity -> true);

                    if (!entities.isEmpty()) {
                        level.playSound(null, getOnPos(), ModSounds.SOUND_ITEM_BATTERY_ZAP.get(), SoundSource.BLOCKS, 1.5F, 0.9F + (0.2F * level.random.nextFloat()));
                    }

                    for (final var entity : entities) {
                        if (entity instanceof LivingEntity && entity.isInWater()) {
                            entity.hurt(damageSource, zapDamage);
                        }
                    }
                }
            }
        }
    }

    public void setDamage(final float damage) {
        this.damage = damage;
    }

    public void setZap(final float zapDamage, final float zapRadius) {
        this.zapDamage = zapDamage;
        this.zapRadius = zapRadius;
    }

    protected @NotNull Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    private ParticleOptions getParticle() {
        final var itemStack = getItemRaw();
        return itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemStack);
    }

    /**
     * Called to create a battery explosion
     *
     * @param entity - Given affiliated {@link Entity} instance
     * @param level  - Given affiliated {@link Level} instance
     * @param pos    - Given position in which to create the explosion
     */
    private static void createBatteryExplosion(final Entity entity, final Level level, final BlockPos pos) {
        final var damageSource = new IndirectEntityDamageSource("battery_explosion", entity, null).setProjectile();
        level.explode(null, damageSource, null, pos.getCenter(), 5.0F, true, Level.ExplosionInteraction.BLOCK);
        level.playSound(null, pos, ModSounds.SOUND_ITEM_BATTERY_ZAP.get(), SoundSource.BLOCKS, 1.5F, 0.9F + (0.2F * level.random.nextFloat()));
        level.playSound(null, pos, ModSounds.SOUND_ITEM_BATTERY_ZAP.get(), SoundSource.BLOCKS, 1.5F, 0.0F);
    }

    @Override
    public void handleEntityEvent(byte b) {
        super.handleEntityEvent(b);

        if (b == 3) {
            final var particle = getParticle();

            for (int i = 0; i < 8; ++i) {
                level.addParticle(particle, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        final var entity = hitResult.getEntity();
        final var damageSource = (new IndirectEntityDamageSource("battery", this, getOwner())).setProjectile();
        entity.hurt(damageSource, damage);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        if (!level.isClientSide) {
            level.broadcastEntityEvent(this, (byte) 3);
            discard();

            if (hitResult instanceof BlockHitResult blockHitResult) {
                final var blockPos = blockHitResult.getBlockPos();
                final var blockState = level.getBlockState(blockPos);

                // If the block is metal, explode
                if (blockState.getMaterial() == Material.METAL || blockState.getMaterial() == Material.HEAVY_METAL) {
                    createBatteryExplosion(getOwner(), level, blockPos);
                    return;
                }

                var impactSound = ModSounds.SOUND_ITEM_BATTERY_BOUNCE_DIRT;

                if (blockState.getMaterial() == Material.WOOD) {
                    impactSound = ModSounds.SOUND_ITEM_BATTERY_BOUNCE_WOOD;
                } else if (blockState.getMaterial() == Material.METAL) {
                    impactSound = ModSounds.SOUND_ITEM_BATTERY_BOUNCE_METAL;
                }

                level.playSound(null, blockPos, impactSound.get(), SoundSource.BLOCKS, 1.5F, 1.2F + (0.2F * level.random.nextFloat()));
            }
        }
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}