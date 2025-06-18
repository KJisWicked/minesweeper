package minesweeper.controller;

import minesweeper.model.GameState;
import minesweeper.service.GameService;
import minesweeper.ui.UserInterface;

public class GameController {
    private final UserInterface userInterface;
    private final GameService gameService;

    public GameController(UserInterface userInterface, GameService gameService) {
        this.userInterface = userInterface;
        this.gameService = gameService;
    }

    public void startGame() {
        do {
            playSingleGame();
        } while (userInterface.renderPlayAgain());
    }

    private void playSingleGame() {
        userInterface.renderWelcomeMessage();
        int gridSize = userInterface.renderGetGridSize();
        int maxMines = gameService.getAllowedMaxMines(gridSize);
        int totalMines = userInterface.renderGetTotalMines(gridSize, maxMines);

        GameState gameState = gameService.initializeGame(gridSize, totalMines);

        loopGame(gameState);
    }

    private void loopGame(GameState gameState) {
        userInterface.renderGrid(gameState);

        while (!gameState.isGameOver()) {
            int[] coordinates = userInterface.renderGetCellSelection(gameState.getGrid().getSize());
            int row = coordinates[0];
            int column = coordinates[1];

            String result = gameService.processMove(gameState, row, column);
            userInterface.renderMessage(result);

            if (!gameState.isGameOver()) {
                userInterface.renderGrid(gameState);
            }
        }
    }
}
