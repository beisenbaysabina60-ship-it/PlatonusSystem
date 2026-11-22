# PlatonusSystem

## Project Description
PlatonusSystem is a university academic management system developed in Java. The system manages students, teachers, courses, student groups, lessons, attendance, and grades.

## Features
- Student management (CRUD)
- Teacher management (CRUD)
- Course management (CRUD)
- Student group management
- Lesson management
- Attendance management
- Grade management
- Grade statistics and GPA calculation
- CSV export
- JSON export
- Multithreading
- SQLite database
- Automated tests using JUnit 5

## Technologies
- Java 17
- Maven
- SQLite
- JDBC
- JUnit 5
- Git & GitHub

## Project Structure
```text
src/
├── main/
│   └── java/
│       └── kz/platonus/
│           ├── model/
│           ├── dao/
│           ├── service/
│           ├── database/
│           ├── export/
│           └── concurrency/
└── test/
    └── java/
```

## Database
The application uses SQLite for data storage.

## Testing
The project contains **140 automated tests**.

Test results:
- Tests run: 140
- Failures: 0
- Errors: 0
- Skipped: 0

All tests passed successfully.

## How to Run

### Requirements
- JDK 17+
- Maven

### Run tests
```bash
mvn clean test
```

### Run the application
```bash
mvn exec:java
```

## Team Project
Developed as a university Java project using GitHub for version control and collaboration.

## Repository
GitHub: https://github.com/beisenbaysabina60-ship-it/PlatonusSystem
