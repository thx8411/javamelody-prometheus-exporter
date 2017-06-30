package fr.fam.melodyexporter;

/**
*
*/
public class ScrapExeption extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
    *
    * @param message message
    * @param cause   cause
    */
    public ScrapExeption(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
    *
    * @param message message
    */
    public ScrapExeption(final String message) {
        super(message);
    }
}
