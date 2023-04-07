package com.boehmod.battery.common.item;

import com.boehmod.battery.common.registry.ModItems;
import com.boehmod.battery.common.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BatteryPackItem extends ModItems {

    private final Item item;

    public BatteryPackItem(final int capacity, final Item item) {
        super(new Item.Properties().durability(capacity));
        this.item = item;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        final var itemStack = player.getItemInHand(hand);

        if (itemStack.getDamageValue() >= itemStack.getMaxDamage()) {
            return InteractionResultHolder.fail(itemStack);
        }

        if (!level.isClientSide) {
            player.getInventory().add(new ItemStack(item));

            if (!player.getAbilities().instabuild) {
                itemStack.hurt(1, level.random, null);
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SOUND_ITEM_BATTERY_PACK.get(), SoundSource.PLAYERS, 0.5F, 0.8F + (0.2F * level.random.nextFloat()));
        player.awardStat(Stats.ITEM_USED.get(this));

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
