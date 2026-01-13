# pizza-app-java
Application Java de gestion de commandes de pizzas

#  Pizza App — Application JavaFX (MVC)

## Présentation
Pizza App est une application Java développée avec JavaFX et basée sur l’architecture MVC.
Elle permet la gestion complète d’un système de commandes de pizzas avec deux rôles :
**Client** et **Pizzaïolo**.

Le projet met l’accent sur :
- la logique métier
- la qualité du code
- la compréhension du fonctionnement global
- le respect des bonnes pratiques Java

---

##  Fonctionnalités

###  Côté Client
- Inscription avec validation des données
- Connexion / déconnexion
- Consultation des pizzas
- Filtrage des pizzas par type
- Création d’une commande
- Ajout de pizzas à une commande
- Validation d’une commande

###  Côté Pizzaïolo
- Création et gestion des ingrédients
- Création et gestion des pizzas
- Interdiction d’ingrédients par type
- Consultation des commandes clients
- Traitement des commandes
- Consultation de statistiques

### Synchronisation Client ↔ Pizzaïolo
- Les pizzas créées par le pizzaïolo sont visibles côté client
- Les commandes validées par les clients apparaissent côté pizzaïolo


## Architecture de l’application (MVC)

L’application est développée selon le pattern **MVC (Model – View – Controller)** afin
d’assurer une séparation claire des responsabilités et une meilleure maintenabilité.

###  Model (Logique métier)
Le package `pizzas` contient les classes métier de l’application, notamment :
- la gestion des pizzas
- les ingrédients
- les commandes
- les types et états métier

Ces classes encapsulent les règles métier et ne dépendent pas de l’interface graphique.

###  Controller
Le package `io` regroupe les classes responsables de la logique applicative et
de la communication entre l’interface utilisateur et le modèle.
Il inclut notamment la gestion des clients et du pizzaïolo.

###  View
Le package `ui` contient l’interface graphique développée avec **JavaFX** :
- fichiers FXML
- contrôleurs graphiques
- gestion des événements utilisateurs

###  Tests
Le package `tests` regroupe les tests unitaires permettant de valider le
bon fonctionnement des fonctionnalités principales.

###  Point d’entrée
La classe `MainPizzas` constitue le point d’entrée de l’application.
Elle initialise les composants métier et lance l’interface JavaFX.

##  Rôle des classes principales

### Client
- Email
- Mot de passe
- Informations personnelles
- Liste des commandes

### Pizza
- Nom
- Type : VIANDE, VEGETARIENNE, REGIONALE
- Ingrédients
- Prix minimal (calculé)
- Prix de vente (modifiable)

**Calcul du prix minimal :**
(somme des ingrédients × 1.4), arrondi à la dizaine supérieure

### Commande
- Client
- Pizzas
- État : CREE, VALIDEE, TRAITEE



## Règles métier

### Inscription
- Email obligatoire et unique
- Mot de passe ≥ 8 caractères
- Âge strictement positif

### Commande
- Impossible de modifier une commande validée
- Impossible de valider une commande vide

### Ingrédients
- Un ingrédient interdit ne peut pas être ajouté à une pizza
- Vérification possible après création

### Prix
- Prix de vente ≥ prix minimal
- Saisie numérique obligatoire

---

## Qualité du code
- Respect du pattern MVC
- Respect des interfaces
- Code lisible et structuré
- Absence de bugs bloquants

---

##  Documentation — Javadoc
Toutes les classes métiers, interfaces, méthodes publiques et exceptions
sont documentées avec Javadoc afin de faciliter la maintenance
et la compréhension du code.

---
## JavaFX

Ce projet utilise **JavaFX** pour l’interface graphique.

### Configuration
- Java JDK 11 ou supérieur
- JavaFX compatible avec la version du JDK

### Lancement
- Ouvrir le projet dans Eclipse
- Ajouter JavaFX au Build Path
- Lancer la classe `MainInterface`

---
##  Bonnes pratiques & Checkstyle
- Aucun System.out.println
- Méthodes de taille raisonnable
- Indentation correcte
- Noms explicites
- Accolades systématiques

---

##  Démonstration
1. Lancer l’application
2. Créer un ingrédient
3. Créer une pizza
4. Interdire un ingrédient
5. Côté client : afficher les pizzas
6. Inscription / connexion
7. Création et validation d’une commande
8. Côté pizzaïolo : traitement de la commande
