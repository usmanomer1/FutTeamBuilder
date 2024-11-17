package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.Player;

public class PitchPanel extends JPanel {
    private Map<Integer, JButton> positionButtons = new HashMap<>();
    private Map<Integer, JButton> substituteButtons = new HashMap<>();
    private JButton toggleSubsButton;
    private boolean isSubstitutesVisible = false;
    private String formationType;

    // Card dimensions
    private static final int CARD_WIDTH = 120;
    private static final int CARD_HEIGHT = 160;

    private JPanel centerPanel;
    private JLayeredPane startingElevenPane;
    private JLayeredPane substitutesPane;

    public PitchPanel(ActionListener addPlayerListener, String formationType) {
        this.formationType = formationType;
        setLayout(new BorderLayout());
        setBackground(Color.GREEN); // Default background

        // Print current working directory
        System.out.println("Current working directory: " + new File(".").getAbsolutePath());

        // Centering Panel using BorderLayout to fill the center
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(true); // Set to true to ensure background is painted
        centerPanel.setBackground(Color.GREEN); // Default background color

        // Initialize starting eleven pane
        startingElevenPane = createPitchPane(addPlayerListener, formationType, false);
        centerPanel.add(startingElevenPane, BorderLayout.CENTER);

        // Initialize substitutes pane
        substitutesPane = createPitchPane(addPlayerListener, formationType, true);

        // Add centerPanel to PitchPanel
        add(centerPanel, BorderLayout.CENTER);

        // Toggle Substitutes Button
        toggleSubsButton = new JButton("Show Substitutes");
        toggleSubsButton.addActionListener(e -> toggleSubstitutes());
        JPanel toggleButtonPanel = new JPanel();
        toggleButtonPanel.setOpaque(false);
        toggleButtonPanel.add(toggleSubsButton);
        add(toggleButtonPanel, BorderLayout.NORTH);
    }

    private JLayeredPane createPitchPane(ActionListener addPlayerListener, String formationType, boolean isSubstitutes) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null); // Use absolute positioning
        layeredPane.setOpaque(true); // Ensure the background is painted
        layeredPane.setBackground(Color.GREEN); // Default background color

        // Load the pitch image using file system paths
        BufferedImage pitchImage = null;
        File imageFile = new File("./data/pitch.png");
        if (!imageFile.exists()) {
            System.out.println("pitch.png not found at " + imageFile.getAbsolutePath());
        } else {
            try {
                pitchImage = ImageIO.read(imageFile);
                System.out.println("pitch.png loaded successfully with dimensions: " + pitchImage.getWidth() + "x" + pitchImage.getHeight());
            } catch (IOException e) {
                System.out.println("Error loading pitch image: " + e.getMessage());
                e.printStackTrace();
            }
        }

        int pitchWidth;
        int pitchHeight;

        if (pitchImage == null) {
            System.out.println("pitch.png could not be loaded.");
            pitchWidth = 800;
            pitchHeight = 600;
            layeredPane.setPreferredSize(new Dimension(pitchWidth, pitchHeight));

            // Optional: Add a label to indicate the image is missing
            JLabel errorLabel = new JLabel("Pitch image not found.");
            errorLabel.setForeground(Color.WHITE);
            errorLabel.setBounds(50, 50, 200, 30);
            layeredPane.add(errorLabel, Integer.valueOf(1));
        } else {
            pitchWidth = pitchImage.getWidth();
            pitchHeight = pitchImage.getHeight();

            layeredPane.setPreferredSize(new Dimension(pitchWidth, pitchHeight));

            JLabel pitchLabel = new JLabel(new ImageIcon(pitchImage));
            pitchLabel.setBounds(0, 0, pitchWidth, pitchHeight);
            layeredPane.add(pitchLabel, Integer.valueOf(0)); // Base layer
        }

        // Positions coordinates for players
        int[][] positions = isSubstitutes
                ? getPositionsForSubstitutes(pitchWidth, pitchHeight)
                : getPositionsForFormation(formationType, pitchWidth, pitchHeight);

        for (int i = 0; i < positions.length; i++) {
            JButton btn = new JButton();
            btn.setBounds(positions[i][0], positions[i][1], CARD_WIDTH, CARD_HEIGHT);
            btn.addActionListener(addPlayerListener);
            int actionIndex = isSubstitutes ? i + 11 : i;
            btn.setActionCommand(String.valueOf(actionIndex)); // Use position index as action command
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setForeground(Color.BLACK);
            btn.setFont(new Font("Arial", Font.BOLD, 12));

            // Set empty card slot image using file system paths
            ImageIcon emptyCardIcon = loadImageIcon("./data/empty_card.png", CARD_WIDTH, CARD_HEIGHT);
            if (emptyCardIcon != null) {
                btn.setIcon(emptyCardIcon);
            } else {
                System.out.println("empty_card.png could not be loaded.");
                btn.setText("+");
            }

            if (isSubstitutes) {
                substituteButtons.put(actionIndex, btn);
            } else {
                positionButtons.put(actionIndex, btn);
            }
            layeredPane.add(btn, Integer.valueOf(1)); // Above the pitch image
        }

        return layeredPane;
    }

    public void updatePlayerCard(int positionIndex, Player player) {
        JButton btn;
        if (positionIndex < 11) {
            btn = positionButtons.get(positionIndex);
        } else {
            btn = substituteButtons.get(positionIndex);
        }

        if (btn != null) {
            ImageIcon playerCardIcon = createPlayerCardIcon(player);
            if (playerCardIcon != null) {
                btn.setIcon(playerCardIcon);
                btn.setText("");
                btn.setEnabled(false);
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
            } else {
                // Fallback to text if image creation fails
                String stats = "<html>PAC: " + player.getPace() + "<br>SHO: " + player.getShooting() + "<br>DRI: " + player.getDribbling() +
                        "<br>DEF: " + player.getDefending() + "<br>PHY: " + player.getPhysicality() + "<br>PAS: " + player.getPassing() + "</html>";
                btn.setText("<html><center>" + player.getName() + "<br>" + stats + "</center></html>");
                btn.setEnabled(false);
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setBorderPainted(true);
                btn.setForeground(Color.BLACK);
                btn.setBackground(Color.WHITE);
            }
        }
    }

    private ImageIcon createPlayerCardIcon(Player player) {
        try {
            // Load the player card template image using file system paths
            File templateFile = new File("./data/player_card_template.png");
            if (!templateFile.exists()) {
                System.out.println("player_card_template.png not found at " + templateFile.getAbsolutePath());
                return null;
            }
            BufferedImage cardTemplate = ImageIO.read(templateFile);

            BufferedImage cardImage = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = cardImage.createGraphics();

            // Draw the template
            g.drawImage(cardTemplate.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);

            // Set text properties
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));

            // Draw player name and stats onto the template
            int textX = 10;
            int textY = 20;
            int lineHeight = 15;

            g.drawString(player.getName(), textX, textY);
            textY += lineHeight;
            g.drawString("PAC: " + player.getPace(), textX, textY);
            textY += lineHeight;
            g.drawString("SHO: " + player.getShooting(), textX, textY);
            textY += lineHeight;
            g.drawString("DRI: " + player.getDribbling(), textX, textY);
            textY += lineHeight;
            g.drawString("DEF: " + player.getDefending(), textX, textY);
            textY += lineHeight;
            g.drawString("PHY: " + player.getPhysicality(), textX, textY);
            textY += lineHeight;
            g.drawString("PAS: " + player.getPassing(), textX, textY);
            textY += lineHeight;
            g.drawString("Rating: " + player.getRating(), textX, textY);

            g.dispose();

            return new ImageIcon(cardImage);
        } catch (IOException e) {
            System.out.println("Error creating player card icon: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void toggleSubstitutes() {
        isSubstitutesVisible = !isSubstitutesVisible;
        centerPanel.removeAll(); // Remove all components from centerPanel
        if (isSubstitutesVisible) {
            centerPanel.add(substitutesPane, BorderLayout.CENTER);
            toggleSubsButton.setText("Show Starting Eleven");
        } else {
            centerPanel.add(startingElevenPane, BorderLayout.CENTER);
            toggleSubsButton.setText("Show Substitutes");
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private int[][] getPositionsForFormation(String formationType, int pitchWidth, int pitchHeight) {
        // Calculate positions relative to the pitch size
        int centerX = pitchWidth / 2 - CARD_WIDTH / 2;
        int centerY = pitchHeight / 2 - CARD_HEIGHT / 2;

        // Example positions for 4-4-2 formation
        switch (formationType) {
            case "4-4-2":
                return new int[][]{
                    {centerX, pitchHeight - CARD_HEIGHT - 50}, // Goalkeeper
                    {centerX - 400, centerY + 200},            // Left Back
                    {centerX + 400, centerY + 200},            // Right Back
                    {centerX - 200, centerY + 200},            // Center Back Left
                    {centerX + 200, centerY + 200},            // Center Back Right
                    {centerX - 400, centerY},                  // Left Midfielder
                    {centerX + 400, centerY},                  // Right Midfielder
                    {centerX - 200, centerY - 50},             // Center Midfielder Left
                    {centerX + 200, centerY - 50},             // Center Midfielder Right
                    {centerX - 100, centerY - 250},            // Striker Left
                    {centerX + 100, centerY - 250}             // Striker Right
                };
            // Add other formations here
            default:
                // Default to 4-4-2
                return new int[][]{
                    {centerX, pitchHeight - CARD_HEIGHT - 50}, // Goalkeeper
                    {centerX - 400, centerY + 200},            // Left Back
                    {centerX + 400, centerY + 200},            // Right Back
                    {centerX - 200, centerY + 200},            // Center Back Left
                    {centerX + 200, centerY + 200},            // Center Back Right
                    {centerX - 400, centerY},                  // Left Midfielder
                    {centerX + 400, centerY},                  // Right Midfielder
                    {centerX - 200, centerY - 50},             // Center Midfielder Left
                    {centerX + 200, centerY - 50},             // Center Midfielder Right
                    {centerX - 100, centerY - 250},            // Striker Left
                    {centerX + 100, centerY - 250}             // Striker Right
                };
        }
    }

    private int[][] getPositionsForSubstitutes(int pitchWidth, int pitchHeight) {
        // Arrange substitutes in rows
        int numSubstitutes = 12;
        int cols = 4;
        int rows = (int) Math.ceil((double) numSubstitutes / cols);

        int xSpacing = (pitchWidth - cols * CARD_WIDTH) / (cols + 1);
        int ySpacing = (pitchHeight - rows * CARD_HEIGHT) / (rows + 1);

        int[][] positions = new int[numSubstitutes][2];
        int index = 0;
        for (int row = 0; row < rows && index < numSubstitutes; row++) {
            for (int col = 0; col < cols && index < numSubstitutes; col++) {
                int x = xSpacing + col * (CARD_WIDTH + xSpacing);
                int y = ySpacing + row * (CARD_HEIGHT + ySpacing);
                positions[index][0] = x;
                positions[index][1] = y;
                index++;
            }
        }
        return positions;
    }

    private ImageIcon loadImageIcon(String filePath, int width, int height) {
        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            System.out.println(filePath + " not found at " + imageFile.getAbsolutePath());
            return null;
        }
        try {
            BufferedImage image = ImageIO.read(imageFile);
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            System.out.println("Error loading image " + filePath + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}