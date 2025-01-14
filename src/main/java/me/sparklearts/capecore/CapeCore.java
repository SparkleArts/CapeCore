package me.sparklearts.capecore;

import com.mojang.logging.LogUtils;
import me.sparklearts.capecore.commands.CapeCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

/**
 * The CapeCore class serves as the main entry point for the CapeCore mod.
 * It initializes essential components for mod functionality, performs setup tasks,
 * and handles both client- and server-specific events via event subscribers.
 * <p>
 * Responsibilities of the class:<br>
 * - Defines the MODID constant used to identify the mod.<br>
 * - Configures mod-specific setup during initialization.<br>
 * - Registers event handlers for common, client, and server events.
 */
@Mod(me.sparklearts.capecore.CapeCore.MODID)
public class CapeCore {

    /**
     * A constant string representing the unique identifier (mod ID) for the mod.
     * This value is used throughout the mod's code to identify resources, register
     * items, blocks, and other mod-specific assets.
     * <p>
     * The mod ID must be unique within the Minecraft ecosystem to avoid conflicts
     * with other mods.
     */
    public static final String MODID = "capecore";

    /**
     * A Logger instance used for logging messages within the CapeCore class.
     * This logger is utilized for debugging, informational outputs, and error
     * reporting during the execution of various methods in this class.
     *
     * <ul>
     * - Common applications include logging messages during the setup phase
     *   and providing runtime information about block registry and behavior.<br>
     * - The logger instance is static and final, ensuring a single, unmodifiable
     *   instance is used throughout the class lifecycle.<br>
     * - Powered by the LogUtils framework for flexible and efficient logging configurations.
     * </ul>
     */
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * The CapeCore class serves as the main entry point for the mod initialization process.
     * It is responsible for registering mod-specific events and setting up the mod during
     * the loading phase.
     * <p>
     * This constructor registers the common setup method on the mod event bus and subscribes
     * the class instance to the Minecraft Forge event bus to handle server and other game events.
     */
    public CapeCore() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Performs the common setup process during the mod initialization phase.
     * This method is executed as part of the FMLCommonSetupEvent and is used
     * to initialize mod-wide settings or configurations that apply to both
     * the client and the server.
     *
     * @param event The FMLCommonSetupEvent instance that provides
     *              context and resources for the common setup process.
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    /**
     * Handles the ServerStartingEvent and performs actions during the server startup phase.
     * This method is triggered when the server is starting.
     *
     * @param event The ServerStartingEvent instance that provides context and allows
     *              interaction with the server as it starts.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    /**
     * The CommandRegisterEvents class is responsible for handling command registration events
     * in the Forge modding framework. It listens for the {@link RegisterCommandsEvent} and
     * registers commands to the provided command dispatcher.
     * <p>
     * Responsibilities:<br>
     * - Subscribes to the {@code RegisterCommandsEvent}.<br>
     * - Registers the "cape" command, which is defined in the {@link CapeCommand} class.
     * <p>
     * Methods:<br>
     * - onRegisterCommands(RegisterCommandsEvent): Handles the registration of commands by calling
     *   {@code CapeCommand.register()} and passing the command dispatcher from the event.
     * <p>
     * This class is marked with {@code @Mod.EventBusSubscriber}, making it a static event listener
     * for events fired on the modding event bus.
     */
    @Mod.EventBusSubscriber
    public static class CommandRegisterEvents {

        /**
         * Handles the registration of commands during the {@link RegisterCommandsEvent}.
         * This method is called when the event is triggered and is responsible for
         * registering all relevant commands to the provided command dispatcher.
         *
         * @param event The event containing the command dispatcher used for registering commands.
         */
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            CapeCommand.register(event.getDispatcher());
        }
    }

    /**
     * The ClientModEvents class is responsible for handling client-specific mod events.
     * It is registered as an event subscriber for the client distribution (client-side operations).
     * <p>
     * Responsibilities of this class include handling the client setup event to perform
     * necessary configurations or interactions required specifically for the client side
     * of the Minecraft game.
     * <p>
     * Methods:<br>
     * - onClientSetup(FMLClientSetupEvent): Called during the client setup phase to execute
     *   client-side initialization tasks.
     */
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        /**
         * Handles the client setup phase of the mod. This method is executed during the client-specific
         * setup event and allows for initialization and configuration tasks required on the client side.
         * It is automatically invoked as part of the mod's client-side event handling.
         *
         * @param event The client setup event that provides context and tools for initializing
         *              client-specific configurations.
         */
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
