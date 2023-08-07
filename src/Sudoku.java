import javax.swing.*;

public class Sudoku {
    //Array to keep track of Sudoku board.
    int[][] board;
    //Number of Rows & Columns of board (To be specified by user?)
    int N;
    //Square root of N.
    int SRN;
    //Number of empty cells on final board.
    int K;

    // Constructor
    Sudoku(int N, int K) {
        this.N = N;
        this.K = K;
        double SRNd = Math.sqrt(N);
        SRN = (int) SRNd;

        board = new int[N][N];
    }

    //Method to generate random numbers to fill cells with
    int randomGenerator(int num) {
        return (int) Math.floor((Math.random()*num+1));
    }

    //FileMethods to check that a number is allowed in a cell based on Box, Row & Column
    boolean allowedBox (int r, int c, int num) {
        for (int i = 0; i < SRN; i++) {
            for (int j = 0; j < SRN; j++) {
                if(board[i+r][j+c] == num)
                {
                    return false;
                }
            }
        }
        return true;
    }
    boolean allowedRow (int r, int num) {
        for (int i = 0; i < N; i++) {
            if (board[r][i] == num) {
                return false;
            }
        }
        return true;
    }
    boolean allowedCol (int c, int num) {
        for (int i = 0; i < N; i++) {
            if (board[i][c] == num) {
                return false;
            }
        }
        return true;
    }
    boolean numAllowed (int r, int c, int num) {
        return (allowedRow(r, num) && allowedCol(c, num) && allowedBox(r-r%SRN, c-c%SRN, num));
    }

    //Method to fill a specified 3x3 box of the board
    void fillBox(int r, int c) {
        int cell;
        for (int i = 0; i < SRN; i++) {
            for (int j = 0; j < SRN; j++) {
                do{
                    cell = randomGenerator(N);
                }
                while (!allowedBox(r, c, cell));
                board[r+i][c+j] = cell;
            }
        }
    }


    //Filling 3 diagonal boxes (Top left, mid, bottom right)
    void fillBoardDiag (){
        for (int i = 0; i < N; i+=SRN) {
            fillBox(i, i);
        }
    }


    // Method to fill the remaining cells using backtracking
    public boolean fillRemaining(int i, int j) {
        // Base case: If we reach the end of the board, return true
        if (i == N - 1 && j == N) {
            return true;
        }

        // Move to the next row if we reach the end of the current row
        if (j == N) {
            i++;
            j = 0;
        }

        // Skip the cells that are already filled
        if (board[i][j] != 0) {
            return fillRemaining(i, j + 1);
        }

        // Try different numbers in the current cell
        for (int num = 1; num <= N; num++) {
            if (numAllowed(i, j, num)) {
                board[i][j] = num;

                // Move to the next cell
                if (fillRemaining(i, j + 1)) {
                    return true;
                }

                // Backtrack and try the next number
                board[i][j] = 0;
            }
        }

        // No valid number found for the current cell, backtrack
        return false;
    }

    //Removing digits from the board to be filled in by the player
    public void removeDigits(){
        int i = K;
        while (i != 0){
            int cell = randomGenerator(N*N)-1;

            int r = (cell/N);
            int c = cell%9;
            if (c != 0) c--;

            if (board[r][c] != 0)
            {
                i--;
                board[r][c] = 0;
            }
        }
    }

    // Driver
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        //Setting variables for Sudoku board
        int N = 9, K = 0;
        PreGUI setPanel = new PreGUI();
        String diff = setPanel.preSettings();
        switch (diff) {
            case "Easy" -> K = 15;
            case "Medium" -> K = 30;
            case "Hard" -> K = 40;
        }

        //Initializing Sudoku board
        Sudoku sudoku = new Sudoku(N, K);
        sudoku.fillBoardDiag();
        sudoku.fillRemaining(0, sudoku.SRN);

        int[][] corrBoard = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                corrBoard[i][j] = sudoku.board[i][j];
            }
        }
        sudoku.removeDigits();

        // Display the Sudoku board using GUI
        SwingUtilities.invokeLater(() -> {
            SudokuGUI sudokuGUI = new SudokuGUI(sudoku.board, corrBoard);
            sudokuGUI.displayGUI();
        });
    }
}