# FIFA Ultimate Team Manager

## Project Proposal

The **FIFA Ultimate Team Manager** application allows users to create and manage a collection of 23 players, adhering to the standard FIFA Ultimate Team roster of 11 starters and 12 substitutes. Each player profile includes detailed information such as *name*, *nationality*, *rating*, *position*, and specific stats like *pace*, *passing*, *shooting*, *weak foot*, and *club affiliation*. Users can assign a *price* to each player, enabling the program to **calculate the total price** of the team based on individual player costs.

Additionally, the application offers a search functionality where users can input their **budget**, specify **desired players**, and set criteria for **team rating** and **chemistry**. The program then filters and displays teams that match these preferences, ensuring that users can find a reliable and optimized team without the need for extensive experimentation.

Furthermore, the application includes a **popularity feature** where users can **like** their favorite team builds. This allows users to **compare teams based on how popular they are**, showcasing the most liked and highly rated team configurations within the community. This feature is especially beneficial for new FIFA players who want to build a competitive team quickly and efficiently without incurring unnecessary transfer fees or risking poor team performance.

### **Key Features:**

- **User Authentication:**
  - **Sign Up**: Users can create an account with a unique username and password.
  - **Log In**: Users can log in to access their personal teams and preferences.
  - Credentials are securely stored in a JSON format in a flat file.

- **Create and Save Teams:**
  - Assemble a roster of 23 players with comprehensive profiles.
  - Assign prices to each player to manage and calculate the team's total cost.
  - Teams are stored under the user's profile.

- **Community Teams Repository:**
  - Users can choose to list their teams publicly.
  - All community teams are stored in a separate repository accessible to all users.

- **Search and Filter Teams:**
  - Input budget constraints and specific player preferences.
  - Filter teams based on total price, average rating, and chemistry score.

- **Popularity and Likes:**
  - **Like** favorite team builds to express preference.
  - **Compare** teams based on the number of likes to determine popularity.

- **Automated Calculations:**
  - **Total Price:** Sum of individual player prices.
  - **Average Rating:** Average rating of all 23 players.
  - **Total Chemistry:** Sum of chemistry points for all players.

**Who will use it?**

- **FIFA Ultimate Team Players:** Ideal for those who prefer a *tried-and-tested* team setup without the hassle of manually mixing and matching players.

**Why is this project of interest to you?**

- As a **gaming and football enthusiast**, I have a passion for FIFA. Each year, when the game is released, players seek reliable starter teams or aim to experiment with new player combinations. Manually adjusting teams can lead to high transfer fees, potential losses due to poor team performance, and wasted time. **FIFA Ultimate Team Manager** simplifies this process by providing optimized team suggestions based on user-defined budgets and preferences, saving both time and resources. The added popularity feature allows users to identify and adopt the most favored team builds within the community, further enhancing their gaming experience.

### User Stories

- **As a user, I want to be able to sign up and log in to my personal account** so that my teams and preferences are securely stored and accessible.

- **As a user, I want to have my credentials securely stored in a file** so that my personal information is protected.

- **As a user, I want to be able to create and post a new FIFA team** so that I can share my team composition with others.

- **As a user, I want my teams to be saved under my user profile** so that I can manage and edit them whenever I log in.

- **As a user, I want to be able to view a list of all posted teams** so that I can browse and compare different team setups.

- **As a user, I want to be able to like my favorite team builds** so that I can express my preference for certain team configurations.

- **As a user, I want to be able to search for teams based on my budget, desired players, average rating, and chemistry score** so that I can find a team that fits my specific preferences.

- **As a user, I want to be able to view the popularity of each team based on the number of likes it has received** so that I can identify and choose the most favored team builds.

- **As a user, I want to be able to filter teams based on specific player attributes or positions** so that I can find teams that match my playing style.

- **As a user, I want to be able to rate and review posted teams** so that I can provide feedback to other users and help improve team selections.

- **As a user, I want to have the option to save my personal teams and preferences to a file** so that I can preserve my progress and resume later.

- **As a user, I want to have the option to load my personal teams and preferences from a file** so that I can continue from where I left off.

- **As a user, I want to be able to fetch and load all community teams from the community repository** so that I can view and interact with the latest shared teams.

- **As a user, I want to have the option to save the current state of the application** so that I can ensure all my recent changes are stored.

- **As a user, I want to be able to load the entire application state from a file** so that I can resume exactly where I left off at an earlier time.

---

**Note:** The application securely stores user credentials and team data in JSON format within flat files. Personal teams and their statuses are stored within each user's profile (`users.json`), while community teams are stored in a separate repository (`community_teams.json`) accessible to all users.

---
