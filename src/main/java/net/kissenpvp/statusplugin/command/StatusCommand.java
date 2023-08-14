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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Class representing the command executor for handling status commands in the game.
 *
 * <p>This class implements the {@link CommandExecutor} interface, which means
 * this class is responsible for processing status commands issued by players
 * in the game. By implementing CommandExecutor, this class must override the
 * method {@code onCommand()} which determines the operations to be performed
 * when a status command is executed.
 *
 * <p>The specific commands and the operations they initiate could include
 * setting player status, clearing player status, retrieving player status,
 * amongst others. The actual command handling procedure will depend on the
 * implementation in the {@code onCommand()} method.
 *
 * @see CommandExecutor
 */
public class StatusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        try {
            player.sendMessage(String.format("Your status has been set to %s.", StatusPlugin.getInstance()
                    .setStatus(player, String.join(" ", args))));
        } catch (EventCancelledException eventCancelledException) {
            player.sendMessage("The status was not changed.");
        }
        return true;
    }
}