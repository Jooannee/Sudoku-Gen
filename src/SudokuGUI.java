import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Point;
import java.util.ArrayList;


public class SudokuGUI {
        private int[][] board;
        private int N;
        private int SRN;
        private JFrame frame; // Declare frame as a member variable
        private Timer timer;
        private long elapsedTime; // Timer variable to store elapsed time in milliseconds
        private JLabel timerLabel; // Label to display the timer
        private JLabel[][] cellLabels;

        // Constructor
        SudokuGUI(int[][] board) {
                this.board = board;
                this.N = board.length;
                this.SRN = (int) Math.sqrt(N);
                this.elapsedTime = 0;
                cellLabels = new JLabel[N][N];
        }

        // Method to create and display the Sudoku board GUI
        public void displayGUI() {
                frame = new JFrame("Sudoku Board");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(500, 500);
                frame.setLayout(new BorderLayout());

                // Create a label to display the timer
                timerLabel = new JLabel("", SwingConstants.CENTER);
                timerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                frame.add(timerLabel, BorderLayout.NORTH);

                // Submit button for the player to finish the puzzle.
                JButton subButton = new JButton("Submit");
                subButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                stopTimer();
                        }
                });
                frame.add(subButton, BorderLayout.SOUTH);

                JPanel boardPanel = new JPanel();
                boardPanel.setLayout(new GridLayout(N, N));

                // Create and add JLabels to represent Sudoku cells
                for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                                cellLabels[i][j] = new JLabel("", SwingConstants.CENTER);
                                cellLabels[i][j].setFont(new Font("Arial", Font.PLAIN, 20));

                                // Add a thicker border to separate 3x3 boxes
                                int top = 1, left = 1, bottom = 1, right = 1;
                                if (i % SRN == 0) top = 3;
                                if (j % SRN == 0) left = 3;
                                cellLabels[i][j].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                                // Add mouse click listener for empty cells
                                final int row = i;
                                final int col = j;
                                if (board[i][j] == 0) {
                                        cellLabels[i][j].addMouseListener(new MouseAdapter() {
                                                @Override
                                                public void mouseClicked(MouseEvent e) {
                                                        handleCellClick(row, col);
                                                }
                                        });
                                }

                                boardPanel.add(cellLabels[i][j]);
                        }
                }

                frame.add(boardPanel, BorderLayout.CENTER);
                for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                                if (board[i][j] != 0) {
                                        cellLabels[i][j].setText(String.valueOf(board[i][j]));
                                }
                        }
                }
                frame.setVisible(true);
                startTimer();
        }

        // Method to handle cell click for player input
        private void handleCellClick(int row, int col) {
                String input = JOptionPane.showInputDialog(null, "Enter a number (1-9) for the selected cell:", "Player Input", JOptionPane.PLAIN_MESSAGE);

                if (input != null) {
                        try {
                                int number = Integer.parseInt(input);
                                if (number >= 1 && number <= 9) {
                                        board[row][col] = number;
                                        updateCellLabel(row, col); // Update the cell label with the new value
                                } else {
                                        JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number between 1 and 9.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                        } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid integer between 1 and 9.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                }
        }

        // Method to update the label of a specific cell with its new value
        private void updateCellLabel(int row, int col) {
                JLabel cellLabel = cellLabels[row][col];
                if (board[row][col] != 0) {
                        cellLabel.setText(String.valueOf(board[row][col]));
                } else {
                        cellLabel.setText("");
                }
        }


        // Timer Methods
        private void startTimer() {
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                                elapsedTime += 1000; // Update the elapsed time every second (1000 milliseconds)
                                updateTimerDisplay();
                        }
                }, 0, 1000); // Start the timer with a delay of 0 milliseconds and update every 1000 milliseconds (1 second)
        }
        private void updateTimerDisplay() {
                // Convert elapsed time to seconds and minutes for display
                long seconds = elapsedTime / 1000;
                long minutes = seconds / 60;
                seconds %= 60;

                // Format the timer display as "MM:SS"
                String timerText = String.format("%02d:%02d", minutes, seconds);
                timerLabel.setText("Time: " + timerText);
        }
        public void stopTimer() {
                if (timer != null) {
                        timer.cancel();
                }
        }

        //Methods to check if the player's solution of the board is correct (Copied from Sudoku class)
        public void solutionCorrect() {
            boolean isCorrect = true;
            ArrayList<Point> incorrectCells = new ArrayList<>(); // Store the coordinates of incorrect cells

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == 0 || !numAllowed(i, j, board[i][j])) {
                        isCorrect = false;
                        incorrectCells.add(new Point(i, j));
                    }
                }
            }

            if (isCorrect) {
                JOptionPane.showMessageDialog(frame, "Congratulations! Sudoku puzzle solved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int retry = JOptionPane.showConfirmDialog(frame, "There are errors in the solution. Would you like to keep trying?", "Error", JOptionPane.YES_NO_OPTION);
                if (retry == JOptionPane.YES_OPTION) {
                    // Continue trying: Start the timer again and update the board
                    startTimer();
                }
            }
        }

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

}
