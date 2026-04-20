# Java Escape

Escape game Visual Novel en JavaFX. Projet final Java B1 (Ynov).

## Contenu

- Menu principal (Nouvelle Partie / Reprendre / Quitter)
- Dialogues avec effet machine à écrire (Timeline + barre espace pour accélérer)
- Quizz (API opentdb, 5 bonnes réponses pour passer)
- Démineur 10x10, 15 bombes
- Mastermind, 4 couleurs parmi 6, 10 tentatives
- Deux fins (victoire / défaite)
- Sauvegarde automatique JSON (Gson) à la fermeture, dans `~/.javaescape/save.json`

## Prérequis

- JDK 21+ (testé sur Temurin 21)
- Maven 3.8+

## Lancer le projet

Depuis le dossier racine :

```bash
mvn clean javafx:run
```

Dans IntelliJ IDEA :

1. Ouvrir le dossier `javaescape` (détection auto du `pom.xml`)
2. Attendre l'import Maven
3. Onglet Maven → Plugins → javafx → `javafx:run`

Ou configurer un run sur `com.ynov.escape.Launcher` (le launcher non-JavaFX contourne le souci de lancement direct d'`Application` sur certains environnements).

## Structure

```
src/main/java/com/ynov/escape/
  Launcher.java         point d'entrée
  Main.java             Application JavaFX
  model/
    GameState.java      état sauvegardable
    Step.java           enum des étapes
  scene/
    BaseScene.java
    MenuScene.java
    DialogueScene.java
    QuizScene.java
    MinesweeperScene.java
    MastermindScene.java
  service/
    SaveManager.java    Gson save/load
    QuizApi.java        appels opentdb
  util/
    SceneManager.java   navigation entre scènes
    Dialogues.java      textes du Chef
```

## Images

Trois placeholders sont fournis dans `resources/com/ynov/escape/images/` :
`chef.png`, `bomb_defused.png`, `bomb_explosion.png`.
Tu peux les remplacer par les vraies images quand tu veux, en gardant les mêmes noms.

## Sauvegarde

La sauvegarde est faite automatiquement quand on quitte le jeu (fermeture fenêtre ou bouton Quitter). Fichier : `~/.javaescape/save.json` (sur Windows : `C:\Users\antho\.javaescape\save.json`).

Pour supprimer la sauvegarde : cliquer sur "Nouvelle Partie" depuis le menu.
