package pizzas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Collection;
import java.io.File;
import java.io.Serializable;

/**
 * Implémentation des services du pizzaïolo.
 * <p>
 * Cette classe permet :
 * <ul>
 *   <li>la gestion des ingrédients</li>
 *   <li>la création et la gestion des pizzas</li>
 *   <li>le traitement des commandes</li>
 *   <li>le calcul des statistiques et bénéfices</li>
 * </ul>
 * </p>
 */
public class GestionPizzaiolo implements InterPizzaiolo, Serializable {

    /**
     * Identifiant de version pour la sérialisation.
     */
    private static final long serialVersionUID = 1L;

    /* =========================
       DONNÉES
       ========================= */

    /**
     * Ingrédients disponibles (clé = nom en minuscule).
     */
    private final Map<String, Ingredient> ingredients = new HashMap<>();

    /**
     * Ingrédients interdits par type de pizza.
     */
    private final Map<TypePizza, Set<String>> ingredientsInterdits = new HashMap<>();

    /**
     * Ensemble des pizzas créées.
     */
    private final Set<Pizza> pizzas = new HashSet<>();

    /**
     * Liste des commandes non encore traitées.
     */
    private final List<Commande> commandesNonTraitees = new ArrayList<>();

    /**
     * Liste des commandes déjà traitées.
     */
    private final List<Commande> commandesTraitees = new ArrayList<>();

    /**
     * Ensemble des clients enregistrés.
     */
    private final Set<Client> clients = new HashSet<>();

    /**
     * Référence transiente vers un gestionnaire pizzaiolo.
     */
    private transient GestionPizzaiolo gestionPizzaiolo;

    /**
     * Définit le gestionnaire pizzaiolo associé.
     *
     * @param gp gestionnaire pizzaiolo
     */
    public void setGestionPizzaiolo(GestionPizzaiolo gp) {
        this.gestionPizzaiolo = gp;
    }

    /* =========================
       CONSTRUCTEUR
       ========================= */

    /**
     * Construit un gestionnaire pizzaiolo et initialise
     * les listes d'ingrédients interdits pour chaque type de pizza.
     */
    public GestionPizzaiolo() {
        for (TypePizza t : TypePizza.values()) {
            ingredientsInterdits.put(t, new HashSet<>());
        }
    }

    /* =========================
       INGRÉDIENTS
       ========================= */

    /**
     * Crée un nouvel ingrédient.
     *
     * @param nom nom de l'ingrédient
     * @param prix prix de l'ingrédient
     * @return code de retour :
     * <ul>
     *   <li>0 : succès</li>
     *   <li>-1 : nom invalide</li>
     *   <li>-2 : ingrédient déjà existant</li>
     *   <li>-3 : prix invalide</li>
     * </ul>
     */
    @Override
    public int creerIngredient(String nom, double prix) {
        if (nom == null || nom.isBlank()) return -1;
        if (prix <= 0) return -3;

        String key = nom.toLowerCase();
        if (ingredients.containsKey(key)) return -2;

        ingredients.put(key, new Ingredient(nom, prix));
        return 0;
    }

    /**
     * Retourne la collection des ingrédients disponibles.
     *
     * @return collection d'ingrédients
     */
    public Collection<Ingredient> getIngredients() {
        return ingredients.values();
    }

    /**
     * Traite toutes les commandes non traitées.
     *
     * @return liste des commandes traitées
     */
    @Override
    public List<Commande> commandeNonTraitees() {

        List<Commande> result = new ArrayList<>(commandesNonTraitees);

        commandesTraitees.addAll(result);
        commandesNonTraitees.clear();

        result.forEach(c -> {
            try {
                c.traiter();
            } catch (Exception e) {
                // ignore
            }
        });

        return result;
    }

    /**
     * Change le prix d'un ingrédient.
     *
     * @param nom nom de l'ingrédient
     * @param prix nouveau prix
     * @return code de retour :
     * <ul>
     *   <li>0 : succès</li>
     *   <li>-1 : nom invalide</li>
     *   <li>-2 : prix invalide</li>
     *   <li>-3 : ingrédient inexistant</li>
     * </ul>
     */
    @Override
    public int changerPrixIngredient(String nom, double prix) {
        if (nom == null || nom.isBlank()) return -1;
        if (prix <= 0) return -2;

        Ingredient ing = ingredients.get(nom.toLowerCase());
        if (ing == null) return -3;

        ing.setPrix(prix);
        return 0;
    }

    /**
     * Interdit un ingrédient pour un type de pizza donné.
     *
     * @param nomIngredient nom de l'ingrédient
     * @param type type de pizza
     * @return {@code true} si l'interdiction est ajoutée, {@code false} sinon
     */
    @Override
    public boolean interdireIngredient(String nomIngredient, TypePizza type) {

        if (nomIngredient == null || nomIngredient.isBlank() || type == null) {
            return false;
        }

        Ingredient ing = ingredients.get(nomIngredient.toLowerCase());
        if (ing == null) {
            return false;
        }

        return ingredientsInterdits
                .get(type)
                .add(nomIngredient.toLowerCase());
    }

    /* =========================
       PIZZAS
       ========================= */

    /**
     * Crée une nouvelle pizza.
     *
     * @param nom nom de la pizza
     * @param type type de pizza
     * @return la pizza créée ou {@code null} en cas d'erreur
     */
    @Override
    public Pizza creerPizza(String nom, TypePizza type) {
        if (nom == null || nom.isBlank() || type == null) return null;

        for (Pizza p : pizzas) {
            if (p.getNom().equalsIgnoreCase(nom)) return null;
        }

        Pizza pizza = new Pizza(nom, type);
        pizzas.add(pizza);
        return pizza;
    }

    /**
     * Ajoute un ingrédient à une pizza.
     *
     * @param pizza pizza concernée
     * @param nomIngredient nom de l'ingrédient
     * @return code de retour selon le résultat
     */
    @Override
    public int ajouterIngredientPizza(Pizza pizza, String nomIngredient) {
        if (pizza == null || !pizzas.contains(pizza)) return -1;
        if (nomIngredient == null || nomIngredient.isBlank()) return -2;

        String key = nomIngredient.toLowerCase();
        Ingredient ing = ingredients.get(key);
        if (ing == null) return -2;

        if (ingredientsInterdits.get(pizza.getType()).contains(key)) {
            return -3;
        }

        boolean ajoute = pizza.ajouterIngredient(ing);
        return ajoute ? 0 : 1;
    }

    /**
     * Vérifie les ingrédients interdits présents dans une pizza.
     *
     * @param pizza pizza à vérifier
     * @return ensemble des ingrédients interdits
     */
    @Override
    public Set<String> verifierIngredientsPizza(Pizza pizza) {
        if (pizza == null || !pizzas.contains(pizza)) return null;

        return pizza.getIngredients().stream()
                .map(i -> i.getNom().toLowerCase())
                .filter(n -> ingredientsInterdits.get(pizza.getType()).contains(n))
                .collect(Collectors.toSet());
    }

    /**
     * Ajoute une photo à une pizza.
     *
     * @param pizza pizza concernée
     * @param file chemin du fichier
     * @return {@code true} si la photo est ajoutée
     */
    @Override
    public boolean ajouterPhoto(Pizza pizza, String file) {
        if (pizza == null || !pizzas.contains(pizza) || file == null) return false;

        File f = new File(file);
        if (!f.exists() || !file.matches(".*\\.(png|jpg|jpeg)$")) return false;

        pizza.setPhoto(file);
        return true;
    }

    /**
     * Retourne le prix de vente d'une pizza.
     *
     * @param pizza pizza concernée
     * @return prix de vente ou -1 en cas d'erreur
     */
    @Override
    public double getPrixPizza(Pizza pizza) {
        if (pizza == null || !pizzas.contains(pizza)) return -1;
        return pizza.getPrixVente();
    }

    /**
     * Définit le prix de vente d'une pizza.
     *
     * @param pizza pizza concernée
     * @param prix nouveau prix
     * @return {@code true} si le prix est modifié
     */
    @Override
    public boolean setPrixPizza(Pizza pizza, double prix) {

        if (pizza == null || !pizzas.contains(pizza)) {
            return false;
        }

        if (prix <= 0) {
            return false;
        }

        if (prix < pizza.getPrixMinimal()) {
            return false;
        }

        pizza.setPrixVente(prix);
        return true;
    }

    /**
     * Retire un ingrédient d'une pizza.
     *
     * @param pizza pizza concernée
     * @param nomIngredient nom de l'ingrédient
     * @return code de retour selon le résultat
     */
    @Override
    public int retirerIngredientPizza(Pizza pizza, String nomIngredient) {

        if (pizza == null || nomIngredient == null || nomIngredient.isBlank()) {
            return -1;
        }

        Ingredient i = ingredients.get(nomIngredient.toLowerCase());
        if (i == null) {
            return -2;
        }

        boolean removed = pizza.retirerIngredient(i);
        return removed ? 0 : -3;
    }

    /**
     * Calcule le prix minimal d'une pizza.
     *
     * @param pizza pizza concernée
     * @return prix minimal ou -1 en cas d'erreur
     */
    @Override
    public double calculerPrixMinimalPizza(Pizza pizza) {
        if (pizza == null || !pizzas.contains(pizza)) return -1;
        return pizza.getPrixMinimal();
    }

    /**
     * Retourne l'ensemble des pizzas.
     *
     * @return ensemble non modifiable de pizzas
     */
    @Override
    public Set<Pizza> getPizzas() {
        return Collections.unmodifiableSet(pizzas);
    }

    /* =========================
       CLIENTS & COMMANDES
       ========================= */

    /**
     * Enregistre un client.
     *
     * @param c client à enregistrer
     */
    public void enregistrerClient(Client c) {
        if (c != null) clients.add(c);
    }

    /**
     * Enregistre une commande validée.
     *
     * @param c commande à enregistrer
     */
    public void enregistrerCommande(Commande c) {
        if (c != null && c.getEtat() == EtatCommande.VALIDEE) {
            commandesNonTraitees.add(c);
        }
    }

    /**
     * Retourne les commandes déjà traitées.
     *
     * @return liste des commandes traitées
     */
    @Override
    public List<Commande> commandesDejaTraitees() {
        return commandesTraitees.stream()
                .sorted(Comparator.comparing(Commande::getDateCreation))
                .collect(Collectors.toList());
    }

    /**
     * Retourne les commandes traitées d'un client.
     *
     * @param client informations personnelles du client
     * @return liste des commandes
     */
    @Override
    public List<Commande> commandesTraiteesClient(InformationPersonnelle client) {
        if (client == null) return null;

        return commandesTraitees.stream()
                .filter(c -> c.getClient().getInfos().equals(client))
                .sorted(Comparator.comparing(Commande::getDateCreation))
                .collect(Collectors.toList());
    }

    /* =========================
       STATISTIQUES
       ========================= */

    /**
     * Calcule le bénéfice par pizza.
     *
     * @return map pizza → bénéfice
     */
    @Override
    public Map<Pizza, Double> beneficeParPizza() {
        Map<Pizza, Double> map = new HashMap<>();
        for (Pizza p : pizzas) {
            map.put(p, p.getPrixVente() - p.getPrixMinimal());
        }
        return map;
    }

    /**
     * Calcule le bénéfice d'une commande.
     *
     * @param commande commande concernée
     * @return bénéfice ou -1 si invalide
     */
    @Override
    public double beneficeCommandes(Commande commande) {
        if (commande == null || commande.getEtat() != EtatCommande.TRAITEE) return -1;
        return commande.getBenefice();
    }

    /**
     * Calcule le bénéfice total de toutes les commandes.
     *
     * @return bénéfice total
     */
    @Override
    public double beneficeToutesCommandes() {
        return commandesTraitees.stream()
                .mapToDouble(Commande::getBenefice)
                .sum();
    }

    /**
     * Calcule le nombre de pizzas commandées par client.
     *
     * @return map client → nombre de pizzas
     */
    @Override
    public Map<InformationPersonnelle, Integer> nombrePizzasCommandeesParClient() {
        Map<InformationPersonnelle, Integer> map = new HashMap<>();

        for (Commande c : commandesTraitees) {
            InformationPersonnelle info = c.getClient().getInfos();
            map.put(info, map.getOrDefault(info, 0) + c.getPizzas().size());
        }
        return map;
    }

    /**
     * Calcule le bénéfice par client.
     *
     * @return map client → bénéfice
     */
    @Override
    public Map<InformationPersonnelle, Double> beneficeParClient() {
        Map<InformationPersonnelle, Double> map = new HashMap<>();

        for (Commande c : commandesTraitees) {
            InformationPersonnelle info = c.getClient().getInfos();
            map.put(info,
                    map.getOrDefault(info, 0.0) + c.getBenefice());
        }
        return map;
    }

    /**
     * Calcule le nombre de fois qu'une pizza a été commandée.
     *
     * @param pizza pizza concernée
     * @return nombre de commandes ou -1 si invalide
     */
    @Override
    public int nombrePizzasCommandees(Pizza pizza) {
        if (pizza == null || !pizzas.contains(pizza)) return -1;

        int count = 0;
        for (Commande c : commandesTraitees) {
            for (Pizza p : c.getPizzas()) {
                if (p.equals(pizza)) count++;
            }
        }
        return count;
    }

    /**
     * Classe les pizzas par nombre de commandes.
     *
     * @return liste triée des pizzas
     */
    @Override
    public List<Pizza> classementPizzasParNombreCommandes() {
        return pizzas.stream()
                .sorted((p1, p2) ->
                        Integer.compare(
                                nombrePizzasCommandees(p2),
                                nombrePizzasCommandees(p1)))
                .collect(Collectors.toList());
    }

    /**
     * Retourne l'ensemble des clients.
     *
     * @return ensemble des informations clients
     */
    @Override
    public Set<InformationPersonnelle> ensembleClients() {
        return clients.stream()
                .map(Client::getInfos)
                .collect(Collectors.toSet());
    }

    /* =========================
       UTILITAIRE
       ========================= */

    /**
     * Retourne les noms des ingrédients disponibles.
     *
     * @return ensemble des noms d'ingrédients
     */
    public Set<String> getNomsIngredients() {
        return ingredients.values().stream()
                .map(Ingredient::getNom)
                .collect(Collectors.toSet());
    }
}
