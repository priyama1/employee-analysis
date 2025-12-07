# Employee Analysis

Java program that reads employee data from CSV and identifies salary/reporting issues.

## What It Does

Reads a CSV file and reports:

- Managers earning less than they should (and by how much)
- Managers earning more than they should (and by how much)
- Employees with reporting lines too long (and by how much)

## Business Rules

- Managers must earn at least **20% more** than the average salary of their direct reports
- Managers must earn no more than **50% more** than the average salary of their direct reports
- Employees should have at most **4 managers** between them and the CEO

## Requirements

- Java 8+
- Maven 3.6+

## Build & Run

```bash
# build
mvn clean package

# run tests
mvn test

# run the app
java -jar target/employee-analysis-1.0.0.jar path/to/employees.csv
```

## CSV Format

```
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
```

- First row is header (skipped)
- CEO has empty managerId field
- IDs must be unique

## Sample Output

```
Loaded 5 employees

-- Underpaid Managers --
(should earn at least 20% more than avg of direct reports)

  Martin Chekov (ID 124): underpaid by 15000.00

-- Overpaid Managers --
(should earn no more than 50% above avg of direct reports)

None found.

-- Long Reporting Lines --
(max 4 managers between employee and CEO)

None found.
```

## Assumptions

1. CSV has header row (always skipped)
2. Exactly one CEO exists (employee with no manager)
3. All IDs are unique integers
4. Salaries can be decimal values
5. Whitespace in fields is trimmed
6. Empty lines are skipped
7. "Reporting line length" = number of managers between employee and CEO
8. Manager = any employee with at least one direct report
9. Salary rules only apply to managers (employees with subordinates)

## Project Structure

```
src/main/java/com/bigcompany/
├── EmployeeAnalysisApp.java    # entry point
├── model/Employee.java         # data class
├── service/OrganizationAnalyzer.java  # business logic
└── util/CsvEmployeeParser.java # file parsing

src/test/java/com/bigcompany/
├── EmployeeTest.java
├── CsvEmployeeParserTest.java
└── OrganizationAnalyzerTest.java
```
