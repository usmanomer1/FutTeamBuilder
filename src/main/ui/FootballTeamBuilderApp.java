package ui;

import model.Player;
import model.Team;
import model.TeamRepository;
import model.User;
import model.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class FootballTeamBuilderApp extends JFrame {
    private UserManager userManager;
    private TeamRepository repository;
    private User currentUser;

    private JPanel authPanel;
    private JPanel mainPanel;
    private JPanel teamPanel;

    public FootballTeamBuilderApp() {
        setTitle("Football Team Builder");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userManager = new UserManager();
        repository = new TeamRepository();

        userManager.loadUsers();
        initializeCommunityTeams();

        showAuthPanel();
    }

    private void initializeCommunityTeams() {
        for (User user : userManager.getAllUsers()) {
            for (Team team : user.getTeams()) {
                if (team.isListed()) {
                    repository.addTeamToCommunity(team);
                }
            }
        }
    }

    // Authentication Panel
    private void showAuthPanel() {
        authPanel = new JPanel();
        authPanel.setLayout(new GridLayout(4, 1));

        JLabel welcomeLabel = new JLabel("Welcome to the Football Team Builder!", JLabel.CENTER);
        JButton signUpButton = new JButton("Sign Up");
        JButton loginButton = new JButton("Login");

        signUpButton.addActionListener(e -> signUp());
        loginButton.addActionListener(e -> login());

        authPanel.add(welcomeLabel);
        authPanel.add(signUpButton);
        authPanel.add(loginButton);

        setContentPane(authPanel);
        setVisible(true);
    }

    // Main Panel
    private void showMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 1));

        JLabel mainLabel = new JLabel("Main Menu", JLabel.CENTER);
        JButton createTeamButton = new JButton("Create a new team");
        JButton viewTeamsButton = new JButton("View your teams");
        JButton searchTeamsButton = new JButton("Search for teams");
        JButton popularTeamsButton = new JButton("View popular teams");

        createTeamButton.addActionListener(e -> showTeamPanel());
        viewTeamsButton.addActionListener(e -> viewUserTeams());
        searchTeamsButton.addActionListener(e -> searchTeams());
        popularTeamsButton.addActionListener(e -> viewPopularTeams());

        mainPanel.add(mainLabel);
        mainPanel.add(createTeamButton);
        mainPanel.add(viewTeamsButton);
        mainPanel.add(searchTeamsButton);
        mainPanel.add(popularTeamsButton);

        setContentPane(mainPanel);
        revalidate();
    }

    // Team Creation Panel
    private void showTeamPanel() {
        teamPanel = new JPanel();
        teamPanel.setLayout(new GridLayout(0, 1));

        JLabel teamNameLabel = new JLabel("Enter team name:");
        JTextField teamNameField = new JTextField();
        JButton addPlayerButton = new JButton("Add Player");
        JButton saveTeamButton = new JButton("Save Team");

        teamPanel.add(teamNameLabel);
        teamPanel.add(teamNameField);
        teamPanel.add(addPlayerButton);
        teamPanel.add(saveTeamButton);

        addPlayerButton.addActionListener(e -> addPlayerToTeam(teamNameField.getText()));
        saveTeamButton.addActionListener(e -> saveTeam(teamNameField.getText()));

        setContentPane(teamPanel);
        revalidate();
    }

    // Sign Up Functionality
    private void signUp() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");

        try {
            currentUser = userManager.signUp(username, password);
            userManager.saveUsers();
            JOptionPane.showMessageDialog(this, "Signup successful! Welcome, " + username + "!");
            showMainPanel();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Signup failed: " + e.getMessage());
        }
    }

    // Displays a list of teams created by the current user
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

        int selection = JOptionPane.showConfirmDialog(this, userTeamsPanel, "Your Teams", JOptionPane.OK_CANCEL_OPTION);
        if (selection == JOptionPane.OK_OPTION) {
            int selectedIndex = teamList.getSelectedIndex();
            if (selectedIndex != -1) {
                Team selectedTeam = currentUser.getTeams().get(selectedIndex);
                displayTeamDetails(selectedTeam);
            }
        }
    }
}

// Searches for teams in the community based on user-specified budget and rating
private void searchTeams() {
    int budget = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter maximum budget:"));
    double minRating = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter minimum average rating:"));
    String playerName = JOptionPane.showInputDialog(this, "Enter desired player name (or leave blank):");

    List<Team> matchingTeams = repository.searchTeams(budget, minRating, playerName);

    if (matchingTeams.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No teams found matching your criteria.");
    } else {
        displayTeamsBrief(matchingTeams, "Teams Found");
    }
}

// Displays community teams sorted by popularity
private void viewPopularTeams() {
    List<Team> popularTeams = repository.getTeamsByPopularity();
    if (popularTeams.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No teams available in the community.");
    } else {
        displayTeamsBrief(popularTeams, "Popular Teams");
    }
}

// Displays brief information about a list of teams and allows the user to select one for detailed view
private void displayTeamsBrief(List<Team> teams, String title) {
    JPanel teamsPanel = new JPanel(new BorderLayout());
    DefaultListModel<String> teamListModel = new DefaultListModel<>();

    for (Team team : teams) {
        teamListModel.addElement(team.getName() + " | Avg Rating: " + String.format("%.2f", team.getAverageRating()) +
                ", Chemistry: " + team.calculateChemistry() + ", Total Price: " + team.getTotalPrice() + ", Likes: " + team.getLikes());
    }

    JList<String> teamList = new JList<>(teamListModel);
    JScrollPane scrollPane = new JScrollPane(teamList);
    teamsPanel.add(scrollPane, BorderLayout.CENTER);

    int selection = JOptionPane.showConfirmDialog(this, teamsPanel, title, JOptionPane.OK_CANCEL_OPTION);
    if (selection == JOptionPane.OK_OPTION) {
        int selectedIndex = teamList.getSelectedIndex();
        if (selectedIndex != -1) {
            Team selectedTeam = teams.get(selectedIndex);
            displayTeamDetails(selectedTeam);
        }
    }
}

// Displays detailed information about a team, including its players and stats
private void displayTeamDetails(Team team) {
    StringBuilder details = new StringBuilder();
    details.append("Team Name: ").append(team.getName()).append("\n")
           .append("Total Price: ").append(team.getTotalPrice()).append("\n")
           .append("Average Rating: ").append(String.format("%.2f", team.getAverageRating())).append("\n")
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

    JOptionPane.showMessageDialog(this, details.toString(), team.getName() + " Details", JOptionPane.INFORMATION_MESSAGE);
}


    // Login Functionality
    private void login() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");

        try {
            currentUser = userManager.login(username, password);
            JOptionPane.showMessageDialog(this, "Login successful! Welcome back, " + username + "!");
            showMainPanel();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Login failed: " + e.getMessage());
        }
    }

    // Add Player Form
    private void addPlayerToTeam(String teamName) {
        String name = JOptionPane.showInputDialog(this, "Enter player name:");
        String nationality = JOptionPane.showInputDialog(this, "Enter player nationality:");
        String league = JOptionPane.showInputDialog(this, "Enter player league:");
        String clubAffiliation = JOptionPane.showInputDialog(this, "Enter player club affiliation:");
        String preferredPosition = JOptionPane.showInputDialog(this, "Enter player preferred position:");
        String currentPosition = JOptionPane.showInputDialog(this, "Enter player current position:");
        int rating = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player rating (1-100):"));
        int pace = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player pace (1-100):"));
        int passing = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player passing (1-100):"));
        int shooting = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player shooting (1-100):"));
        int dribbling = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player dribbling (1-100):"));
        int defending = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player defending (1-100):"));
        int physicality = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player physicality (1-100):"));
        int skillMoves = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player skill moves (1-5):"));
        int weakFoot = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player weak foot (1-5):"));
        int price = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter player price:"));
        boolean isInStarting11 = JOptionPane.showConfirmDialog(this, "Is player in starting 11?") == JOptionPane.YES_OPTION;

        Player player = new Player(name, nationality, league, clubAffiliation, preferredPosition, currentPosition,
                rating, pace, passing, shooting, dribbling, defending, physicality, skillMoves, weakFoot, price, isInStarting11);
        
        Team team = currentUser.getTeams().stream()
                .filter(t -> t.getName().equals(teamName))
                .findFirst()
                .orElse(new Team(teamName, "4-4-2"));
        
        team.addPlayer(player);
        currentUser.addTeam(team);
        JOptionPane.showMessageDialog(this, "Player added to " + teamName);
    }

    // Save Team Method
    private void saveTeam(String teamName) {
        Team team = currentUser.getTeams().stream()
                .filter(t -> t.getName().equals(teamName))
                .findFirst()
                .orElse(null);
        if (team != null && team.isComplete()) {
            currentUser.addTeam(team);
            repository.addTeamToCommunity(team);
            userManager.saveUsers();
            JOptionPane.showMessageDialog(this, "Team " + teamName + " saved and listed in community.");
        } else {
            JOptionPane.showMessageDialog(this, "Cannot save incomplete team!");
        }

        
    }

  
}
