import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;


public class PreGUI {
    private static final String[] difficulties = {"Easy", "Medium", "Hard", "Select Difficulty"};
    private static String selectedDiff;
    private static CountDownLatch latch = new CountDownLatch(1);


    public String displayGUI(){
        // GUI Setup
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        // Dropdown menu of difficulties
        JComboBox<String> diffDropdown = new JComboBox<>(difficulties);
        diffDropdown.setSelectedIndex(3);

        // Button to end preGUI and enter game
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDiff = (String) diffDropdown.getSelectedItem();
                latch.countDown(); // Notify the latch that the button has been clicked
            }
        });


        frame.add(diffDropdown, BorderLayout.NORTH);
        frame.add(startButton, BorderLayout.SOUTH);
        frame.setVisible(true);

        try{
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.dispose();
        return selectedDiff;
    }
}
