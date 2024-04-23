package com.example.sudoku;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SudokuGame extends Application {
    private static final int SIZE = 9;
    private static final int CELL_SIZE = 50;
    private Timeline timer;
    private int secondsPassed;

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setPadding(new Insets(25));
        root.setHgap(5);
        root.setVgap(5);
        root.setStyle("-fx-background-image: url('https://www.nastol.com.ua/large/202311/550246.jpg');" +
                "-fx-background-size: cover;");


        TextField[][] cells = new TextField[SIZE][SIZE];

        int[][] initialBoard = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                TextField cell = new TextField();
                cell.setPrefWidth(CELL_SIZE);
                cell.setPrefHeight(CELL_SIZE);
                cell.setStyle("-fx-alignment: center;-fx-font-size: 26px;");

                if ((i / 3 + j / 3) % 2 == 0) {
                    cell.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    cell.setBackground(new Background(new BackgroundFill(Color.rgb(255, 182, 193, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
                }

                if (initialBoard[i][j] != 0) {
                    cell.setText(String.valueOf(initialBoard[i][j]));
                    cell.setEditable(false);
                }
                cells[i][j] = cell;
                root.add(cell, j, i);
            }
        }

        Button checkButton = new Button("Проверить");
        Text resultText = new Text();

        VBox sideBox = new VBox(10, checkButton, resultText);
        sideBox.setPadding(new Insets(10));

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsPassed++;
            System.out.println("Прошло времени: " + secondsPassed + " сек.");
        }));
        timer.setCycleCount(Animation.INDEFINITE);

        checkButton.setOnAction(e -> {
            timer.stop();
            if (isSolutionValid(cells)) {
                resultText.setText("Верно! Затраченное время: " + secondsPassed + " сек.");
            } else {
                resultText.setText("Неверно!");
            }
        });


        HBox bottomBox = new HBox(10, sideBox);
        bottomBox.setPadding(new Insets(10, 0, 0, 0));

        root.add(bottomBox, SIZE + 1, 0);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sudoku");

        primaryStage.setOnShown(event -> timer.play());

        primaryStage.show();
    }

    private boolean isSolutionValid(TextField[][] cells) {
        int[][] board = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                String text = cells[i][j].getText();
                if (text.isEmpty()) {
                    return false;
                }
                int value = Integer.parseInt(text);
                if (value < 1 || value > 9) {
                    return false;
                }
                board[i][j] = value;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            boolean[] used = new boolean[SIZE + 1];
            for (int j = 0; j < SIZE; j++) {
                int num = board[i][j];
                if (used[num]) {
                    return false;
                }
                used[num] = true;
            }
        }

        for (int j = 0; j < SIZE; j++) {
            boolean[] used = new boolean[SIZE + 1];
            for (int i = 0; i < SIZE; i++) {
                int num = board[i][j];
                if (used[num]) {
                    return false;
                }
                used[num] = true;
            }
        }

        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                boolean[] used = new boolean[SIZE + 1];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int num = board[blockRow * 3 + i][blockCol * 3 + j];
                        if (used[num]) {
                            return false;
                        }
                        used[num] = true;
                    }
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


