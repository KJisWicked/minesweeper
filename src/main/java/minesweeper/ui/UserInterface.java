package minesweeper.ui;

import minesweeper.model.GameState;

public interface UserInterface {
    void renderWelcomeMessage();

    int renderGetGridSize();

    int renderGetTotalMines(int gridSize, int maxMines);

    int[] renderGetCellSelection(int gridSize);

    void renderGrid(GameState gameState);

    void renderMessage(String message);

    boolean renderPlayAgain();
}
