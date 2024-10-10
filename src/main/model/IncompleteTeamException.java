package model;


/**
 * Represents an exception thrown when attempting to add an incomplete team to the repository.
 */
public class IncompleteTeamException extends Exception {

    /**
     * EFFECTS: Constructs a new IncompleteTeamException with the specified detail message.
     */
    public IncompleteTeamException(String message) {
        super(message);
    }
}
