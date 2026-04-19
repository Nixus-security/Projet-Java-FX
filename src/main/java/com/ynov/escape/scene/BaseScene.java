package com.ynov.escape.scene;

import com.ynov.escape.util.SceneManager;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public abstract class BaseScene {

    protected final SceneManager manager;

    protected BaseScene(SceneManager manager) {
        this.manager = manager;
    }

    public abstract Pane getRoot();

    public void onShow(Scene scene) {
    }
}
