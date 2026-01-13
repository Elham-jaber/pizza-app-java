package pizzas;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pizzaiolo implements InterPizzaiolo {
// les fonctions de cette classe sont traitées dans la classe GestionPizzaiolo 
  @Override
  public int creerIngredient(String nom, double prix) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int changerPrixIngredient(String nom, double prix) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean interdireIngredient(String nomIngredient, TypePizza type) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Pizza creerPizza(String nom, TypePizza type) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int ajouterIngredientPizza(Pizza pizza, String nomIngredient) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int retirerIngredientPizza(Pizza pizza, String nomIngredient) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Set<String> verifierIngredientsPizza(Pizza pizza) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean ajouterPhoto(Pizza pizza, String file) throws IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public double getPrixPizza(Pizza pizza) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean setPrixPizza(Pizza pizza, double prix) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public double calculerPrixMinimalPizza(Pizza pizza) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Set<Pizza> getPizzas() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<InformationPersonnelle> ensembleClients() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Commande> commandesDejaTraitees() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Commande> commandeNonTraitees() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Commande> commandesTraiteesClient(InformationPersonnelle client) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Pizza, Double> beneficeParPizza() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double beneficeCommandes(Commande commande) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double beneficeToutesCommandes() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Map<InformationPersonnelle, Integer> nombrePizzasCommandeesParClient() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<InformationPersonnelle, Double> beneficeParClient() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int nombrePizzasCommandees(Pizza pizza) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public List<Pizza> classementPizzasParNombreCommandes() {
    // TODO Auto-generated method stub
    return null;
  }

}
