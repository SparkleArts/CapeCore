package me.sparklearts.capecore.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.sparklearts.capecore.utils.CapeManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * The CapeCommand class provides a set of commands to manage player capes
 * on a Minecraft server. It allows server operators to set, remove, check,
 * or retrieve capes for players.
 * <p>
 * Commands managed by this class require a permission level of 2 (OP level 2).
 */
public class CapeCommand {
    private static final int PERMISSION_LEVEL = 2;
    private static final String PLAYER_NAME_ARG = "playerName";
    private static final String CAPE_PATH_ARG = "capePath";
    /**
     * Registers the "cape" command and its subcommands to the provided command dispatcher.
     * This command allows the management of player capes, including setting, removing,
     * checking, and retrieving cape details for specific players.
     *
     * @param dispatcher The command dispatcher to which the "cape" command will be registered.
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cape")
                .requires(source -> source.hasPermission(PERMISSION_LEVEL)) // Requires OP level 2
                .then(Commands.literal("set")
                        .then(Commands.argument(PLAYER_NAME_ARG, EntityArgument.player())
                                .then(Commands.argument(CAPE_PATH_ARG, StringArgumentType.string())
                                        .executes(context -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, PLAYER_NAME_ARG);
                                            String playerName = player.getGameProfile().getName();
                                            String capePath = StringArgumentType.getString(context, CAPE_PATH_ARG);

                                            return setCape(context.getSource(), playerName, capePath);
                                        })
                                )
                        )
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument(PLAYER_NAME_ARG, EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, PLAYER_NAME_ARG);
                                    String playerName = player.getGameProfile().getName();

                                    return removeCape(context.getSource(), playerName);
                                })
                        )
                )
                .then(Commands.literal("check")
                        .then(Commands.argument(PLAYER_NAME_ARG, EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, PLAYER_NAME_ARG);
                                    String playerName = player.getGameProfile().getName();

                                    return checkCape(context.getSource(), playerName);
                                })
                        )
                )
                .then(Commands.literal("get")
                        .then(Commands.argument(PLAYER_NAME_ARG, EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, PLAYER_NAME_ARG);
                                    String playerName = player.getGameProfile().getName();

                                    return getCape(context.getSource(), playerName);
                                })
                        )
                )
        );
    }

    /**
     * Sets a cape for the specified player using the provided cape path and notifies the command
     * source of the result. The cape is applied to the player if they are online.
     *
     * @param source The command source executing the command, used to send feedback messages.
     * @param playerName The name of the player for whom the cape is to be set.
     * @param capePath The string path to the cape resource to be applied.
     * @return A command result indicating successful execution (1).
     */
    private static int setCape(CommandSourceStack source, String playerName, String capePath) {
        CapeManager.setCape(playerName, capePath);
        source.sendSuccess(() -> Component.literal("Cape set for " + playerName), true);
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Removes the cape associated with the specified player and notifies the command source
     * of the result. If the player does not have a cape, a failure message is sent to the
     * command source.
     *
     * @param source The command source executing the command, used for sending feedback messages.
     * @param playerName The name of the player whose cape is to be removed.
     * @return A command result indicating success (1) if the cape was removed, or failure (0) if
     *         the player did not have a cape.
     */
    private static int removeCape(CommandSourceStack source, String playerName) {
        if (CapeManager.hasCape(playerName)) {
            CapeManager.removeCape(playerName);
            source.sendSuccess(() -> Component.literal("Cape removed for " + playerName), true);
            return Command.SINGLE_SUCCESS;
        } else {
            source.sendFailure(Component.literal("Player " + playerName + " does not have a cape."));
            return 0;
        }
    }

    /**
     * Checks if a specified player has a cape and notifies the command source of the result.
     *
     * @param source The command source executing the command, used to send feedback messages.
     * @param playerName The name of the player to check for the presence of a cape.
     * @return A command result indicating successful execution (1) if the player has a cape, or failure (0) if not.
     */
    private static int checkCape(CommandSourceStack source, String playerName) {
        if (CapeManager.hasCape(playerName)) {
            source.sendSuccess(() -> Component.literal(playerName + " has a cape."), true);
            return Command.SINGLE_SUCCESS;
        } else {
            source.sendFailure(Component.literal(playerName + " does not have a cape."));
            return 0;
        }
    }

    /**
     * Retrieves the cape of a specified player and sends the cape information as feedback
     * to the command source. If the player does not exist or has no cape, an error will occur.
     *
     * @param source The command source executing the command, used for sending feedback messages.
     * @param playerName The name of the player whose cape is being retrieved.
     * @return A command result indicating successful execution (1).
     */
    private static int getCape(CommandSourceStack source, String playerName) {
        ResourceLocation cape = CapeManager.getCape(playerName);

        source.sendSuccess(() -> Component.literal(playerName + "'s cape: " + cape.toString()), false);
        return Command.SINGLE_SUCCESS; // Success
    }
}
