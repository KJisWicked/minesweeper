package minesweeper.model;

public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private int adjacentMineCount;
    private final int row;
    private final int column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.isMine = false;
        this.isRevealed = false;
        this.adjacentMineCount = 0;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public int getAdjacentMineCount() {
        return adjacentMineCount;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setMine(boolean mine) {
        this.isMine = mine;
    }

    public void setRevealed(boolean revealed) {
        this.isRevealed = revealed;
    }

    public void setAdjacentMineCount(int count) {
        this.adjacentMineCount = count;
    }

    public String renderValue() {
        if (!isRevealed) {
            return "_";
        }

        return isMine ? "*" : String.valueOf(adjacentMineCount);
    }
}
