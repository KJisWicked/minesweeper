package minesweeper;

import minesweeper.controller.GameController;
import minesweeper.service.GameService;
import minesweeper.service.GridService;
import minesweeper.service.MineService;
import minesweeper.ui.ConsoleUserInterface;
import minesweeper.util.InputValidator;

public class MineSweeper {
    public static void main(String[] args) {

        // All Dependency Injection
        InputValidator inputValidator = new InputValidator();
        ConsoleUserInterface userInterface = new ConsoleUserInterface(inputValidator);

        GridService gridService = new GridService();
        MineService mineService = new MineService();
        GameService gameService = new GameService(gridService, mineService);

        GameController gameController = new GameController(userInterface, gameService);

        // Start Game
        gameController.startGame();
    }
}
