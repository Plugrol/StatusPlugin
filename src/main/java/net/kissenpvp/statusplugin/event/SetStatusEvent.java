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

package net.kissenpvp.statusplugin.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event in the game indicating a status change for a player.
 *
 * <p>The `SetStatusEvent` class extends {@link Event} and implements
 * {@link Cancellable}, indicating that the event can be cancelled by
 * event listeners within the game. This event contains the player entity
 * associated with the status change and the new status itself.
 * <p>
 * The class is enriched with Lombok's {@link lombok.Getter} and
 * {@link lombok.Setter} annotations providing field-level getters and
 * setters automatically.
 * <p>
 * This event class also maintains a static {@code HandlerList} which holds all
 * the handlers registered for this event. This list is used when the event is
 * called, to pass it to all the handlers that have registered for this event type.
 *
 * @see Event
 * @see Cancellable
 * @see lombok.Getter
 * @see lombok.Setter
 */
@Getter
@Setter
public class SetStatusEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private boolean cancelled;
    private String status;

    /**
     * Constructor for creating a new {@code SetStatusEvent}.
     *
     * <p>This constructor creates a {@code SetStatusEvent} instance with the required
     * player and status details. It also sets the initial state of the event to
     * 'not cancelled'.
     *
     * @param player the player for whom the status change event is being created.
     * Must be non-{@code null}.
     * @param status the new status that is intended to be set for the provided player.
     * Must be non-{@code null}.
     */
    public SetStatusEvent(@NotNull Player player, @NotNull String status) {
        this.player = player;
        this.status = status;
        this.cancelled = false;
    }

    @SuppressWarnings("unused") // Bukkit...
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
