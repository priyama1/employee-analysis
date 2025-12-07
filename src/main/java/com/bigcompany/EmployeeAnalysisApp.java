package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.service.OrganizationAnalyzer;
import com.bigcompany.util.CSVEmployeeParser;

import java.util.List;
import java.util.Map;

public class EmployeeAnalysisApp {

    private static final String CSV_FILE = "src/main/resources/employee.csv";

    public static void main(String[] args) {

        try {

            CSVEmployeeParser parser = new CSVEmployeeParser();
            List<Employee> employees = parser.parseFile(CSV_FILE);

            System.out.println("Loaded " + employees.size()+ " employees.");
            System.out.println();

            OrganizationAnalyzer analyzer = new OrganizationAnalyzer(employees);

            System.out.println("<------------------- Underpaid Managers -------------------->");
            System.out.println("{ Should earn at least 20% more than avg of direct reports. }");
            System.out.println();
            Map<Employee, Double> underpaid = analyzer.findUnderpaidManagers();
            if (underpaid.isEmpty()) {
                System.out.println("No managers are underpaid.");
            }else {
                for (Map.Entry<Employee, Double> entry : underpaid.entrySet()) {
                    System.out.printf(" %s (ID %d): underpaid by %.2f%n",
                            entry.getKey().getFullName(),
                            entry.getKey().getId(),
                            entry.getValue());
                }
            }
            System.out.println();

            System.out.println("<------------------- Overpaid Managers -------------------->");
            System.out.println("{ Should earn no more than 50% above avg of direct reports. }");
            System.out.println();
            Map<Employee, Double> overpaid = analyzer.findOverpaidManagers();
            if (overpaid.isEmpty()) {
                System.out.println("No overpaid managers found.");
            }else {
                for (Map.Entry<Employee, Double> entry: overpaid.entrySet()) {
                    System.out.printf(" %s (ID %d): overpaid by %.2f%n",
                            entry.getKey().getFullName(),
                            entry.getKey().getId(),
                            entry.getValue());
                }
            }
            System.out.println();

            System.out.println("<------------------- Long Reporting Lines -------------------->");
            System.out.println("{ Max 4 managers between employee and CEO. }");
            System.out.println();

            Map<Employee, Integer> longLines = analyzer.findEmployeesWithLongReportingLine();
            if (longLines.isEmpty()) {
                System.out.println("None found");
            }else {
                for (Map.Entry<Employee, Integer> entry : longLines.entrySet()) {
                    int totalManagers = entry.getValue() + 4;
                    System.out.printf(" There are %d number of managers between %s (ID %d) and CEO.%n",
                            totalManagers,
                            entry.getKey().getFullName(),
                            entry.getKey().getId());
                }
            }


            System.out.println("----------------------------");
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
