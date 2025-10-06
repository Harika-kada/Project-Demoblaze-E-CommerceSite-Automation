# Demoblaze E-Commerce Site Automation Framework

This project provides a comprehensive set of automated tests for the Demoblaze E-commerce Website (https://www.demoblaze.com/). The automation suite is designed to ensure the stability and functionality of key user journeys, including user registration, login, product browsing, adding items to the cart, and placing an order. It is built following the Page Object Model (POM) design pattern to ensure test code is clean, reusable, and easy to maintain.

## Features

The framework is designed for efficiency and maintainability, incorporating several best practices:

- Page Object Model (POM): Separates test logic from element locators for maintainability.
- TestNG: Used as the test execution framework, supporting parallel execution and flexible test suite configuration.
- User Management: Test cases for user registration, successful login, and invalid login attempts.
- Data-Driven Testing: Utilizes Apache POI to read test data directly from Excel spreadsheets (testdata.xlsx).
- WebDriver Management: Uses WebDriverManager to handle browser driver setup automatically.
- Cross-Browser Support: Easily switch between Chrome and Firefox via configuration.
- Failure Management: Automatically captures screenshots on test failure for quick debugging.
- Reporting: Integrated with detailed logging and produces comprehensive TestNG HTML Reports for execution analysis.

## Tech Stack & Dependencies

The core technologies and libraries used in this project are:

- Language - Java
- Framework - TestNG
- Automation Tool - Selenium WebDriver
- Data Handling - Apache POI for data reading from Excel
- Build Tool - Maven
- IDE - Used IntelliJ IDEA
- Reporting	- Extent Reports, Allure, built-in framework reports

## Prerequisites

You need the following software installed on your machine:

- Java Development Kit (JDK) 11+
- Apache Maven 3.6 or higher
- Web Browser: Chrome, Firefox, or Edge
- A modern IDE (e.g., IntelliJ IDEA, Eclipse).

## Installation & Setup

### Step 1 - Clone the Repository:
- git clone https://github.com/Harika-kada/Project-Demoblaze-E-CommerceSite-Automation.git
- cd Project-Demoblaze-E-CommerceSite-Automation

### Step 2 - Install Maven Dependencies:
Open your terminal in the project root directory and run:
- mvn clean install

### Step 3 - Configuration
Before running the tests, customize the src/resources/config.properties file for desired browser and URL.:

- browser=chrome #Options: chrome, firefox
- url=https://www.demoblaze.com/
- defaultTimeout=10

You can also update the test data in the Excel file: src/resources/testdata.xlsx

## Project Structure

Project-Demoblaze-E-CommerceSite-Automation/

├── src/
│   ├── main/java/
│   │   ├── pages/              # Page classes
│   │   ├── utils/              # Helper classes
│   │   ├── managers/           # Driver management
│   │   └── data/               # Data providers
│   │
│   └── test/java/
│       ├── tests/              # Test classes
│       ├── listeners/          # TestNG listeners
│       └── suites/             # Test suites
│
├── resources/
│   ├── config.properties       # Configuration file
│   ├── testdata.xlsx           # Test data
│   └── testng.xml              # TestNG configuration
│
└── output/
│   ├── test-output/            # Test reports
│   ├── screenshots/            # Failure and Passed screenshots
│   └── logs/                   # Execution logs
└── pom.xml                     # Maven dependencies and build configuration

## How to Run Tests

1. The primary way to execute the tests is by using maven command, pointing to the testng.xml file:
   - mvn test
2. Use `testng.xml` to run the test suite and also there are other xml suites to run specific groups, classes, or set parameters.

## Test Reporting
Upon completion of the test execution, reports will be generated in the following directory:

1. Custom HTML reports: Saved in output/test-output/custom-report and open the file in the folder in the web browser.
2. Allure reports: After execution, use the following commands to generate reports and will be saved in target/allure-report
   - mvn allure:report
   - mvn allure:serve
3. Other reports: Generated in target/surefire-reports

## Screenshots & Logs
Upon completion of the test execution, following will be generated with date and time.
- Screenshots: All test screenshots(Pass/Fail) are saved in output/screenshots
- Logs: Execution logs are saved in output/logs/

## Author

- Name: Harika Kada
- Contact: harikakada10@gmail.com