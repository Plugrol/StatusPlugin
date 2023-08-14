package net.groldi.statusplugin;

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
