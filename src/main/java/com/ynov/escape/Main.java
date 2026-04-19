package com.ynov.escape;

import com.ynov.escape.model.GameState;
import com.ynov.escape.scene.MenuScene;
import com.ynov.escape.service.SaveManager;
import com.ynov.escape.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private GameState state;

    @Override
    public void start(Stage stage) {
        state = new GameState();

        stage.setTitle("Java Escape");
        stage.setMinWidth(960);
        stage.setMinHeight(640);

        SceneManager sceneManager = new SceneManager(stage, state);
        sceneManager.show(new MenuScene(sceneManager));

        stage.setOnCloseRequest(e -> SaveManager.save(state));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
