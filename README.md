# API Test Automation Project

## 🚀 Local Execution

### 0. Prerequisites
Before running the project, make sure you have the following installed:

Java 25 (required)

### 1. Configure API Key
Fill in api-key in file : `src/main/resources/config.properties`
``` properties
api.key=your-api-key-here
```
### 2. Build the project
``` bash
./gradlew clean build -x test
```

### 3. Run tests
``` bash
./gradlew clean test --continue allureReport

```

### Allure Report

After running tests, the Allure report is available at:
```
build/reports/allure-report/allureReport/index.html
```