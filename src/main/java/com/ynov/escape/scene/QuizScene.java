package com.ynov.escape.scene;

import com.ynov.escape.model.Step;
import com.ynov.escape.service.QuizApi;
import com.ynov.escape.util.SceneManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class QuizScene extends BaseScene {

    private static final int GOAL = 5;

    private final StackPane root;
    private final VBox content;
    private final Label score;
    private final Label questionLabel;
    private final HBox answersBox;
    private final Label feedback;
    private final Button nextButton;

    private List<QuizApi.Question> questions;
    private int currentIndex = 0;
    private int correctCount;

    public QuizScene(SceneManager manager) {
        super(manager);
        correctCount = manager.state().getQuizScore();

        score = new Label(correctCount + "/" + GOAL);
        score.getStyleClass().add("quiz-score");

        HBox top = new HBox(score);
        top.setAlignment(Pos.CENTER_RIGHT);
        top.setPadding(new Insets(20));

        questionLabel = new Label("Chargement des questions...");
        questionLabel.setWrapText(true);
        questionLabel.getStyleClass().add("quiz-question");

        answersBox = new HBox(12);
        answersBox.setAlignment(Pos.CENTER);

        feedback = new Label();
        feedback.getStyleClass().add("quiz-feedback");

        nextButton = new Button("Suivant");
        nextButton.getStyleClass().add("primary-button");
        nextButton.setDisable(true);
        nextButton.setOnAction(e -> showNextQuestion());

        content = new VBox(30, top, questionLabel, answersBox, feedback, nextButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        ProgressIndicator loader = new ProgressIndicator();
        loader.setMaxSize(80, 80);
        root = new StackPane(loader);
        root.getStyleClass().add("quiz-root");

        loadQuestions();
    }

    private void loadQuestions() {
        Task<List<QuizApi.Question>> task = new Task<>() {
            @Override
            protected List<QuizApi.Question> call() throws Exception {
                return QuizApi.fetch();
            }
        };
        task.setOnSucceeded(e -> {
            questions = task.getValue();
            root.getChildren().setAll(content);
            showNextQuestion();
        });
        task.setOnFailed(e -> {
            Label error = new Label("Impossible de contacter l'API. Vérifie ta connexion.");
            error.getStyleClass().add("quiz-feedback");
            Button retry = new Button("Réessayer");
            retry.getStyleClass().add("primary-button");
            retry.setOnAction(ev -> {
                root.getChildren().setAll(new ProgressIndicator());
                loadQuestions();
            });
            VBox box = new VBox(20, error, retry);
            box.setAlignment(Pos.CENTER);
            root.getChildren().setAll(box);
        });
        new Thread(task, "quiz-loader").start();
    }

    private void showNextQuestion() {
        if (correctCount >= GOAL) {
            manager.goToStep(Step.MID_DIALOGUE_1);
            return;
        }

        if (currentIndex >= questions.size()) {
            Task<List<QuizApi.Question>> task = new Task<>() {
                @Override
                protected List<QuizApi.Question> call() throws Exception {
                    return QuizApi.fetch();
                }
            };
            task.setOnSucceeded(e -> {
                questions = task.getValue();
                currentIndex = 0;
                showNextQuestion();
            });
            new Thread(task, "quiz-reload").start();
            return;
        }

        QuizApi.Question q = questions.get(currentIndex++);
        questionLabel.setText(q.question);
        feedback.setText("");
        nextButton.setDisable(true);
        answersBox.getChildren().clear();

        String[] colors = {"answer-green", "answer-blue", "answer-red", "answer-orange"};
        for (int i = 0; i < q.answers.size(); i++) {
            String answer = q.answers.get(i);
            Button btn = new Button(answer);
            btn.getStyleClass().addAll("answer-button", colors[i % colors.length]);
            btn.setOnAction(e -> handleAnswer(answer, q.correctAnswer));
            answersBox.getChildren().add(btn);
        }
    }

    private void handleAnswer(String picked, String correct) {
        for (var n : answersBox.getChildren()) {
            n.setDisable(true);
        }
        if (picked.equals(correct)) {
            correctCount++;
            manager.state().setQuizScore(correctCount);
            score.setText(correctCount + "/" + GOAL);
            feedback.setText("Correct !");
            feedback.setStyle("-fx-text-fill: #2e8b57;");
        } else {
            feedback.setText("Incorrect ! La bonne réponse était : " + correct);
            feedback.setStyle("-fx-text-fill: #c0392b;");
        }
        nextButton.setDisable(false);
    }

    @Override
    public StackPane getRoot() {
        return root;
    }
}
