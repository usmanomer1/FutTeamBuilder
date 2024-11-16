package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PitchPanel extends JPanel {
    private Map<Integer, JButton> positionButtons = new HashMap<>();
    private JPanel substitutesPanel;
    private JButton toggleSubsButton;
    private boolean isSubstitutesVisible = false;
    private String formationType;

    public PitchPanel(ActionListener addPlayerListener, String formationType) {
        setLayout(null);
        this.formationType = formationType;

        // Load the pitch image from the data directory
        File imageFile = new File("./data/pitch.png");
        if (!imageFile.exists()) {
            System.out.println("pitch.png not found at " + imageFile.getAbsolutePath());
            // If the image is not found, set a default background color
            setBackground(Color.GREEN);
        } else {
            ImageIcon pitchIcon = new ImageIcon(imageFile.getAbsolutePath());
            JLabel pitchLabel = new JLabel(pitchIcon);
            pitchLabel.setBounds(0, 0, pitchIcon.getIconWidth(), pitchIcon.getIconHeight());
            add(pitchLabel);

            // Positions coordinates (x, y) for each player on the pitch according to formation
            int[][] positions = getPositionsForFormation(formationType);

            for (int i = 0; i < positions.length; i++) {
                JButton btn = new JButton();
                btn.setBounds(positions[i][0], positions[i][1], 80, 100);
                btn.addActionListener(addPlayerListener);
                btn.setActionCommand(String.valueOf(i)); // Use position index as action command
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setForeground(Color.BLACK);
                btn.setFont(new Font("Arial", Font.BOLD, 12));

                // Set empty card slot image
                ImageIcon emptyCardIcon = new ImageIcon("./data/empty_card.png");
                btn.setIcon(emptyCardIcon);

                positionButtons.put(i, btn);
                pitchLabel.add(btn);
            }

            // Substitutes Panel
            substitutesPanel = new JPanel();
            substitutesPanel.setLayout(new FlowLayout());
            substitutesPanel.setBounds(0, pitchIcon.getIconHeight(), pitchIcon.getIconWidth(), 100);
            substitutesPanel.setBackground(new Color(0, 100, 0)); // Dark green
            substitutesPanel.setVisible(isSubstitutesVisible);
            add(substitutesPanel);

            // Toggle Substitutes Button
            toggleSubsButton = new JButton("Show Substitutes");
            toggleSubsButton.setBounds(pitchIcon.getIconWidth() - 150, pitchIcon.getIconHeight() - 50, 140, 30);
            toggleSubsButton.addActionListener(e -> toggleSubstitutes());
            add(toggleSubsButton);
        }
    }

    public void updatePlayerCard(int positionIndex, String playerName, String stats) {
        if (positionIndex < 11) {
            JButton btn = positionButtons.get(positionIndex);
            btn.setText("<html><center>" + playerName + "<br>" + stats + "</center></html>");
            btn.setEnabled(false); // Disable to prevent re-adding
        } else {
            addSubstitutePlayer(playerName);
        }
    }

    private void addSubstitutePlayer(String playerName) {
        JButton playerButton = new JButton(playerName);
        playerButton.setPreferredSize(new Dimension(100, 30));
        substitutesPanel.add(playerButton);
        substitutesPanel.revalidate();
        substitutesPanel.repaint();
    }

    private void toggleSubstitutes() {
        isSubstitutesVisible = !isSubstitutesVisible;
        substitutesPanel.setVisible(isSubstitutesVisible);
        toggleSubsButton.setText(isSubstitutesVisible ? "Hide Substitutes" : "Show Substitutes");
        revalidate();
        repaint();
    }

    private int[][] getPositionsForFormation(String formationType) {
        // Define positions for different formations
        switch (formationType) {
            case "4-4-2":
                return new int[][]{
                        {220, 20},   // Goalkeeper
                        {100, 100},  // Left Back
                        {340, 100},  // Right Back
                        {160, 100},  // Center Back Left
                        {280, 100},  // Center Back Right
                        {100, 220},  // Left Midfielder
                        {340, 220},  // Right Midfielder
                        {160, 220},  // Center Midfielder Left
                        {280, 220},  // Center Midfielder Right
                        {180, 340},  // Striker Left
                        {260, 340}   // Striker Right
                };
            case "4-3-3":
                return new int[][]{
                        {220, 20},   // Goalkeeper
                        {100, 100},  // Left Back
                        {340, 100},  // Right Back
                        {160, 100},  // Center Back Left
                        {280, 100},  // Center Back Right
                        {220, 180},  // Center Midfielder
                        {160, 180},  // Left Midfielder
                        {280, 180},  // Right Midfielder
                        {100, 300},  // Left Winger
                        {340, 300},  // Right Winger
                        {220, 340}   // Striker
                };
            case "3-5-2":
                return new int[][]{
                        {220, 20},   // Goalkeeper
                        {160, 100},  // Center Back Left
                        {280, 100},  // Center Back Right
                        {220, 100},  // Center Back Center
                        {100, 180},  // Left Midfielder
                        {340, 180},  // Right Midfielder
                        {160, 180},  // Center Midfielder Left
                        {280, 180},  // Center Midfielder Right
                        {220, 220},  // Center Midfielder Center
                        {180, 340},  // Striker Left
                        {260, 340}   // Striker Right
                };
            case "5-3-2":
                return new int[][]{
                        {220, 20},   // Goalkeeper
                        {80, 100},   // Left Back
                        {360, 100},  // Right Back
                        {140, 100},  // Center Back Left
                        {300, 100},  // Center Back Right
                        {220, 100},  // Center Back Center
                        {220, 180},  // Center Midfielder
                        {160, 180},  // Left Midfielder
                        {280, 180},  // Right Midfielder
                        {180, 340},  // Striker Left
                        {260, 340}   // Striker Right
                };
            default:
                // Default to 4-4-2 if unknown formation
                return new int[][]{
                        {220, 20},   // Goalkeeper
                        {100, 100},  // Left Back
                        {340, 100},  // Right Back
                        {160, 100},  // Center Back Left
                        {280, 100},  // Center Back Right
                        {100, 220},  // Left Midfielder
                        {340, 220},  // Right Midfielder
                        {160, 220},  // Center Midfielder Left
                        {280, 220},  // Center Midfielder Right
                        {180, 340},  // Striker Left
                        {260, 340}   // Striker Right
                };
        }
    }
}
