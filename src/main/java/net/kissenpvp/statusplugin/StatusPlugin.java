/*
 * Copyright (C) 2023 KissenPvP
 *
 * This program is licensed under the Apache License, Version 2.0.
 *
 * This software may be redistributed and/or modified under the terms
 * of the Apache License as published by the Apache Software Foundation,
 * either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the Apache
 * License, Version 2.0 for the specific language governing permissions
 * and limitations under the License.
 *
 * You should have received a copy of the Apache License, Version 2.0
 * along with this program. If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */

package net.kissenpvp.statusplugin;

import lombok.Getter;
import net.kissenpvp.statusplugin.command.ClearStatusCommand;
import net.kissenpvp.statusplugin.event.ClearStatusEvent;
import net.kissenpvp.statusplugin.event.SetStatusEvent;
import net.kissenpvp.statusplugin.command.StatusCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class representing the main status plug-in for the game.
 *
 * <p>This class extends {@link JavaPlugin} and acts as the main class for the
 * status plug-in in the game. By extending JavaPlugin, we have the capacity to
 * override certain methods to dictate the functionality of this plug-in. This
 * can include actions on enabling and disabling the plug-in, implementing
 * commands, interacting with events, and more.
 *
 * <p>The plug-in is responsible for handling player statuses, events associated
 * with status changes and refreshing game components when a status change occurs.
 *
 * <p>The {@code final} keyword ensures that this class cannot be subclassed.
 * This makes sense for a main plug-in class as there should only be one main
 * class for a plug-in.
 *
 * @see JavaPlugin
 */
public final class StatusPlugin extends JavaPlugin {

    @Getter
    private static StatusPlugin instance;
    private final Map<UUID, String> statusMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        statusMap.clear();
        Objects.requireNonNull(getCommand("status")).setExecutor(new StatusCommand());
        Objects.requireNonNull(getCommand("clearstatus")).setExecutor(new ClearStatusCommand());

        getServer().getPluginManager().registerEvents(new Listener() {

            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerJoinEvent(@NotNull PlayerJoinEvent playerJoinEvent) {
                refreshTab(playerJoinEvent.getPlayer());
            }

        }, this);
    }

    /**
     * Changes the status of a provided player and propagates this status change
     * to all relevant game components. The status will not be changed if the
     * {@link SetStatusEvent} is cancelled by any event listener in the game.
     *
     * <p>This method first creates a {@link SetStatusEvent} with the given player
     * and status. This event is then passed to the server's plugin manager which
     * in turn distributes the event to all registered event listeners. If any
     * of these listeners cancels the event, this method will throw an
     * {@link EventCancelledException} and the player's status will remain unchanged.
     *
     * <p>Subsequently, the status is sanitized and a trailing space is added:
     * any ampersand characters ('&') are replaced with section sign characters ('§'),
     * and white space is removed from both ends. Hereafter, the previous status
     * of the player is cleared from the {@code statusMap} and the updated status
     * is inserted. This map relates each player (based on their unique ID) to
     * their corresponding status.
     *
     * <p>Lastly, the {@link #refreshTab(Player)} method is called to ensure the
     * updated status is properly displayed on the game's player tab.
     *
     * @param player the player whose status is to be updated.
     *               Must be non-{@code null}.
     * @param status a String representing the new status of the player.
     *               Must be non-{@code null}.
     * @return a sanitized version of the input status, with ampersands replaced with
     * section signs, white space removed from both ends, and with a trailing space
     * added.
     * @throws EventCancelledException if any event listener in the game cancels
     *                                 the {@link SetStatusEvent} that is fired when changing the player's status.
     * @see SetStatusEvent
     * @see EventCancelledException
     */
    public @NotNull String setStatus(@NotNull Player player, @NotNull String status) throws EventCancelledException {
        status = status.replace('&', '§').strip() + " ";
        SetStatusEvent setStatusEvent = new SetStatusEvent(player, status);
        getServer().getPluginManager().callEvent(setStatusEvent);
        if (setStatusEvent.isCancelled() || setStatusEvent.getStatus().isBlank()) {
            throw new EventCancelledException();
        }

        statusMap.remove(player.getUniqueId()); // clear previous data
        statusMap.put(player.getUniqueId(), status);

        refreshTab(player);
        return status;
    }

    /**
     * Retrieves the status of a provided player from the {@code statusMap}.
     *
     * <p>This method fetches the status of the player referenced by their unique
     * ID from the {@code statusMap}. This map relates each player (based on their
     * unique ID) to their corresponding status.
     *
     * <p>If a status value for the player does not exist in the {@code statusMap},
     * {@link Optional#empty()} is returned.
     *
     * <p>The {@link Optional} wrapper is used to safely handle null values and
     * express the absence of a value, mitigating potential {@link NullPointerException}s.
     *
     * @param player the player whose status is to be retrieved.
     *               Must be non-{@code null}.
     * @return An {@link Optional} that may contain the {@code String} status of the
     * player if it exists in {@code statusMap}. If not, it will contain no value
     * ({@link Optional#empty()}).
     * @throws EventCancelledException - This exception is no longer thrown
     *                                 from this method. This change in method signature might affect an interface
     *                                 it may be overwriting or some other part of your architecture.
     */
    public @NotNull Optional<String> getStatus(@NotNull Player player) throws EventCancelledException {
        return Optional.ofNullable(statusMap.get(player.getUniqueId()));
    }

    /**
     * Clears the status of a provided player if such status exists and propagates
     * this status change to all relevant game components. The status will not be
     * cleared if the {@link ClearStatusEvent} is cancelled by any event listener
     * in the game.
     *
     * <p>This method first retrieves the current status of the player using the
     * {@link #getStatus(Player)} method. If a status exists, a
     * {@link ClearStatusEvent} with the given player and the status is created.
     * This event is then passed to the server's plugin manager which in turn
     * distributes the event to all registered event listeners. If any of these
     * event listeners cancels the event, this method will throw an
     * {@link EventCancelledException} and the player's status will remain active.
     *
     * <p>If the event is not cancelled, the status of the player is removed from
     * the {@code statusMap}. Lastly, the {@link #refreshTab(Player)} method is
     * called to ensure the updated (empty) status is properly displayed on the
     * game's player tab.
     *
     * @param player the player whose status is to be cleared.
     *               Must be non-{@code null}.
     * @throws EventCancelledException if any event listener cancels the
     *                                 {@link ClearStatusEvent} that is fired when clearing the player's status.
     * @see ClearStatusEvent
     * @see EventCancelledException
     */
    public void clearStatus(@NotNull Player player) throws EventCancelledException {
        Optional<String> status = getStatus(player);
        if (status.isPresent()) {
            ClearStatusEvent clearStatusEvent = new ClearStatusEvent(player, status.get());
            getServer().getPluginManager().callEvent(clearStatusEvent);
            if (clearStatusEvent.isCancelled()) {
                throw new EventCancelledException();
            }
            statusMap.remove(player.getUniqueId());
            refreshTab(player);
        }
    }

    /**
     * Refreshes the player tab for a specified player.
     *
     * <p>This method retrieves the status of the player and concatenates it with
     * the player's name. If the player does not have a status, only the player's
     * name is used. This result is then set as the player list name of the
     * specified player, thereby refreshing the display of the player's name
     * in the game tab.
     *
     * @param player the player whose player tab is to be refreshed. Must be non-{@code null}.
     */
    private void refreshTab(@NotNull Player player) {
        player.setPlayerListName(getStatus(player).map(status -> status + player.getName()).orElse(player.getName()));
    }
}
