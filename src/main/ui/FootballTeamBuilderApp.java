// // FootballTeamBuilderApp.java

// package ui;


// import model.Player;
// import model.Team;
// import model.TeamRepository;
// import model.User;
// import model.UserManager;

// import java.util.List;
// import java.util.Scanner;

// /**
//  * Represents the console-based user interface for the Football Team Builder application.
//  */
// public class FootballTeamBuilderApp {

//     private Scanner scanner;
//     private User currentUser;
//     private TeamRepository repository;
//     private UserManager userManager;

//     /**
//      * EFFECTS: Initializes the application by setting up the scanner, repository, and user manager.
//      */
//     public FootballTeamBuilderApp() {
//         scanner = new Scanner(System.in);
//         repository = new TeamRepository();
//         userManager = new UserManager();

//         // Load users from file
//         userManager.loadUsers();

//         // Initialize community teams from users' listed teams
//         initializeCommunityTeams();
//     }

//     /**
//      * EFFECTS: Runs the Football Team Builder application.
//      */
    
//     @SuppressWarnings("methodlength")
//     public void run() {
//         boolean keepGoing = true;

//         System.out.println("Welcome to the Football Team Builder!");

//         while (keepGoing) {
//             if (currentUser == null) {
//                 displayAuthMenu();
//                 String command = scanner.nextLine().trim();

//                 switch (command) {
//                     case "1":
//                         signUp();
//                         break;
//                     case "2":
//                         login();
//                         break;
//                     case "3":
//                         keepGoing = false;
//                         break;
//                     default:
//                         System.out.println("Invalid selection. Please choose a valid option.");
//                 }
//             } else {
//                 displayMainMenu();
//                 String command = scanner.nextLine().trim();

//                 switch (command) {
//                     case "1":
//                         createTeam();
//                         break;
//                     case "2":
//                         viewUserTeams();
//                         break;
//                     case "3":
//                         searchTeams();
//                         break;
//                     case "4":
//                         viewPopularTeams();
//                         break;
//                     case "5":
//                         logout();
//                         break;
//                     default:
//                         System.out.println("Invalid selection. Please choose a valid option.");
//                 }
//             }
//         }

//         // Save users before exiting
//         userManager.saveUsers();

//         System.out.println("Thank you for using the Football Team Builder. Goodbye!");
//     }

//     /**
//      * EFFECTS: Initializes community teams from users' listed teams.
//      */
//     private void initializeCommunityTeams() {
//         for (User user : userManager.getAllUsers()) {
//             for (Team team : user.getTeams()) {
//                 if (team.isListed()) {
//                     repository.addTeamToCommunity(team);
//                 }
//             }
//         }
//     }

//     /**
//      * EFFECTS: Displays the authentication menu options to the user.
//      */
//     private void displayAuthMenu() {
//         System.out.println("\nAuthentication Menu:");
//         System.out.println("1. Sign Up");
//         System.out.println("2. Login");
//         System.out.println("3. Exit");
//         System.out.print("Please select an option (1-3): ");
//     }

//     /**
//      * EFFECTS: Displays the main menu options to the user.
//      */
//     private void displayMainMenu() {
//         System.out.println("\nMain Menu:");
//         System.out.println("1. Create a new team");
//         System.out.println("2. View your teams");
//         System.out.println("3. Search for teams");
//         System.out.println("4. View popular teams");
//         System.out.println("5. Logout");
//         System.out.print("Please select an option (1-5): ");
//     }

//     /**
//      * EFFECTS: Handles user signup.
//      */
//     private void signUp() {
//         System.out.print("Enter a username: ");
//         String username = scanner.nextLine().trim();

//         System.out.print("Enter a password: ");
//         String password = scanner.nextLine().trim();

//         try {
//             currentUser = userManager.signUp(username, password);
//             System.out.println("Signup successful! You are now logged in as " + username + ".");
//             userManager.saveUsers();
//         } catch (IllegalArgumentException e) {
//             System.out.println("Signup failed: " + e.getMessage());
//         }
//     }

//     /**
//      * EFFECTS: Handles user login.
//      */
//     private void login() {
//         System.out.print("Enter your username: ");
//         String username = scanner.nextLine().trim();

//         System.out.print("Enter your password: ");
//         String password = scanner.nextLine().trim();

//         try {
//             currentUser = userManager.login(username, password);
//             System.out.println("Login successful! Welcome back, " + username + ".");
//         } catch (IllegalArgumentException e) {
//             System.out.println("Login failed: " + e.getMessage());
//         }
//     }

//     /**
//      * EFFECTS: Logs out the current user.
//      */
//     private void logout() {
//         System.out.println("Logged out from user: " + currentUser.getUsername());
//         currentUser = null;
//     }

//     /**
//      * EFFECTS: Allows the user to create a new team by adding players.
//      */
//     @SuppressWarnings("methodlength")
//     private void createTeam() {
//         System.out.print("Enter a name for your team: ");
//         String teamName = scanner.nextLine().trim();

//         Team team = new Team(teamName);
//         System.out.println("\nCreating a new team...");

//         boolean addingPlayers = true;

//         while (addingPlayers && team.getPlayers().size() < 11) {
//             System.out.println("\nAdd a player to your team:");
//             Player player = createPlayer();
//             team.addPlayer(player);
//             System.out.println("Player added: " + player.getName());

//             if (team.getPlayers().size() < 23) {
//                 System.out.print("Would you like to add another player? (yes/no): ");
//                 String response = scanner.nextLine().trim().toLowerCase();
//                 if (!response.equals("yes")) {
//                     addingPlayers = false;
//                 }
//             } else {
//                 System.out.println("You have reached the maximum number of players (11).");
//             }
//         }

//         try {
//             if (team.getPlayers().size() >= 23) {
//                 System.out.print("Would you like to list this team to the community? (yes/no): ");
//                 String response = scanner.nextLine().trim().toLowerCase();
//                 if (response.equals("yes")) {
//                     team.setListed(true);
//                     repository.addTeamToCommunity(team);
//                     System.out.println("Your team has been listed to the community.");
//                 } else {
//                     System.out.println("Your team has been saved as a draft.");
//                 }
//                 currentUser.addTeam(team);
//                 userManager.saveUsers();
//             } else {
//                 System.out.println("Cannot save team: Team must have at least 11 players.");
//             }
//         } catch (Exception e) {
//             System.out.println("Error saving team: " + e.getMessage());
//         }
//     }

//     /**
//      * EFFECTS: Prompts the user for player details and creates a new Player object.
//      *
//      * @return the created Player object
//      */
//     private Player createPlayer() {
//         System.out.print("Enter player name: ");
//         String name = scanner.nextLine().trim();

//         System.out.print("Enter player country: ");
//         String country = scanner.nextLine().trim();

//         System.out.print("Enter player league: ");
//         String league = scanner.nextLine().trim();

//         System.out.print("Enter player club: ");
//         String club = scanner.nextLine().trim();

//         System.out.print("Enter player primary position: ");
//         String primaryPosition = scanner.nextLine().trim();

//         System.out.print("Enter player secondary position: ");
//         String secondaryPosition = scanner.nextLine().trim();

//         int overallRating = promptForInteger("Enter player overall rating (1-100): ", 1, 100);
//         int pace = promptForInteger("Enter player pace (1-100): ", 1, 100);
//         int shooting = promptForInteger("Enter player shooting (1-100): ", 1, 100);
//         int passing = promptForInteger("Enter player passing (1-100): ", 1, 100);
//         int skillMoves = promptForInteger("Enter player skill moves (1-5): ", 1, 5);
//         int price = promptForInteger("Enter player price (in currency units): ", 0, Integer.MAX_VALUE);

//         return new Player(name, country, league, club, primaryPosition, secondaryPosition,
//                 overallRating, pace, shooting, passing, skillMoves, price);
//     }

//     /**
//      * EFFECTS: Prompts the user for an integer within a specified range.
//      *
//      * @param prompt the prompt message to display
//      * @param min    the minimum acceptable value
//      * @param max    the maximum acceptable value
//      * @return the integer entered by the user
//      */
//     private int promptForInteger(String prompt, int min, int max) {
//         int value = 0;
//         boolean validInput = false;

//         while (!validInput) {
//             System.out.print(prompt);
//             String input = scanner.nextLine().trim();

//             try {
//                 value = Integer.parseInt(input);
//                 if (value >= min && value <= max) {
//                     validInput = true;
//                 } else {
//                     System.out.println("Please enter a value between " + min + " and " + max + ".");
//                 }
//             } catch (NumberFormatException e) {
//                 System.out.println("Invalid input. Please enter a numeric value.");
//             }
//         }

//         return value;
//     }

//     /**
//      * EFFECTS: Displays the user's teams.
//      */
//     private void viewUserTeams() {
//         List<Team> userTeams = currentUser.getTeams();

//         if (userTeams.isEmpty()) {
//             System.out.println("\nYou have no teams.");
//         } else {
//             System.out.println("\nYour Teams:");
//             int index = 1;
//             for (Team team : userTeams) {
//                 String status = team.isListed() ? "Listed" : "Draft";
//                 System.out.println(index + ". " + team.getName() + " (" + status + ")");
//                 index++;
//             }

//             // Allow the user to select a team to view details
//             selectUserTeamFromList(userTeams);
//         }
//     }

//     /**
//      * EFFECTS: Allows the user to select one of their teams to view details.
//      *
//      * @param teams the list of user's teams
//      */
//     private void selectUserTeamFromList(List<Team> teams) {
//         System.out.print("\nEnter the number of the team to view details, or 0 to return to the main menu: ");
//         int selection = promptForTeamSelection(teams.size());

//         if (selection > 0) {
//             Team selectedTeam = teams.get(selection - 1);
//             viewTeamDetails(selectedTeam);
//         }
//     }

//     /**
//      * EFFECTS: Prompts the user to select a team number from the list.
//      *
//      * @param max the maximum valid selection number
//      * @return the selected team number
//      */
//     private int promptForTeamSelection(int max) {
//         int selection = -1;
//         boolean validInput = false;

//         while (!validInput) {
//             String input = scanner.nextLine().trim();

//             try {
//                 selection = Integer.parseInt(input);
//                 if (selection >= 0 && selection <= max) {
//                     validInput = true;
//                 } else {
//                     System.out.print("Invalid selection. Please enter a number between 0 and " + max + ": ");
//                 }
//             } catch (NumberFormatException e) {
//                 System.out.print("Invalid input. Please enter a numeric value between 0 and " + max + ": ");
//             }
//         }

//         return selection;
//     }

//     /**
//      * EFFECTS: Allows the user to search for teams based on criteria.
//      */
//     private void searchTeams() {
//         System.out.println("\nSearch for Teams:");
//         int budget = promptForInteger("Enter maximum budget: ", 0, Integer.MAX_VALUE);
//         double minAverageRating = promptForDouble("Enter minimum average rating (0.0 - 100.0): ", 0.0, 100.0);
//         //int chemistry = promptForInteger("Enter minimum team chemistry (0-100): ", 0, 100);
//         System.out.print("Enter desired player name (or leave blank): ");
//         String desiredPlayerName = scanner.nextLine().trim();

//         List<Team> matchingTeams = repository.searchTeams(budget, minAverageRating, desiredPlayerName);

//         if (matchingTeams.isEmpty()) {
//             System.out.println("No teams found matching your criteria.");
//         } else {
//             System.out.println("\nTeams Found:");
//             displayTeamsBrief(matchingTeams);

//             // Allow the user to select a team to view details
//             selectTeamFromList(matchingTeams);
//         }
//     }

//     /**
//      * EFFECTS: Prompts the user for a double within a specified range.
//      *
//      * @param prompt the prompt message to display
//      * @param min    the minimum acceptable value
//      * @param max    the maximum acceptable value
//      * @return the double entered by the user
//      */
//     private double promptForDouble(String prompt, double min, double max) {
//         double value = 0.0;
//         boolean validInput = false;

//         while (!validInput) {
//             System.out.print(prompt);
//             String input = scanner.nextLine().trim();

//             try {
//                 value = Double.parseDouble(input);
//                 if (value >= min && value <= max) {
//                     validInput = true;
//                 } else {
//                     System.out.println("Please enter a value between " + min + " and " + max + ".");
//                 }
//             } catch (NumberFormatException e) {
//                 System.out.println("Invalid input. Please enter a numeric value.");
//             }
//         }

//         return value;
//     }

//     /**
//      * EFFECTS: Displays a brief list of teams with basic information.
//      *
//      * @param teams the list of teams to display
//      */
//     private void displayTeamsBrief(List<Team> teams) {
//         int index = 1;
//         for (Team team : teams) {
//             System.out.println(index + ". " + team.getName()
//                     + " | Avg Rating: " + String.format("%.2f", team.getAverageRating())
//                     + ", Chemistry: " + team.calculateChemistry()
//                     + ", Total Price: " + team.getTotalPrice()
//                     + ", Likes: " + team.getLikes());
//             index++;
//         }
//     }

//     /**
//      * EFFECTS: Allows the user to select a team from a list and view its details.
//      *
//      * @param teams the list of teams to select from
//      */
//     private void selectTeamFromList(List<Team> teams) {
//         System.out.print("\nEnter the number of the team to view details, or 0 to return to the main menu: ");
//         int selection = promptForTeamSelection(teams.size());

//         if (selection > 0) {
//             Team selectedTeam = teams.get(selection - 1);
//             viewTeamDetails(selectedTeam);
//         }
//     }

//     /**
//      * EFFECTS: Displays detailed information about a team and allows the user to like the team.
//      *
//      * @param team the team to display
//      */
//     private void viewTeamDetails(Team team) {
//         boolean viewingTeam = true;

//         while (viewingTeam) {
//             System.out.println("\nTeam Details:");
//             displayTeamDetails(team);

//             System.out.println("\nOptions:");
//             System.out.println("1. Like this team");
//             System.out.println("2. Return to previous menu");
//             System.out.print("Please select an option (1-2): ");

//             String command = scanner.nextLine().trim();

//             switch (command) {
//                 case "1":
//                     team.likeTeam();
//                     System.out.println("You have liked this team. Total likes: " + team.getLikes());
//                     break;
//                 case "2":
//                     viewingTeam = false;
//                     break;
//                 default:
//                     System.out.println("Invalid selection. Please choose a valid option.");
//             }
//         }
//     }

//     /**
//      * EFFECTS: Displays detailed information about a team, including players and stats.
//      *
//      * @param team the team to display
//      */
//     private void displayTeamDetails(Team team) {
//         System.out.println("Team Name: " + team.getName());
//         System.out.println("Players:");
//         for (Player player : team.getPlayers()) {
//             System.out.println("- " + player.getName() + " (" + player.getCurrentPosition() + ")");
//         }
//         System.out.println("Total Price: " + team.getTotalPrice());
//         System.out.println("Average Rating: " + String.format("%.2f", team.getAverageRating()));
//         System.out.println("Team Chemistry: " + team.calculateChemistry());
//         System.out.println("Likes: " + team.getLikes());
//     }

//     /**
//      * EFFECTS: Displays the teams sorted by popularity and allows the user to select one to view details.
//      */
//     private void viewPopularTeams() {
//         List<Team> popularTeams = repository.getTeamsByPopularity();

//         if (popularTeams.isEmpty()) {
//             System.out.println("\nNo teams available in the community.");
//         } else {
//             System.out.println("\nTeams by Popularity:");
//             displayTeamsBrief(popularTeams);

//             // Allow the user to select a team to view details
//             selectTeamFromList(popularTeams);
//         }
//     }

//     /**
//      * Main method to start the application.
//      *
//      * @param args command-line arguments (not used)
//      */
  
// }
