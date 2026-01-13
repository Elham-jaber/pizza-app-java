package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une pizza en vente.
 */
public class Pizza  implements Serializable {

    private final String nom;
    private final TypePizza type;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<Evaluation> evaluations = new ArrayList<>();
    private double prixVente;
    private String photo;

    public Pizza(String nom, TypePizza type) {
        this.nom = nom;
        this.type = type;
        this.prixVente = getPrixMinimal();
    }


    public String getNom() {
        return nom;
    }

    public TypePizza getType() {
        return type;
    }

    public List<Ingredient> getIngredients() {
        return List.copyOf(ingredients);
    }

    public List<Evaluation> getEvaluations() {
        return List.copyOf(evaluations);
    }

    public double getPrixVente() {
        return prixVente == 0 ? getPrixMinimal() : prixVente;
    }

    public String getPhoto() {
        return photo;
    }
    public boolean retirerIngredient(Ingredient i) {
        if (i == null) return false;
        return ingredients.remove(i);
    }

    public boolean ajouterIngredient(Ingredient i) {
        if (i == null) return false;

        if (ingredients.contains(i)) {
            return false;
        }

        ingredients.add(i);
        return true;
    }



    public double getPrixMinimal() {
        double somme = ingredients.stream()
                .mapToDouble(Ingredient::getPrix)
                .sum();

        double prix = somme * 1.4;
        return Math.ceil(prix * 10) / 10.0;
    }

    public void setPrixVente(double prixVente) {
        if (prixVente < getPrixMinimal()) {
            throw new IllegalArgumentException("Prix inférieur au prix minimal");
        }
        this.prixVente = prixVente;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void ajouterEvaluation(Evaluation e) {
        if (e != null) {
            evaluations.add(e);
        }
    }
}
