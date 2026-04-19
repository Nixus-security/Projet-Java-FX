package com.ynov.escape.scene;

import com.ynov.escape.model.Step;
import com.ynov.escape.util.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MastermindScene extends BaseScene {

    private static final int SLOTS = 4;
    private static final int MAX_TRIES = 10;
    private static final Color[] PALETTE = {
            Color.CRIMSON, Color.FORESTGREEN, Color.ROYALBLUE,
            Color.GOLD, Color.DARKORCHID, Color.DARKORANGE
    };

    private final StackPane root;
    private final VBox history;
    private final HBox currentRow;
    private final Label triesLabel;
    private final Button validate;

    private int[] secret;
    private int triesLeft = MAX_TRIES;
    private final int[] current = new int[SLOTS];

    public MastermindScene(SceneManager manager) {
        super(manager);

        Label title = new Label("Mastermind — Désamorce la bombe");
        title.getStyleClass().add("mastermind-title");

        triesLabel = new Label("Tentatives restantes : " + MAX_TRIES);
        triesLabel.getStyleClass().add("mastermind-tries");

        history = new VBox(6);
        history.setAlignment(Pos.CENTER);

        currentRow = new HBox(10);
        currentRow.setAlignment(Pos.CENTER);
        for (int i = 0; i < SLOTS; i++) {
            current[i] = 0;
            currentRow.getChildren().add(makeSlot(i));
        }

        HBox palette = new HBox(8);
        palette.setAlignment(Pos.CENTER);
        for (int i = 0; i < PALETTE.length; i++) {
            final int color = i;
            Circle c = new Circle(15, PALETTE[i]);
            c.setOnMouseClicked(e -> pickColor(color));
            c.getStyleClass().add("palette-dot");
            palette.getChildren().add(c);
        }

        validate = new Button("Valider");
        validate.getStyleClass().add("primary-button");
        validate.setOnAction(e -> validateGuess());

        VBox box = new VBox(20, title, history, currentRow, palette, validate, triesLabel);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));

        root = new StackPane(box);
        root.getStyleClass().add("mastermind-root");

        generateSecret();
    }

    private Circle makeSlot(int index) {
        Circle circle = new Circle(18, Color.LIGHTGRAY);
        circle.getStyleClass().add("slot");
        circle.setOnMouseClicked(e -> selectedSlot = index);
        return circle;
    }

    private int selectedSlot = 0;

    private void pickColor(int colorIndex) {
        current[selectedSlot] = colorIndex + 1;
        ((Circle) currentRow.getChildren().get(selectedSlot)).setFill(PALETTE[colorIndex]);
        selectedSlot = (selectedSlot + 1) % SLOTS;
    }

    private void generateSecret() {
        Random rng = new Random();
        secret = new int[SLOTS];
        for (int i = 0; i < SLOTS; i++) secret[i] = rng.nextInt(PALETTE.length) + 1;
    }

    private void validateGuess() {
        for (int v : current) {
            if (v == 0) {
                triesLabel.setText("Choisis une couleur pour chaque emplacement.");
                return;
            }
        }

        int placed = 0;
        int colors = 0;
        boolean[] usedSecret = new boolean[SLOTS];
        boolean[] usedGuess = new boolean[SLOTS];

        for (int i = 0; i < SLOTS; i++) {
            if (current[i] == secret[i]) {
                placed++;
                usedSecret[i] = true;
                usedGuess[i] = true;
            }
        }
        for (int i = 0; i < SLOTS; i++) {
            if (usedGuess[i]) continue;
            for (int j = 0; j < SLOTS; j++) {
                if (!usedSecret[j] && current[i] == secret[j]) {
                    colors++;
                    usedSecret[j] = true;
                    break;
                }
            }
        }

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);
        for (int v : current) {
            Circle c = new Circle(14, PALETTE[v - 1]);
            row.getChildren().add(c);
        }
        Label hint = new Label("Bien placées : " + placed + "  |  Bonnes couleurs : " + colors);
        hint.getStyleClass().add("mastermind-hint");
        row.getChildren().add(hint);
        history.getChildren().add(row);

        if (placed == SLOTS) {
            triesLabel.setText("Bombe désamorcée !");
            validate.setDisable(true);
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(1.2));
            pause.setOnFinished(e -> manager.goToStep(Step.VICTORY));
            pause.play();
            return;
        }

        triesLeft--;
        triesLabel.setText("Tentatives restantes : " + triesLeft);

        if (triesLeft <= 0) {
            validate.setDisable(true);
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(1.2));
            pause.setOnFinished(e -> manager.goToStep(Step.DEFEAT));
            pause.play();
            return;
        }

        for (int i = 0; i < SLOTS; i++) {
            current[i] = 0;
            ((Circle) currentRow.getChildren().get(i)).setFill(Color.LIGHTGRAY);
        }
        selectedSlot = 0;
    }

    @Override
    public StackPane getRoot() {
        return root;
    }
}
