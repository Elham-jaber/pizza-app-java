package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente un client de l'application.
 */
public class Client  implements Serializable {

    private final String email;
    private String mdp;
    private InformationPersonnelle infos;
    private final List<Commande> commandes = new ArrayList<>();

    public Client(String email, String mdp, InformationPersonnelle infos) {
        if (email == null || email.isBlank() || mdp == null) {
            throw new IllegalArgumentException("Email ou mot de passe invalide");
        }
        this.email = email;
        this.mdp = mdp;
        this.infos = infos;
    }

    public Commande creerCommande() {
        Commande c = new Commande(this);
        commandes.add(c);
        return c;
    }

    public void validerCommande(Commande c) throws CommandeException {
        if (!commandes.contains(c)) {
            throw new CommandeException("Commande inconnue");
        }
        c.valider();
    }

    public void annulerCommande(Commande c) throws CommandeException {
        if (!commandes.contains(c)) {
            throw new CommandeException("Commande inconnue");
        }
        if (c.getEtat() != EtatCommande.CREE) {
            throw new CommandeException("Commande non annulable");
        }
        commandes.remove(c);
    }

    public void evaluerPizza(Pizza p, int note, String commentaire) {
        boolean aCommande = commandes.stream()
            .filter(c -> c.getEtat() == EtatCommande.TRAITEE)
            .flatMap(c -> c.getPizzas().stream())
            .anyMatch(pizza -> pizza.equals(p));

        if (!aCommande) {
            throw new IllegalStateException("Pizza non commandée");
        }

        Evaluation e = new Evaluation(this, p, note, commentaire);
        p.ajouterEvaluation(e);
    }

    public String getEmail() {
        return email;
    }

    public InformationPersonnelle getInfos() {
        return infos;
    }
    public String getMdp() {
        return mdp;
    }

    public List<Commande> getCommandes() {
        return Collections.unmodifiableList(commandes);
    }

    @Override
    public String toString() {
        return "Client[email=" + email + ", infos=" + infos + "]";
    }
}
