package ru.abbysoft.wisebuild.exception;

/**
 * This exception indicates that slot being used
 * has some limit that was violated.
 *
 * @author apopov
 */
public class SlotLimitException extends Exception {

    /**
     * Creates new exception with message
     *
     * @param message cause of exception
     */
    public SlotLimitException(String message) {
        super(message);
    }
}
