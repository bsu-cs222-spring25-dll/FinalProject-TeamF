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
- [Yayaha Fofana]
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

### Database Setup

The application uses an embedded H2 database which is automatically initialized on first run. No additional database setup is required.

- The H2 Console is available at `http://localhost:8082` during application runtime
- Default system user: username = `system`, password = `systempassword`

## Development Notes

### Suppressed Warnings

- `@SuppressWarnings("ALL")` in Message.java: This suppression is used because the class contains auto-generated code from Hibernate, and we want to avoid warnings about unused fields and methods that might be used by the framework.

## Project Status

This is the first iteration of the GroupSync application. Currently implemented features:
- User registration and login and logout
- Basic database schema for users, interests, groups, and messages
- Initial user interface for authentication
- Join groups and search groups
- Ability to view joined groups
- In-group messaging system

Upcoming features:
- Group creation and management
- User profile management
- Interest-based group recommendations
