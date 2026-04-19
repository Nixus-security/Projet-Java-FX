package com.ynov.escape.scene;

import com.ynov.escape.model.Step;
import com.ynov.escape.util.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.Random;

public class MinesweeperScene extends BaseScene {

    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int BOMBS = 8;

    private final StackPane root;
    private final GridPane grid;
    private final Label status;
    private Cell[][] cells;
    private int revealed;
    private boolean gameOver;
    private boolean firstClick;

    public MinesweeperScene(SceneManager manager) {
        super(manager);

        status = new Label("Clic : révéler | Ctrl+clic : drapeau");
        status.getStyleClass().add("minesweeper-status");

        Button reset = new Button("Recommencer");
        reset.getStyleClass().add("primary-button");
        reset.setOnAction(e -> build());

        HBox top = new HBox(20, status, reset);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(20));

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(3);
        grid.setVgap(3);

        BorderPane pane = new BorderPane();
        pane.setTop(top);
        pane.setCenter(grid);

        root = new StackPane(pane);
        root.getStyleClass().add("minesweeper-root");

        build();
    }

    private void build() {
        grid.getChildren().clear();
        cells = new Cell[ROWS][COLS];
        revealed = 0;
        gameOver = false;
        firstClick = true;
        status.setText("Trouve toutes les cases sans bombe !");

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                cells[r][c] = new Cell();
                final int row = r, col = c;
                cells[r][c].button.setOnMouseClicked(e -> {
                    if (gameOver) return;
                    boolean rightClick = e.getButton() == MouseButton.SECONDARY
                            || (e.getButton() == MouseButton.PRIMARY && e.isControlDown());
                    if (rightClick) toggleFlag(row, col);
                    else if (e.getButton() == MouseButton.PRIMARY) reveal(row, col);
                });
                grid.add(cells[r][c].button, c, r);
            }
        }
    }

    private void placeBombs(int safeRow, int safeCol) {
        Random rng = new Random();
        int placed = 0;
        while (placed < BOMBS) {
            int r = rng.nextInt(ROWS);
            int c = rng.nextInt(COLS);
            if (cells[r][c].bomb) continue;
            if (Math.abs(r - safeRow) <= 1 && Math.abs(c - safeCol) <= 1) continue;
            cells[r][c].bomb = true;
            placed++;
        }

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (cells[r][c].bomb) continue;
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr, nc = c + dc;
                        if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && cells[nr][nc].bomb) count++;
                    }
                }
                cells[r][c].neighbors = count;
            }
        }
    }

    private void reveal(int r, int c) {
        Cell cell = cells[r][c];
        if (cell.revealed || cell.flagged) return;

        if (firstClick) {
            placeBombs(r, c);
            firstClick = false;
        }

        if (cell.bomb) {
            cell.button.setText("💣");
            cell.button.getStyleClass().add("cell-bomb");
            status.setText("Bombe ! Clique sur Recommencer.");
            gameOver = true;
            revealAllBombs();
            return;
        }

        floodReveal(r, c);

        if (revealed == ROWS * COLS - BOMBS) {
            status.setText("Bravo ! Tu as localisé la bombe.");
            gameOver = true;
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(1.2));
            pause.setOnFinished(e -> manager.goToStep(Step.MID_DIALOGUE_2));
            pause.play();
        }
    }

    private void floodReveal(int r, int c) {
        if (r < 0 || r >= ROWS || c < 0 || c >= COLS) return;
        Cell cell = cells[r][c];
        if (cell.revealed || cell.flagged || cell.bomb) return;

        cell.revealed = true;
        revealed++;
        cell.button.getStyleClass().add("cell-revealed");

        if (cell.neighbors > 0) {
            cell.button.setText(String.valueOf(cell.neighbors));
            cell.button.getStyleClass().add("cell-n" + cell.neighbors);
            return;
        }

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                floodReveal(r + dr, c + dc);
            }
        }
    }

    private void toggleFlag(int r, int c) {
        Cell cell = cells[r][c];
        if (cell.revealed) return;
        cell.flagged = !cell.flagged;
        cell.button.setText(cell.flagged ? "🚩" : "");
    }

    private void revealAllBombs() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (cells[r][c].bomb) {
                    cells[r][c].button.setText("💣");
                    cells[r][c].button.getStyleClass().add("cell-bomb");
                }
            }
        }
    }

    @Override
    public StackPane getRoot() {
        return root;
    }

    private static class Cell {
        Button button = new Button();
        boolean bomb;
        boolean revealed;
        boolean flagged;
        int neighbors;

        Cell() {
            button.setPrefSize(48, 48);
            button.getStyleClass().add("cell");
        }
    }
}
