package pizzas;

/**
 * Exception levée quand aucun utilisateur n'est connecté.
 */
public class NonConnecteException extends Exception {

    private static final long serialVersionUID = -2876441299971092712L;

    public NonConnecteException() {
        super("Aucun utilisateur n'est connecté");
    }

    public NonConnecteException(String message) {
        super(message);
    }
}
