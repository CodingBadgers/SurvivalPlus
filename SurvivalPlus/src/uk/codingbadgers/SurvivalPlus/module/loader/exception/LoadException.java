package uk.codingbadgers.SurvivalPlus.module.loader.exception;

/**
 * Created by James on 05/05/2014.
 */
public class LoadException extends Exception {
    public LoadException(String message) {
        super(message);
    }

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadException(Throwable cause) {
        super(cause);
    }
}
