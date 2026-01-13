package pizzas;

import java.time.LocalDateTime;

/**
 * Représente l'évaluation d'une pizza par un client.
 */
public class Evaluation {

    private final Client client;
    private final Pizza pizza;
    private final int note;
    private final String commentaire;
    private final LocalDateTime date;

    /**
     * Crée une évaluation.
     *
     * @param client client ayant évalué la pizza
     * @param pizza pizza évaluée
     * @param note note entre 0 et 5
     * @param commentaire commentaire optionnel
     */
    public Evaluation(Client client, Pizza pizza, int note, String commentaire) {
        if (client == null || pizza == null) {
            throw new IllegalArgumentException("Client ou pizza nul");
        }
        if (note < 0 || note > 5) {
            throw new IllegalArgumentException("Note invalide");
        }
        this.client = client;
        this.pizza = pizza;
        this.note = note;
        this.commentaire = commentaire;
        this.date = LocalDateTime.now();
    }

    public Client getClient() {
        return client;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public int getNote() {
        return note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
