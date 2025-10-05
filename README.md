# Demoblaze Automation Framework

This is a Selenium-based automation framework for testing the Demoblaze e-commerce site. It uses the Page Object Model design pattern with TestNG for execution and reporting.

## Features

- Page Object Model for maintainability
- TestNG for parallel execution and reporting
- Data-driven testing using Excel
- Cross-browser support (Chrome & Firefox)
- Screenshot capture on test failure
- Logging and detailed HTML reports

## 📁 Project Structure

src/ 
├── main/java/ 
│ ├── pages/ 
│ ├── utils/ 
│ ├── managers/ 
│ └── data/ 
├── test/java/ 
│ ├── tests/ 
│ ├── listeners/ 
│ └── suites/ resources/ 
├── config.properties 
├── testdata.xlsx 
├── testng.xml output/ 
├── test-output/ 
├── screenshots/ 
└── logs/

## 🧪 How to Run Tests

1. Update `config.properties` with desired browser and URL.
2. Use `testng.xml` to run the test suite.
3. View reports in `output/test-output`.

## 🧰 Tools & Dependencies

- Selenium WebDriver
- TestNG
- Apache POI
- WebDriverManager

## 📸 Screenshots & Logs

- Screenshots saved in `output/screenshots`
- Logs saved in `output/logs`

## 📦 Author

Harika Kada 
kadahari@amazon.com