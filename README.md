# GroupSync - Social Networking Application

GroupSync is a Java-based desktop application that allows users to connect with others based on shared interests. Users can create profiles, join groups, and communicate with other members who share similar interests.

## Features

- **User Authentication**: Create an account and log in securely
- **Interest Management**: Add and manage interests to personalize your profile
- **Group System**: Join existing groups or create your own based on shared interests
- **Messaging**: Communicate with group members through a messaging system
- **Group Discovery**: Find new groups that align with your interests

## Technology Stack

- **Frontend**: JavaFX
- **Backend**: Java
- **Database**: H2 Database with Hibernate ORM
- **Build Tool**: Gradle

## Team Members

- [Shristi Khadka]
- [Lilly Waterman]
- [Yahaya Fofana]
- [Kyle Dille]

## Project Setup

### Prerequisites

- Java 11 or higher
- Gradle 7.0 or higher
- JavaFX 17 or higher

### Installation

1. Clone the repository:
2. Build the project:
3. Run the application:

### Accessing the Database
The application uses an embedded H2 database that initializes automatically on first run. To view or manage the database during development:

H2 Console becomes available at http://localhost:8082 when the application is running(in the console)
Connect using JDBC URL: jdbc:h2:./groupsyncdb

### Architecture
GroupSync follows the MVC (Model-View-Controller) architecture:

Model: Entity classes representing users, groups, interests, and messages
View: JavaFX UI components for different screens and features
Controller: Business logic handling user actions and application flow
Service Layer: Connection between controllers and data access
DAO Layer: Database operations with Hibernate ORM

### Project Status
Completed (Iterations 1 & 2)

User registration and login with validation
Database integration and entity relationships
User profile management
Group browsing and joining
In-group messaging
Interest-based group recommendations
Group creation with interest tagging

Upcoming (Iteration 3)

Direct messaging between users
Group content moderation
Group categorization and organization
Mobile-responsive design
Push notifications

### Testing
We've implemented JUnit tests for core functionality with a focus on the "one assert per test" principle to ensure precise test coverage and clear failure diagnosis.