package pizzas;

/**
 * Exception levée quand il y a un problème avec la commande d'un client.
 */
public class CommandeException extends Exception {

    private static final long serialVersionUID = -2876441299971092712L;

    public CommandeException() {
        super();
    }

    public CommandeException(String message) {
        super(message);
    }

    public CommandeException(String message, Throwable cause) {
        super(message, cause);
    }
}
