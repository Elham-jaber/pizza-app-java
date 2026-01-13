package pizzas;

import java.io.Serializable;

/**
 * Représente un ingrédient utilisé dans une pizza.
 */
public class Ingredient  implements Serializable  {

    private String nom;
    private double prix;

    /**
     * Crée un ingrédient avec un nom et un prix.
     *
     * @param nom nom de l'ingrédient (non nul)
     * @param prix prix de l'ingrédient (>= 0)
     */
    public Ingredient(String nom, double prix) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Nom invalide");
        }
        this.nom = nom;
        this.prix = Math.max(0, prix);
    }

    public String getNom() {
        return nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setNom(String nom) {
        if (nom != null && !nom.isBlank()) {
            this.nom = nom;
        }
    }

    public void setPrix(double prix) {
        if (prix >= 0) {
            this.prix = prix;
        }
    }
}
