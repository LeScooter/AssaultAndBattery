package com.boehmod.battery.common.registry;

import com.boehmod.battery.Battery;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Battery.MOD_ID);

    public static final Object2ObjectMap<String, RegistryObject<SoundEvent>> SOUNDS = new Object2ObjectOpenHashMap<>();

    public static final RegistryObject<SoundEvent> SOUND_ITEM_BATTERY_THROW = registerSound(Battery.resourceLocation("item.battery.throw"));
    public static final RegistryObject<SoundEvent> SOUND_ITEM_BATTERY_PACK = registerSound(Battery.resourceLocation("item.battery.pack"));
    public static final RegistryObject<SoundEvent> SOUND_ITEM_BATTERY_ZAP = registerSound(Battery.resourceLocation("item.battery.zap"));
    public static final RegistryObject<SoundEvent> SOUND_ITEM_BATTERY_BOUNCE_DIRT = registerSound(Battery.resourceLocation("item.battery.bounce.dirt"));
    public static final RegistryObject<SoundEvent> SOUND_ITEM_BATTERY_BOUNCE_METAL = registerSound(Battery.resourceLocation("item.battery.bounce.metal"));
    public static final RegistryObject<SoundEvent> SOUND_ITEM_BATTERY_BOUNCE_WOOD = registerSound(Battery.resourceLocation("item.battery.bounce.wood"));

    private static RegistryObject<SoundEvent> registerSound(ResourceLocation name) {
        final var registryObject = SOUND_EVENTS.register(name.getPath(), () -> SoundEvent.createVariableRangeEvent(name));
        SOUNDS.put(name.toString(), registryObject);
        return registryObject;
    }
}
