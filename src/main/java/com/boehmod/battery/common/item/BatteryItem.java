package com.boehmod.battery.common.item;

import com.boehmod.battery.common.entity.BatteryProjectile;
import com.boehmod.battery.common.entity.ModEntities;
import com.boehmod.battery.common.registry.ModItems;
import com.boehmod.battery.common.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

public class BatteryItem extends ModItems {

    private final float damage;
    private float zapDamage = 0F;
    private float zapRadius = 0F;
    private final float throwSpeed;

    public BatteryItem(final int stackSize, final float damage, final float throwSpeed) {
        super(new Item.Properties().stacksTo(stackSize));
        this.damage = damage;
        this.throwSpeed = throwSpeed;
    }

    /**
     * Called to set the zap damage of this battery
     *
     * @param zapDamage - Given damage to set
     * @param zapRadius - Given radius in which to zap
     * @return - Returns the initial {@link BatteryItem} instance
     */
    public BatteryItem setZap(final float zapDamage, final float zapRadius) {
        this.zapDamage = zapDamage;
        this.zapRadius = zapRadius;
        return this;
    }

    /**
     * Called to spawn a new battery projectile
     *
     * @param level     - Given affiliated {@link Level} instance
     * @param player    - Given {@link Player} instance that threw the battery
     * @param itemStack - Given {@link ItemStack} to set as the affiliated item
     */
    private void spawnBatteryProjectile(final Level level, final Player player, final ItemStack itemStack) {
        final var batteryProjectile = ModEntities.BATTERY_ENTITY.get().create(player.level);
        if (batteryProjectile == null) return;

        batteryProjectile.setPos(player.getEyePosition());
        batteryProjectile.setOwner(player);
        batteryProjectile.setDamage(damage);
        batteryProjectile.setZap(zapDamage, zapRadius);
        batteryProjectile.setItem(itemStack);
        batteryProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, throwSpeed, 1.5F);
        level.addFreshEntity(batteryProjectile);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        final var itemInHand = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SOUND_ITEM_BATTERY_THROW.get(), SoundSource.NEUTRAL, 0.5F, .8F + (0.2F * level.random.nextFloat()));

        if (!level.isClientSide) {
            spawnBatteryProjectile(level, player, itemInHand);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemInHand.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide());
    }
}
