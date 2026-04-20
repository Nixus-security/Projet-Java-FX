# Java Escape — Projet Final Java B1

**Auteur :** Cyril
**Formation :** YBOOST B1 — Ynov
**Module :** Java / JavaFX

## Présentation du projet

Ce projet est la réalisation du sujet « Java Escape » proposé dans le cadre du projet final de Java B1. Il s'agit d'un escape game de type Visual Novel développé en JavaFX, alternant séquences de dialogue interactives et mini-jeux, dans lequel le joueur incarne un agent chargé de localiser puis de désamorcer une bombe placée en ville.

## Fonctionnalités implémentées

Le sujet demandait la partie dialogues et **un** mini-jeu au choix. Pour ce projet, l'ensemble des fonctionnalités obligatoires a été implémenté, ainsi que **les trois mini-jeux** proposés (bonus).

| Fonctionnalité | État | Détails |
|---|---|---|
| Menu principal | Implémenté | Nouvelle partie, Reprendre, Quitter |
| Dialogues machine à écrire | Implémenté | Animation via `javafx.animation.Timeline`, passage rapide avec la barre espace |
| Jeu 1 — Quizz (bonus) | Implémenté | Appels HTTP à l'API publique opentdb, seuil de 5 bonnes réponses |
| Jeu 2 — Démineur (bonus) | Implémenté | Grille 8×8, 8 bombes, sécurisation du premier clic |
| Jeu 3 — Mastermind (bonus) | Implémenté | 4 couleurs parmi 6, 10 tentatives maximum |
| Fin Victoire / Défaite | Implémenté | Deux fins distinctes avec dialogues dédiés |
| Sauvegarde JSON (bonus) | Implémenté | Sauvegarde automatique à la fermeture via Gson |

## Choix techniques

**JavaFX pur, sans FXML.** L'ensemble des interfaces est construit programmatiquement, ce qui permet une meilleure lisibilité du flux d'exécution et évite la dépendance à un fichier de vue externe.

**Architecture par scènes.** Chaque étape du jeu est représentée par une classe `Scene` héritant d'une classe abstraite `BaseScene`. Un `SceneManager` centralise la navigation entre les scènes et transporte l'état global du jeu.

**Séparation des responsabilités.** Le code est organisé en quatre packages (`model`, `scene`, `service`, `util`) pour isoler la logique métier, l'affichage, les services externes et les utilitaires.

**Sauvegarde persistante.** L'état du jeu (étape courante, index de dialogue, score du quizz) est sérialisé en JSON à l'aide de la bibliothèque Gson, puis rechargé au démarrage si une sauvegarde existe.

**Appel d'API externe.** Le quizz utilise l'API Open Trivia DB via le client `java.net.http.HttpClient` introduit en Java 11, avec désérialisation JSON par Gson.

## Prérequis techniques

- Java 21 ou supérieur (testé avec Eclipse Temurin 21)
- Maven (le Maven Wrapper est fourni, aucune installation système n'est requise)

## Lancement du projet

Le projet utilise le plugin `javafx-maven-plugin` qui gère automatiquement les modules JavaFX. Depuis la racine du projet, exécuter :

```bash
./mvnw clean javafx:run
```

Sur Windows, la commande équivalente est :

```bash
mvnw.cmd clean javafx:run
```

Le premier lancement peut prendre une à deux minutes, le temps que Maven télécharge les dépendances (JavaFX 21, Gson 2.10.1).

## Arborescence du projet

```
javaescape/
├── pom.xml                          Configuration Maven et dépendances
├── mvnw                             Script Maven Wrapper
├── .mvn/wrapper/                    Propriétés du wrapper
└── src/main/
    ├── java/
    │   ├── module-info.java         Déclaration du module JavaFX
    │   └── com/ynov/escape/
    │       ├── Launcher.java        Point d'entrée de l'application
    │       ├── Main.java            Classe Application JavaFX
    │       ├── model/
    │       │   ├── GameState.java   État sérialisable du jeu
    │       │   └── Step.java        Énumération des étapes
    │       ├── scene/
    │       │   ├── BaseScene.java           Classe abstraite des scènes
    │       │   ├── MenuScene.java           Menu principal
    │       │   ├── DialogueScene.java       Scène de dialogue
    │       │   ├── QuizScene.java           Mini-jeu Quizz
    │       │   ├── MinesweeperScene.java    Mini-jeu Démineur
    │       │   └── MastermindScene.java     Mini-jeu Mastermind
    │       ├── service/
    │       │   ├── SaveManager.java  Sauvegarde et chargement JSON
    │       │   └── QuizApi.java      Client HTTP pour opentdb
    │       └── util/
    │           ├── SceneManager.java Gestion de la navigation
    │           └── Dialogues.java    Textes du personnage Chef
    └── resources/com/ynov/escape/
        ├── styles/main.css          Feuille de style globale
        └── images/                  Ressources graphiques
```

## Fonctionnement détaillé

**Dialogue machine à écrire.** La classe `DialogueScene` utilise une `Timeline` JavaFX qui incrémente l'affichage du texte caractère par caractère toutes les 25 millisecondes. La barre espace déclenche soit l'affichage instantané du texte en cours, soit le passage au dialogue suivant selon l'état.

**Flux de jeu.** Le `SceneManager` expose une méthode `goToStep(Step)` qui aiguille vers la bonne scène en fonction de l'étape courante. L'enchaînement suit l'ordre défini dans le sujet : dialogue d'introduction → quizz → dialogue intermédiaire → démineur → dialogue intermédiaire → mastermind → fin.

**Démineur.** La grille est générée après le premier clic du joueur, ce qui garantit que la première case cliquée ainsi que ses voisines ne contiennent pas de bombe. Cette mécanique, standard dans les démineurs modernes, évite une défaite aléatoire au premier clic.

**Mastermind.** À chaque tentative, le jeu calcule deux indices : le nombre de couleurs bien placées et le nombre de couleurs correctes mais mal placées. Le calcul utilise deux passes pour éviter de compter deux fois une même couleur.

**Sauvegarde.** Le fichier `save.json` est créé dans le dossier `~/.javaescape/` (sur Windows : `C:\Users\<user>\.javaescape\`). La sauvegarde est déclenchée par le handler `setOnCloseRequest` du `Stage` principal, ainsi que par le bouton Quitter du menu.

## Conclusion

Ce projet m'a permis de mettre en pratique plusieurs aspects du programme de Java B1 : programmation orientée objet, gestion d'événements, animations, appels HTTP asynchrones, sérialisation JSON, et architecture logicielle. La réalisation des trois mini-jeux en bonus a été l'occasion d'explorer des logiques de jeu variées (logique déductive pour le démineur, combinatoire pour le mastermind, intégration d'une API tierce pour le quizz).