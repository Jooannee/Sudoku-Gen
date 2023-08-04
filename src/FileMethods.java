import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileMethods {
    private int[][] Board;
    private int[][] corrBoard;
    JFileChooser fc = new JFileChooser();
    FileWriter fw;

    // Constructors
    FileMethods(int[][] Board, int[][] corrBoard){
        this.Board = Board;
        this.corrBoard = corrBoard;
    }
    FileMethods(){}

    // Method to export a board to a text file
    public void exportBoard() {
        // Setting up FileWriter
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                // Writing Playerboard to file
                fw = new FileWriter(file, true);
                fw.write("Playerboard: \n");
                for (int[] ints : Board) {
                    for (int j = 0; j < Board.length; j++) {
                        fw.write(ints[j] + " ");
                    }
                    fw.write("\n");
                }

                // Writing Answerboard to file
                fw.write("Answerboard: \n");
                for (int[] ints : corrBoard) {
                    for (int j = 0; j < corrBoard.length; j++) {
                        fw.write(ints[j] + " ");
                    }
                    fw.write("\n");
                }

                fw.flush();
                fw.close();
                JOptionPane.showMessageDialog(null, "Export completed");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error writing to file");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Save error or operation cancelled");
        }
    }
}
