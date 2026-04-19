package com.ynov.escape.util;

import com.ynov.escape.model.GameState;
import com.ynov.escape.model.Step;
import com.ynov.escape.scene.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SceneManager {

    private final Stage stage;
    private final GameState state;

    public SceneManager(Stage stage, GameState state) {
        this.stage = stage;
        this.state = state;
    }

    public GameState state() {
        return state;
    }

    public void show(BaseScene view) {
        Scene scene = new Scene(view.getRoot(), 1024, 680);
        scene.getStylesheets().add(
                getClass().getResource("/com/ynov/escape/styles/main.css").toExternalForm()
        );
        view.onShow(scene);
        stage.setScene(scene);
    }

    public void goToStep(Step step) {
        state.setStep(step);
        switch (step) {
            case INTRO_DIALOGUE, MID_DIALOGUE_1, MID_DIALOGUE_2, VICTORY, DEFEAT ->
                    show(new DialogueScene(this, step));
            case QUIZ -> show(new QuizScene(this));
            case MINESWEEPER -> show(new MinesweeperScene(this));
            case MASTERMIND -> show(new MastermindScene(this));
        }
    }
}
