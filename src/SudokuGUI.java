import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;


public class SudokuGUI {
        private int[][] board;
        private int[][] corrBoard;
        private int N;
        private int SRN;
        private JFrame mainFrame;
        private JFrame corrFrame;
        private Timer timer;
        private long elapsedTime;
        private JLabel timerLabel;
        private JLabel[][] cellLabels;
        private JLabel[][] corrCellLabels;

        // Constructor
        SudokuGUI(int[][] board, int[][] corrBoard) {
                this.board = board;
                this.corrBoard = corrBoard;
                this.N = board.length;
                this.SRN = (int) Math.sqrt(N);
                this.elapsedTime = 0;
                cellLabels = new JLabel[N][N];
                corrCellLabels = new JLabel[N][N];
        }

        // Method to create and display the Sudoku board GUI
        public void displayGUI() {
                // Setting up player window
                mainFrame = new JFrame("Sudoku Board");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(500, 500);
                mainFrame.setLayout(new BorderLayout());

                JPanel boardPanel = new JPanel();
                boardPanel.setLayout(new GridLayout(N, N));

                // Setting up answer window
                corrFrame = new JFrame("Answer Board");
                corrFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                corrFrame.setSize(500, 500);
                corrFrame.setLayout(new BorderLayout());

                JPanel corrBoardPanel = new JPanel();
                corrBoardPanel.setLayout(new GridLayout(N, N));

                // Setting window icon & Dock icon (If supported)
                ArrayList<Image> icons = new ArrayList<>();
                icons.add(Toolkit.getDefaultToolkit().getImage("src/Icons/Icon 128.png"));
                icons.add(Toolkit.getDefaultToolkit().getImage("src/Icons/Icon 64.png"));
                icons.add(Toolkit.getDefaultToolkit().getImage("src/Icons/Icon 32.png"));
                mainFrame.setIconImages(icons);

                // Setting taskbar Icon for OS which support it
                final Taskbar taskbar = Taskbar.getTaskbar();
                try {
                        taskbar.setIconImage(Toolkit.getDefaultToolkit().getImage("src/Icons/Icon 128.png"));
                } catch (final UnsupportedOperationException e) {
                        System.out.println("The os does not support: 'taskbar.setIconImage'");
                } catch (final SecurityException e) {
                        System.out.println("There was a security exception for: 'taskbar.setIconImage'");
                }


                // Setting up Menubar & "File" Menu
                JMenuBar menuBar = new JMenuBar();
                JMenu fileMenu = new JMenu("File");
                menuBar.add(fileMenu);

                // Adding buttons to "File" Menu
                JMenuItem expButton = new JMenuItem("Export Board");
                expButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                FileMethods fm = new FileMethods(board, corrBoard);
                                fm.exportBoard();
                        }
                });

                JMenuItem prtButton = new JMenuItem("Print Board");
                prtButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                FileMethods fm = new FileMethods(board, corrBoard);
                                fm.printBoard();
                        }
                });

                JMenuItem impButton = new JMenuItem("Import Board");
                impButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                });

                fileMenu.add(expButton);
                fileMenu.add(prtButton);
                fileMenu.addSeparator();
                fileMenu.add(impButton);

                // Display the timer in the main window
                timerLabel = new JLabel("", SwingConstants.CENTER);
                timerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                mainFrame.add(timerLabel, BorderLayout.NORTH);

                // Button for the player to submit the puzzle.
                JButton subButton = new JButton("Finish");
                subButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                stopTimer();
                                solutionCorrect();
                        }
                });
                mainFrame.add(subButton, BorderLayout.SOUTH);


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

                // Fill Answer Board
                for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                                corrCellLabels[i][j] = new JLabel("", SwingConstants.CENTER);
                                corrCellLabels[i][j].setFont(new Font("Arial", Font.PLAIN, 20));

                                // Add a thicker border to separate 3x3 boxes
                                int top = 1, left = 1, bottom = 1, right = 1;
                                if (i % SRN == 0) top = 3;
                                if (j % SRN == 0) left = 3;
                                corrCellLabels[i][j].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                                corrBoardPanel.add(corrCellLabels[i][j]);
                        }
                }

                // Entering numbers into cells for both boards
                mainFrame.add(boardPanel, BorderLayout.CENTER);
                for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                                if (board[i][j] != 0) {
                                        cellLabels[i][j].setText(String.valueOf(board[i][j]));
                                }
                        }
                }

                corrFrame.add(corrBoardPanel, BorderLayout.CENTER);
                for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                                corrCellLabels[i][j].setText(String.valueOf(corrBoard[i][j]));

                        }
                }
                mainFrame.setVisible(true);
                mainFrame.setJMenuBar(menuBar);
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


        // Timer FileMethods
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

        //FileMethods to check if the player's solution of the board is correct based on answer board
        public void solutionCorrect() {
                boolean isCorrect = true;

                for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                                if (!(board[i][j] == corrBoard[i][j])) {
                                        isCorrect = false;
                                        break;
                                }
                        }
                }

                if (isCorrect) {
                        JOptionPane.showMessageDialog(mainFrame, "Congratulations! Sudoku puzzle solved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                        int retry = JOptionPane.showConfirmDialog(mainFrame, "There are errors in the solution. Would you like to keep trying?", "Error", JOptionPane.YES_NO_OPTION);
                        if (retry == JOptionPane.YES_OPTION) {
                                // Continue trying: Start the timer again and update the board
                                startTimer();
                        } else {
                                corrFrame.setVisible(true);
                        }
                }
        }
}