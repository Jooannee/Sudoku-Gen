import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileMethods {
    static JFileChooser fc = new JFileChooser();
    static FileWriter fw;
    public static void exportBoard(int[][] Board, int[][] corrBoard) {
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                fw = new FileWriter(file, true);
                for (int i = 0; i< Board.length; i++) {
                    for (int j = 0; j < Board.length; j++) {
                        fw.write(Board[i][j] + " ");
                    }
                    fw.write("\n");
                }
                fw.flush();
                fw.close();
                JOptionPane.showMessageDialog(null, "Export completed");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Save error or operation cancelled");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Save error or operation cancelled");
        }
    }

    public static void printBoard(int[][] Board){

    }
}
