package com.ynov.escape.scene;

import com.ynov.escape.model.Step;
import com.ynov.escape.util.Dialogues;
import com.ynov.escape.util.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

public class DialogueScene extends BaseScene {

    private final Step step;
    private final List<String> lines;

    private final StackPane root;
    private final Label speaker;
    private final Label text;
    private final Button next;

    private Timeline typing;
    private int charIndex;
    private int lineIndex;

    public DialogueScene(SceneManager manager, Step step) {
        super(manager);
        this.step = step;
        this.lines = Dialogues.LINES.get(step);
        this.lineIndex = manager.state().getDialogueIndex();
        if (lineIndex >= lines.size()) lineIndex = 0;

        speaker = new Label(step == Step.DEFEAT ? "" : "Chef");
        speaker.getStyleClass().add("speaker-name");

        text = new Label();
        text.setWrapText(true);
        text.getStyleClass().add("dialogue-text");

        VBox textBox = new VBox(10, speaker, text);
        textBox.getStyleClass().add("dialogue-box");
        textBox.setPadding(new Insets(20));

        ImageView portrait = new ImageView();
        try {
            String img = step == Step.VICTORY ? "bomb_defused.png"
                    : step == Step.DEFEAT ? "bomb_explosion.png"
                    : "chef.png";
            portrait.setImage(new Image(
                    getClass().getResourceAsStream("/com/ynov/escape/images/" + img)));
            portrait.setFitWidth(180);
            portrait.setFitHeight(180);
            portrait.setPreserveRatio(true);
        } catch (Exception ignored) {
        }

        HBox center = new HBox(20, textBox, portrait);
        center.setAlignment(Pos.CENTER);
        HBox.setHgrow(textBox, javafx.scene.layout.Priority.ALWAYS);
        center.setPadding(new Insets(40));

        next = new Button("Continuer (Espace)");
        next.getStyleClass().add("primary-button");
        next.setOnAction(e -> advance());

        HBox bottom = new HBox(next);
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.setPadding(new Insets(0, 40, 30, 0));

        BorderPane pane = new BorderPane();
        pane.setCenter(center);
        pane.setBottom(bottom);

        root = new StackPane(pane);
        root.getStyleClass().add("dialogue-root");
    }

    @Override
    public StackPane getRoot() {
        return root;
    }

    @Override
    public void onShow(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.SPACE) {
                advance();
                e.consume();
            }
        });
        startTyping();
    }

    private void startTyping() {
        if (typing != null) typing.stop();
        String line = lines.get(lineIndex);
        charIndex = 0;
        text.setText("");
        next.setDisable(true);

        typing = new Timeline(new KeyFrame(Duration.millis(25), e -> {
            if (charIndex < line.length()) {
                text.setText(line.substring(0, ++charIndex));
            } else {
                typing.stop();
                next.setDisable(false);
            }
        }));
        typing.setCycleCount(Timeline.INDEFINITE);
        typing.play();
    }

    private void advance() {
        if (typing != null && typing.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            typing.stop();
            text.setText(lines.get(lineIndex));
            next.setDisable(false);
            return;
        }

        lineIndex++;
        if (lineIndex >= lines.size()) {
            goToNextStep();
            return;
        }
        manager.state().setDialogueIndex(lineIndex);
        startTyping();
    }

    private void goToNextStep() {
        manager.state().setDialogueIndex(0);
        switch (step) {
            case INTRO_DIALOGUE -> manager.goToStep(Step.QUIZ);
            case MID_DIALOGUE_1 -> manager.goToStep(Step.MINESWEEPER);
            case MID_DIALOGUE_2 -> manager.goToStep(Step.MASTERMIND);
            case VICTORY, DEFEAT -> {
                com.ynov.escape.service.SaveManager.delete();
                manager.state().reset();
                manager.show(new MenuScene(manager));
            }
            default -> {}
        }
    }
}
