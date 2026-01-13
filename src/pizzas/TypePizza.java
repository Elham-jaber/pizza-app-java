package pizzas;

import java.util.Set;

/**
 * Définit le type d'une pizza et les ingrédients interdits associés.
 */
public enum TypePizza {

    VIANDE(Set.of()),
    VEGETARIENNE(Set.of("jambon", "boeuf", "bacon")),
    REGIONALE(Set.of());

    private final Set<String> ingredientsInterdits;

    TypePizza(Set<String> ingredientsInterdits) {
        this.ingredientsInterdits = ingredientsInterdits;
    }

    /**
     * Vérifie si un ingrédient est autorisé pour ce type de pizza.
     *
     * @param ingredient ingrédient à tester
     * @return true si autorisé, false sinon
     */
    public boolean estIngredientAutorise(Ingredient ingredient) {
        if (ingredient == null) {
            return false;
        }
        return !ingredientsInterdits.contains(
                ingredient.getNom().toLowerCase());
    }
}


