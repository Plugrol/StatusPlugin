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

/**
 * An exception class indicating that an event within the game has been cancelled.
 *
 * <p>This class extends {@link RuntimeException}, meaning it's an unchecked
 * exception that can be thrown during the normal operation of the JVM.
 *
 * <p>Typically, this exception might be thrown when an event (such as {@code SetStatusEvent}
 * or {@code ClearStatusEvent}) is cancelled by any of the event listeners in the game.
 *
 * @see RuntimeException
 */
public class EventCancelledException extends RuntimeException {}
