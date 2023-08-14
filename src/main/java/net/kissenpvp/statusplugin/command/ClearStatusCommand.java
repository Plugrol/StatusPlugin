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

package net.kissenpvp.statusplugin.command;

import net.kissenpvp.statusplugin.EventCancelledException;
import net.kissenpvp.statusplugin.StatusPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Class that orchestrates the execution of the `clearStatus` command within the game.
 *
 * <p>This class implements {@link CommandExecutor} interface, and hence overrides
 * the {@code onCommand()} method which is called whenever the applicable command
 * (in this case, 'clearStatus') is invoked in the game. This method defines the
 * actions that should be performed when the 'clearStatus' command is used.
 *
 * <p>In the context of the game, 'clearStatus' command is used by a player to remove
 * their current status. This class validates if the command sender is a player and
 * then performs the actions of status clearance using the {@code StatusPlugin} and
 * subsequently interacts with the player to convey the result of the operation.
 *
 * @see CommandExecutor
 */
public class ClearStatusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can execute this command.");
            return true;
        }


        StatusPlugin.getInstance().getStatus(player).ifPresentOrElse(status -> {
            try
            {
                StatusPlugin.getInstance().clearStatus(player);
                player.sendMessage(String.format("§7Your status §f%s §7has been removed.", status.strip()));
            }catch (EventCancelledException eventCancelledException)
            {
                player.sendMessage("§cNothing has been changed.");
            }
        }, () -> player.sendMessage("§7You have no status selected at the moment."));
        return true;
    }
}
