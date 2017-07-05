package fr.fam.melodyexporter;

/**
*
*/
public class ScrapException extends Exception {

    /** */
    private static final long serialVersionUID = 1L;

    /**
    *
    * @param message message
    * @param cause   cause
    */
    public ScrapException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
    *
    * @param message message
    */
    public ScrapException(final String message) {
        super(message);
    }
}
