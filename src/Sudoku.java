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

    //Methods to check that a number is allowed in a cell based on Box, Row & Column
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
    //Filling the rest of the board
    boolean fillRemaining(int r, int c) {
        //  System.out.println(i+" "+j);
        if (c>=N && r<N-1)
        {
            r = r + 1;
            c = 0;
        }
        if (r>=N && c>=N)
            return true;

        if (r < SRN)
        {
            if (c < SRN)
                c = SRN;
        }
        else if (r < N-SRN)
        {
            if (c==(int)(r/SRN)*SRN)
                c =  c + SRN;
        }
        else
        {
            if (c == N-SRN)
            {
                r = r + 1;
                c = 0;
                if (r>=N)
                    return true;
            }
        }

        for (int num = 1; num<=N; num++)
        {
            if (numAllowed(r, c, num))
            {
                board[r][c] = num;
                if (fillRemaining(r, c+1))
                    return true;

                board[r][c] = 0;
            }
        }
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

    //Method to finally print the sudoku, a standard 2D array printing method
    public void printSudoku() {
        for (int i = 0; i<N; i++)
        {
            for (int j = 0; j<N; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }

    // Driver
    public static void main(String[] args) {
        //Setting variables for Sudoku board
        int N = 9, K = 0;
        PreGUI setPanel = new PreGUI();
        String diff = setPanel.displayGUI();
        switch (diff) {
            case "Easy" -> K = 15;
            case "Medium" -> K = 30;
            case "Hard" -> K = 40;
        }

        //Initializing Sudoku board
        Sudoku sudoku = new Sudoku(N, K);
        sudoku.fillBoardDiag();
        sudoku.fillRemaining(0, sudoku.SRN);
        sudoku.removeDigits();

        // Display the Sudoku board using GUI
        SwingUtilities.invokeLater(() -> {
            SudokuGUI sudokuGUI = new SudokuGUI(sudoku.board);
            sudokuGUI.displayGUI();
        });
    }
}