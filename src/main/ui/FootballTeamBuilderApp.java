package ui;

import model.Player;
import model.Team;
import model.TeamRepository;
import model.User;
import model.UserManager;
import model.EventLog;
import model.Formation;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the main application for building football teams.
 */
public class FootballTeamBuilderApp extends JFrame {
    private static final int MAX_PLAYER_RATING = 100;
    private static final int MIN_PLAYER_RATING = 1;
    private static final int MAX_SKILL_MOVES = 5;
    private static final int MIN_SKILL_MOVES = 1;
    private static final int MAX_WEAK_FOOT = 5;
    private static final int MIN_WEAK_FOOT = 1;

    private UserManager userManager;
    private TeamRepository repository;
    private User currentUser;

    private JPanel currentPanel;

    private Clip backgroundClip;

    // Declare pitchPanel, selectedFormation, and teamNameField at the class level
    private PitchPanel pitchPanel;
    private Formation selectedFormation;
    private JTextField teamNameField;

    /**
     * Constructs the FootballTeamBuilderApp.
     */
    public FootballTeamBuilderApp() {
        setTitle("Football Team Builder");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userManager = new UserManager();
        repository = new TeamRepository();

        userManager.loadUsers();
        initializeCommunityTeams();

        playBackgroundMusic();

        showAuthPanel();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printEventLog();
                if (backgroundClip != null && backgroundClip.isRunning()) {
                    backgroundClip.stop();
                }
                dispose();
            }
        });
    }

    /**
     * Prints the event log to the console.
     */
    private void printEventLog() {
        System.out.println("===== Application Event Log =====");
        for (model.Event event : EventLog.getInstance()) {
            System.out.println(event.toString());
        }
        System.out.println("===== End of Event Log =====");
    }

    /**
     * Initializes community teams from all users.
     */
    private void initializeCommunityTeams() {
        for (User user : userManager.getAllUsers()) {
            for (Team team : user.getTeams()) {
                if (team.isListed()) {
                    repository.addTeamToCommunity(team);
                }
            }
        }
    }

    /**
     * Plays background music in a loop.
     */
    private void playBackgroundMusic() {
        try {
            File audioFile = new File("./data/background.wav");
            if (!audioFile.exists()) {
                System.out.println("background.wav not found at " + audioFile.getAbsolutePath());
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("Error playing background music.");
            e.printStackTrace();
        }
    }

    /**
     * Displays the authentication panel.
     */
    private void showAuthPanel() {
        JPanel authPanel = new JPanel();
        authPanel.setLayout(new BorderLayout());
        authPanel.setBackground(new Color(34, 139, 34));

        JLabel welcomeLabel = new JLabel("Welcome to the Football Team Builder!", JLabel.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton signUpButton = new JButton("Sign Up");
        JButton loginButton = new JButton("Login");

        signUpButton.addActionListener(e -> signUp());
        loginButton.addActionListener(e -> login());

        buttonPanel.add(signUpButton);
        buttonPanel.add(loginButton);

        authPanel.add(welcomeLabel, BorderLayout.CENTER);
        authPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(authPanel);
        revalidate();
        repaint();

        currentPanel = authPanel;
    }

    /**
     * Displays the main panel after authentication.
     */
    @SuppressWarnings("methodlength")
    private void showMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(34, 139, 34));

        JLabel mainLabel = new JLabel("Main Menu", JLabel.CENTER);
        mainLabel.setForeground(Color.WHITE);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));

        JButton createTeamButton = new JButton("Create a new team");
        JButton viewTeamsButton = new JButton("View your teams");
        JButton searchTeamsButton = new JButton("Search for teams");
        JButton popularTeamsButton = new JButton("View popular teams");
        JButton logoutButton = new JButton("Logout");

        createTeamButton.addActionListener(e -> selectFormationAndCreateTeam());
        viewTeamsButton.addActionListener(e -> viewUserTeams());
        searchTeamsButton.addActionListener(e -> searchTeams());
        popularTeamsButton.addActionListener(e -> viewPopularTeams());
        logoutButton.addActionListener(e -> logout());

        buttonPanel.add(createTeamButton);
        buttonPanel.add(viewTeamsButton);
        buttonPanel.add(searchTeamsButton);
        buttonPanel.add(popularTeamsButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(mainLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        revalidate();
        repaint();

        currentPanel = mainPanel;
    }

    /**
     * Logs out the current user.
     */
    private void logout() {
        currentUser = null;
        showAuthPanel();
    }

    /**
     * Displays formation selection and proceeds to team creation.
     */
    @SuppressWarnings("methodlength")
    private void selectFormationAndCreateTeam() {
        Set<String> formationCodes = Formation.getAllFormationTypes();
        Map<String, String> formationMap = new LinkedHashMap<>();
        for (String code : formationCodes) {
            String displayName = code.replaceAll("(\\d)(?=(\\d))", "$1-");
            formationMap.put(displayName, code);
        }
        String[] formations = formationMap.keySet().toArray(new String[0]);

        String formationDisplayName = (String) JOptionPane.showInputDialog(
                this,
                "Select Formation:",
                "Formation Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                formations,
                formations[0]);

        if (formationDisplayName != null) {
            String formationCode = formationMap.get(formationDisplayName);
            try {
                selectedFormation = new Formation(formationCode);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Selected formation is unsupported.");
                return;
            }
            showTeamPanel();
        }
    }

    /**
     * Displays the team creation panel.
     */
    @SuppressWarnings("methodlength")
    private void showTeamPanel() {
        JPanel teamPanel = new JPanel(new BorderLayout());
        teamPanel.setBackground(new Color(34, 139, 34));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        JLabel teamNameLabel = new JLabel("Enter team name:");
        teamNameLabel.setForeground(Color.WHITE);
        teamNameField = new JTextField(20);
        topPanel.add(teamNameLabel);
        topPanel.add(teamNameField);

        pitchPanel = new PitchPanel(e -> addPlayerToTeam((ActionEvent) e), selectedFormation);

        JButton saveTeamButton = new JButton("Save Team");
        saveTeamButton.addActionListener(e -> saveTeam());
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showMainPanel());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(saveTeamButton);
        bottomPanel.add(backButton);

        teamPanel.add(topPanel, BorderLayout.NORTH);
        teamPanel.add(pitchPanel, BorderLayout.CENTER);
        teamPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(teamPanel);
        revalidate();
        repaint();

        currentPanel = teamPanel;
    }

    /**
     * Handles user sign-up.
     */
    private void signUp() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.");
            return;
        }

        String password = JOptionPane.showInputDialog(this, "Enter password:");
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.");
            return;
        }

        try {
            currentUser = userManager.signUp(username, password);
            userManager.saveUsers();
            JOptionPane.showMessageDialog(this, "Signup successful! Welcome, " + username + "!");
            showMainPanel();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Signup failed: " + e.getMessage());
        }
    }

    /**
     * Handles user login.
     */
    private void login() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.");
            return;
        }

        String password = JOptionPane.showInputDialog(this, "Enter password:");
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.");
            return;
        }

        try {
            currentUser = userManager.login(username, password);
            JOptionPane.showMessageDialog(this, "Login successful! Welcome back, " + username + "!");
            showMainPanel();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Login failed: " + e.getMessage());
        }
    }

    /**
     * Adds a player to the team based on user input.
     *
     * @param e the ActionEvent triggered by clicking a position
     */
    @SuppressWarnings("methodlength")
    private void addPlayerToTeam(ActionEvent e) {
        String positionName = e.getActionCommand().toUpperCase().trim();

        String teamName = teamNameField.getText().trim();
        if (teamName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a team name before adding players.");
            return;
        }

        Player player = promptForPlayerDetails(positionName);
        if (player == null) {
            return;
        }

        Team team = getOrCreateTeam(teamName);

        if (team.hasPlayer(player.getName())) {
            JOptionPane.showMessageDialog(this, "Player already exists in the team.");
            return;
        }

        if (!team.addPlayer(player)) {
            JOptionPane.showMessageDialog(this, "Cannot add player. Team might be full.");
            return;
        }

        if (!team.setPlayerInStarting11(player, true)) {
            JOptionPane.showMessageDialog(this,
                    "Cannot set player to starting 11. Position might be occupied or invalid.");
            return;
        }

        pitchPanel.updatePlayerCard(positionName, player);

        JOptionPane.showMessageDialog(this, "Player added to " + teamName);
    }

    /**
     * Prompts the user for player details and creates a Player object.
     *
     * @param positionName the position name
     * @return the created Player object, or null if cancelled
     */
    @SuppressWarnings("methodlength")
    private Player promptForPlayerDetails(String positionName) {
        String name = JOptionPane.showInputDialog(this, "Enter player name:");
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        String nationality = JOptionPane.showInputDialog(this, "Enter player nationality:");
        if (nationality == null) {
            nationality = "";
        }

        String league = JOptionPane.showInputDialog(this, "Enter player league:");
        if (league == null) {
            league = "";
        }

        String clubAffiliation = JOptionPane.showInputDialog(this, "Enter player club affiliation:");
        if (clubAffiliation == null) {
            clubAffiliation = "";
        }

        String preferredPosition = JOptionPane.showInputDialog(
                this, "Enter player preferred position (optional):");
        if (preferredPosition == null || preferredPosition.trim().isEmpty()) {
            preferredPosition = positionName;
        }
        preferredPosition = preferredPosition.toUpperCase().trim();

        int rating = getIntInput("Enter player rating (1-100):", MIN_PLAYER_RATING, MAX_PLAYER_RATING);
        int pace = getIntInput("Enter player pace (1-100):", MIN_PLAYER_RATING, MAX_PLAYER_RATING);
        int passing = getIntInput("Enter player passing (1-100):", MIN_PLAYER_RATING, MAX_PLAYER_RATING);
        int shooting = getIntInput("Enter player shooting (1-100):", MIN_PLAYER_RATING, MAX_PLAYER_RATING);
        int dribbling = getIntInput("Enter player dribbling (1-100):", MIN_PLAYER_RATING, MAX_PLAYER_RATING);
        int defending = getIntInput("Enter player defending (1-100):", MIN_PLAYER_RATING, MAX_PLAYER_RATING);
        int physicality = getIntInput("Enter player physicality (1-100):", MIN_PLAYER_RATING, MAX_PLAYER_RATING);
        int skillMoves = getIntInput("Enter player skill moves (1-5):", MIN_SKILL_MOVES, MAX_SKILL_MOVES);
        int weakFoot = getIntInput("Enter player weak foot (1-5):", MIN_WEAK_FOOT, MAX_WEAK_FOOT);
        int price = getIntInput("Enter player price:", 0, Integer.MAX_VALUE);

        boolean isInStarting11 = true;

        return new Player(
                name,
                nationality,
                league,
                clubAffiliation,
                preferredPosition,
                positionName,
                rating,
                pace,
                passing,
                shooting,
                dribbling,
                defending,
                physicality,
                skillMoves,
                weakFoot,
                price,
                isInStarting11
        );
    }

    /**
     * Retrieves or creates a team with the given name.
     *
     * @param teamName the team name
     * @return the Team object
     */
    private Team getOrCreateTeam(String teamName) {
        Team team = currentUser.getTeamByName(teamName);
        if (team == null) {
            team = new Team(teamName, selectedFormation.getFormationType());
            currentUser.addTeam(team);
        }
        return team;
    }

    /**
     * Gets integer input from the user within specified bounds.
     *
     * @param message the prompt message
     * @param min     the minimum acceptable value
     * @param max     the maximum acceptable value
     * @return the integer value input by the user
     */
    private int getIntInput(String message, int min, int max) {
        while (true) {
            String inputStr = JOptionPane.showInputDialog(this, message);
            if (inputStr == null) {
                return min;
            }
            try {
                int input = Integer.parseInt(inputStr);
                if (input >= min && input <= max) {
                    return input;
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a value between " + min + " and " + max);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Saves the current team to the repository.
     */
    private void saveTeam() {
        String teamName = teamNameField.getText().trim();
        if (teamName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a team name before saving the team.");
            return;
        }

        Team team = currentUser.getTeamByName(teamName);
        if (team != null && team.isStartingLineupComplete()) {
            team.setListed(true);
            repository.addTeamToCommunity(team);
            userManager.saveUsers();
            JOptionPane.showMessageDialog(this, "Team " + teamName + " saved and listed in community.");
            showMainPanel();
        } else {
            int playersNeeded = 11 - (team != null ? team.getStartingPlayers().size() : 0);
            JOptionPane.showMessageDialog(this,
                    "Cannot save incomplete team! You need to add " + playersNeeded
                            + " more players to the starting eleven.");
        }
    }

    /**
     * Displays the user's teams.
     */
    @SuppressWarnings("methodlength")
    private void viewUserTeams() {
        JPanel userTeamsPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> teamListModel = new DefaultListModel<>();

        if (currentUser.getTeams().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have no teams.");
        } else {
            for (Team team : currentUser.getTeams()) {
                teamListModel.addElement(team.getName() + " (Likes: " + team.getLikes() + ")");
            }

            JList<String> teamList = new JList<>(teamListModel);
            JScrollPane scrollPane = new JScrollPane(teamList);
            userTeamsPanel.add(scrollPane, BorderLayout.CENTER);

            JButton backButton = new JButton("Back to Main Menu");
            backButton.addActionListener(e -> showMainPanel());

            userTeamsPanel.add(backButton, BorderLayout.SOUTH);

            int selection = JOptionPane.showConfirmDialog(
                    this, userTeamsPanel, "Your Teams", JOptionPane.OK_CANCEL_OPTION);
            if (selection == JOptionPane.OK_OPTION) {
                int selectedIndex = teamList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Team selectedTeam = currentUser.getTeams().get(selectedIndex);
                    displayTeamDetails(selectedTeam);
                }
            }
        }
    }

    /**
     * Searches for teams based on user criteria.
     */
    @SuppressWarnings("methodlength")
    private void searchTeams() {
        JPanel searchPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel ratingLabel = new JLabel("Minimum Average Rating:");
        JSlider ratingSlider = new JSlider(0, 100, 50);
        ratingSlider.setMajorTickSpacing(10);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);

        JLabel budgetLabel = new JLabel("Maximum Total Price:");
        JTextField budgetField = new JTextField();

        JLabel playerNameLabel = new JLabel("Desired Player Name:");
        JTextField playerNameField = new JTextField();

        searchPanel.add(ratingLabel);
        searchPanel.add(ratingSlider);
        searchPanel.add(budgetLabel);
        searchPanel.add(budgetField);
        searchPanel.add(playerNameLabel);
        searchPanel.add(playerNameField);

        int result = JOptionPane.showConfirmDialog(
                this, searchPanel, "Search Teams", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            double minRating = ratingSlider.getValue();
            String budgetText = budgetField.getText().trim();
            String desiredPlayerName = playerNameField.getText().trim();

            int maxBudget = Integer.MAX_VALUE;
            if (!budgetText.isEmpty()) {
                try {
                    maxBudget = Integer.parseInt(budgetText);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid budget input. Please enter a valid number.");
                    return;
                }
            }

            List<Team> matchingTeams = repository.searchTeams(maxBudget, minRating, desiredPlayerName);

            if (matchingTeams.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No teams match your search criteria.");
            } else {
                displayTeamsBrief(matchingTeams, "Search Results");
            }
        }
    }

    /**
     * Displays popular teams.
     */
    private void viewPopularTeams() {
        List<Team> popularTeams = repository.getTeamsByPopularity();
        if (popularTeams.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No teams available in the community.");
        } else {
            displayTeamsBrief(popularTeams, "Popular Teams");
        }
    }

    /**
     * Displays a brief overview of the given teams.
     *
     * @param teams the list of teams to display
     * @param title the title of the display
     */
    @SuppressWarnings("methodlength")
    private void displayTeamsBrief(List<Team> teams, String title) {
        JPanel teamsPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> teamListModel = new DefaultListModel<>();

        for (Team team : teams) {
            teamListModel.addElement(team.getName() + " | Avg Rating: "
                    + String.format("%.2f", team.getAverageRating())
                    + ", Chemistry: " + team.calculateChemistry()
                    + ", Total Price: " + team.getTotalPrice()
                    + ", Likes: " + team.getLikes());
        }

        JList<String> teamList = new JList<>(teamListModel);
        JScrollPane scrollPane = new JScrollPane(teamList);
        teamsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showMainPanel());
        teamsPanel.add(backButton, BorderLayout.SOUTH);

        int selection = JOptionPane.showConfirmDialog(
                this, teamsPanel, title, JOptionPane.OK_CANCEL_OPTION);
        if (selection == JOptionPane.OK_OPTION) {
            int selectedIndex = teamList.getSelectedIndex();
            if (selectedIndex != -1) {
                Team selectedTeam = teams.get(selectedIndex);
                displayTeamDetails(selectedTeam);
            }
        }
    }

    /**
     * Displays detailed information about a team.
     *
     * @param team the team to display
     */
    @SuppressWarnings("methodlength")
    private void displayTeamDetails(Team team) {
        StringBuilder details = new StringBuilder();
        details.append("Team Name: ").append(team.getName()).append("\n")
                .append("Total Price: ").append(team.getTotalPrice()).append("\n")
                .append("Average Rating: ").append(String.format("%.2f", team.getAverageRating()))
                .append("\n")
                .append("Chemistry: ").append(team.calculateChemistry()).append("\n")
                .append("Likes: ").append(team.getLikes()).append("\n")
                .append("Players:\n");

        for (Player player : team.getPlayers()) {
            details.append("- ").append(player.getName())
                    .append(" (Position: ").append(player.getCurrentPosition())
                    .append(", Rating: ").append(player.getRating())
                    .append(", Price: ").append(player.getPrice())
                    .append(")\n");
        }

        JPanel detailPanel = new JPanel(new BorderLayout());
        JTextArea detailArea = new JTextArea(details.toString());
        detailArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailArea);

        JButton likeButton = new JButton("Like");
        likeButton.addActionListener(e -> {
            team.likeTeam();
            JOptionPane.showMessageDialog(this, "You liked " + team.getName());
        });

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showMainPanel());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(likeButton);
        buttonPanel.add(backButton);

        detailPanel.add(scrollPane, BorderLayout.CENTER);
        detailPanel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, detailPanel,
                team.getName() + " Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
