package com.boehmod.battery.common.registry;

import com.boehmod.battery.Battery;
import com.boehmod.battery.common.item.BatteryItem;
import com.boehmod.battery.common.item.BatteryPackItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems extends Item {

    /**
     * Registry for custom items
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Battery.MOD_ID);

    /**
     * Batteries
     */
    public static RegistryObject<Item> BATTERY_AA = ITEMS.register("battery_aa", () -> new BatteryItem(12, 4, 1.25F).setZap(0.2F, 2.0F));
    public static RegistryObject<Item> BATTERY_AAA = ITEMS.register("battery_aaa", () -> new BatteryItem(8, 3, 2.0F).setZap(0.4F, 3.0F));
    public static RegistryObject<Item> BATTERY_9V = ITEMS.register("battery_9v", () -> new BatteryItem(2, 8, 0.8F).setZap(0.8F, 5.0F));
    public static RegistryObject<Item> BATTERY_1_5V = ITEMS.register("battery_1_5v", () -> new BatteryItem(1, 6, 1.0F).setZap(0.6F, 6.0F));

    /**
     * Battery Packs
     */
    public static RegistryObject<Item> BATTERY_PACK_AA = ITEMS.register("battery_pack_aa", () -> new BatteryPackItem(12, BATTERY_AA.get()));
    public static RegistryObject<Item> BATTERY_PACK_AAA = ITEMS.register("battery_pack_aaa", () -> new BatteryPackItem(8, BATTERY_AAA.get()));
    public static RegistryObject<Item> BATTERY_PACK_9V = ITEMS.register("battery_pack_9v", () -> new BatteryPackItem(2, BATTERY_9V.get()));
    public static RegistryObject<Item> BATTERY_PACK_1_5V = ITEMS.register("battery_pack_1_5v", () -> new BatteryPackItem(1, BATTERY_1_5V.get()));

    /**
     * Creative Tabs
     */
    public static CreativeModeTab CREATIVE_MODE_TAB_ITEMS;

    /**
     * Default constructor for a Mod Item
     *
     * @param properties - Given {@link Item.Properties} instance for the item properties
     */
    public ModItems(Item.Properties properties) {
        super(properties);
    }

}
