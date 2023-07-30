import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreGUI {
    private static JFrame frame;
    private static String[] difficulties = {"Easy", "Medium", "Hard", "Select Difficulty"};
    public static String selectedDiff;

    public static void displayGUI(){
        // GUI Setup
        frame = new JFrame ("Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        // Dropdown menu of difficulties
        JComboBox diffDropdown = new JComboBox(difficulties);
        diffDropdown.setSelectedIndex(3);

        // Button to end preGUI and enter game
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDiff = (String) diffDropdown.getSelectedItem();
            }
        });


        frame.add(diffDropdown, BorderLayout.NORTH);
        frame.add(startButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
