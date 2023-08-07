import javax.swing.*;
import java.awt.*;

public class PreGUI {
    private final String[] difficulties = {"Easy", "Medium", "Hard", "Select Difficulty"};
    private String selectedDiff;

    public String preSettings() {
        // Create a JDialog to act as the PreGUI window
        JDialog dialog = new JDialog((Frame) null, "Settings", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 200);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Dropdown menu of difficulties
        JComboBox<String> diffDropdown = new JComboBox<>(difficulties);
        diffDropdown.setSelectedIndex(3);

        // Button to end preGUI and enter the game
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            selectedDiff = (String) diffDropdown.getSelectedItem();
            dialog.dispose(); // Close the dialog after the user clicks "Start"
        });


        dialog.add(diffDropdown);
        dialog.add(startButton, BorderLayout.SOUTH);


        // Center the dialog on the screen
        dialog.setLocationRelativeTo(null);

        // Show the dialog and wait for the user to make a selection
        dialog.setVisible(true);

        return selectedDiff;
    }
}
