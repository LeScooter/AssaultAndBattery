package com.boehmod.battery;

import com.boehmod.battery.common.registry.ModSounds;
import com.boehmod.battery.common.entity.ModEntities;
import com.boehmod.battery.common.registry.ModItems;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("assbat")
public class Battery {

    public static final String MOD_NAME = "Assault And Battery";
    public static final String MOD_VERSION = "0.0.0.1";
    public static final String MOD_ID = "assbat";

    private static Battery MOD_INSTANCE;

    /**
     * Mod logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(Battery.MOD_NAME);

    public Battery() {

        // Declare the mod instance
        MOD_INSTANCE = this;

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Fetch the forge mod event bus
        final var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);

        if (isClientSide()) {
            modEventBus.addListener(this::onClientRenderRegistry);
            modEventBus.addListener(this::onRegisterCreativeTabsEvent);
        }

    }

    public static ResourceLocation resourceLocation(final String location) {
        return new ResourceLocation(MOD_ID, location);
    }

    @OnlyIn(Dist.CLIENT)
    private void onClientRenderRegistry(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BATTERY_ENTITY.get(), ThrownItemRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public void onRegisterCreativeTabsEvent(final CreativeModeTabEvent.Register event) {
        log("Registering creative tabs...");

        // Creative tab for all items
        ModItems.CREATIVE_MODE_TAB_ITEMS = event.registerCreativeModeTab(Battery.resourceLocation("assbat_tab_items"),
                builder -> builder.icon(() -> new ItemStack(ModItems.BATTERY_AAA.get()))
                        .title(Component.translatable("assbat.tab.items"))
                        .displayItems((features, output, hasPermissions) -> ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()))));

        log("Successfully registered creative tabs!");
    }

    public static boolean isClientSide() {
        return FMLEnvironment.dist.equals(Dist.CLIENT);
    }

    public static boolean isServerSide() {
        return FMLEnvironment.dist.equals(Dist.DEDICATED_SERVER);
    }

    /**
     * Log - Log a specific message/output from the mod
     *
     * @param message    - Given {@link String} message
     * @param parameters - Given formatting
     */
    public static void log(final String message, final Object... parameters) {
        LOGGER.info("[" + MOD_NAME + "] " + String.format(message, parameters));
    }

    /**
     * Log - Log a specific error message/output from the mod
     *
     * @param message    - Given {@link String} message
     * @param parameters - Given formatting
     */
    public static void logError(final String message, final Object... parameters) {
        LOGGER.error("[" + MOD_NAME + "] " + String.format(message, parameters));
    }

    public static Battery getInstance() {
        return MOD_INSTANCE;
    }

}
