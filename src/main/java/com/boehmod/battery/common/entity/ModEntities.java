package com.boehmod.battery.common.entity;

import com.boehmod.battery.Battery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Battery.MOD_ID);

    public static final String BATTERY_ENTITY_NAME = "battery";

    public static final RegistryObject<EntityType<BatteryProjectile>> BATTERY_ENTITY = ENTITY_TYPES.register(BATTERY_ENTITY_NAME, () ->
            EntityType.Builder.of(BatteryProjectile::new, MobCategory.MISC)
                    .sized(0.2F, 0.2F)
                    .setTrackingRange(10)
                    .clientTrackingRange(4)
                    .setUpdateInterval(2)
                    .build(Battery.resourceLocation(BATTERY_ENTITY_NAME).toString())
    );
}
