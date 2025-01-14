package me.sparklearts.capecore.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;

/**
 * The CapeManager class provides utility methods for managing player capes
 * in a Minecraft server environment. It enables setting, removing, and
 * retrieving capes for players using their usernames or UUIDs.
 */
public class CapeManager {
    private static final Map<String, ResourceLocation> capeMap = new HashMap<>();
    private static final ResourceLocation EMPTY_RESOURCE = null;
    private static final String EMPTY_STRING = null;

    /**
     * Associates a cape with a player, identified by their username or UUID.
     *
     * @param identifier The identifier of the player, which can be either a UUID or a String representing the username.
     * @param capePath The path to the cape resource to be assigned to the player.
     * @throws IllegalArgumentException if the identifier is neither a UUID nor a String.
     */
    public static void setCape(Object identifier, String capePath) {
        String playerName = null;
        if (identifier instanceof UUID) {
            playerName = String.valueOf(getUsernameFromUUID((UUID) identifier));
        } else if (identifier instanceof String) {
            playerName = (String) identifier;
        } else {
            throw new IllegalArgumentException("Identifier must be either a UUID or a String");
        }

        if (playerName != null) {
            capeMap.put(playerName, new ResourceLocation(capePath));
        }
    }

    /**
     * Removes the cape associated with the specified player.
     *
     * @param playerName The name of the player whose cape should be removed.
     */
    public static void removeCape(String playerName) {
        capeMap.remove(playerName);
    }

    /**
     * Determines if the specified player has a cape.
     *
     * @param playerName the name of the player to check
     * @return true if the player has a cape, otherwise false
     */
    public static boolean hasCape(String playerName) {
        return capeMap.containsKey(playerName);
    }

    /**
     * Retrieves the cape associated with the specified player, if one exists.
     *
     * @param playerName The name of the player whose cape should be retrieved.
     * @return An {@code Optional} containing the cape's {@code ResourceLocation} if the player has a cape,
     *         or an empty {@code Optional} if no cape is associated with the player.
     */
    public static ResourceLocation getCape(String playerName) {
        return capeMap.getOrDefault(playerName, EMPTY_RESOURCE);
    }

    /**
     * Retrieves the username associated with a given player's UUID.
     *
     * @param playerUUID The UUID of the player whose username is to be retrieved.
     * @return An {@code Optional} containing the username if the player is found, or an empty {@code Optional} otherwise.
     */
    public static String getUsernameFromUUID(UUID playerUUID) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);
            if (player != null) {
                return player.getGameProfile().getName();
            }
        }
        return EMPTY_STRING;
    }

    /**
     * Retrieves an unmodifiable view of the current map of capes. The map contains
     * mappings between player identifiers (e.g., usernames) and their corresponding
     * cape resource locations.
     *
     * @return An unmodifiable map containing player identifiers as keys and their associated
     *         {@code ResourceLocation} objects as values.
     */
    public static Map<String, ResourceLocation> getCapeMap() {
        return Collections.unmodifiableMap(capeMap);
    }

    /**
     * Retrieves the default empty ResourceLocation, representing an uninitialized or non-existent resource.
     *
     * @return A {@code ResourceLocation} instance that represents an empty or default resource.
     */
    public static ResourceLocation getEmptyResource() {
        return EMPTY_RESOURCE;
    }

    /**
     * Retrieves the default empty username.
     *
     * @return A string representing the default empty username.
     */
    public static String getEmptyString() {
        return EMPTY_STRING;
    }
}
