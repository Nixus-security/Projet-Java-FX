package com.ynov.escape.scene;

import com.ynov.escape.model.GameState;
import com.ynov.escape.service.SaveManager;
import com.ynov.escape.util.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuScene extends BaseScene {

    private final StackPane root;

    public MenuScene(SceneManager manager) {
        super(manager);

        Label title = new Label("Bienvenue dans l'Escape Game");
        title.getStyleClass().add("menu-title");

        Button newGame = new Button("Nouvelle Partie");
        newGame.getStyleClass().addAll("menu-button", "menu-button-new");
        newGame.setOnAction(e -> startNewGame());

        Button resume = new Button("Reprendre");
        resume.getStyleClass().addAll("menu-button", "menu-button-resume");
        resume.setDisable(!SaveManager.hasSave());
        resume.setOnAction(e -> resumeGame());

        Button quit = new Button("Quitter");
        quit.getStyleClass().addAll("menu-button", "menu-button-quit");
        quit.setOnAction(e -> {
            SaveManager.save(manager.state());
            System.exit(0);
        });

        VBox box = new VBox(20, title, newGame, resume, quit);
        box.setAlignment(Pos.CENTER);

        root = new StackPane(box);
        root.getStyleClass().add("menu-root");
    }

    private void startNewGame() {
        SaveManager.delete();
        manager.state().reset();
        manager.goToStep(manager.state().getStep());
    }

    private void resumeGame() {
        GameState saved = SaveManager.load();
        if (saved == null) {
            startNewGame();
            return;
        }
        manager.state().setStep(saved.getStep());
        manager.state().setDialogueIndex(saved.getDialogueIndex());
        manager.state().setQuizScore(saved.getQuizScore());
        manager.goToStep(saved.getStep());
    }

    @Override
    public StackPane getRoot() {
        return root;
    }
}
