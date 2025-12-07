package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.service.OrganizationAnalyzer;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationAnalyzerTest {

    // helper to create test org
    List<Employee> org(Employee... emps) {
        return Arrays.asList(emps);
    }

    @Test
    void underpaid() {
        // mgr at 45k, team avg 40k, needs 48k min
        List<Employee> emps = org(
                new Employee(1, "CEO", "X", 100000, null),
                new Employee(2, "Mgr", "Y", 45000, 1),
                new Employee(3, "Dev", "A", 40000, 2),
                new Employee(4, "Dev", "B", 40000, 2)
        );

        OrganizationAnalyzer a = new OrganizationAnalyzer(emps);
        Map<Employee, Double> res = a.findUnderpaidManagers();

        assertEquals(1, res.size());
        assertEquals(3000, res.get(emps.get(1)), 1);
    }

    @Test
    void overpaid() {
        // mgr at 70k, team avg 40k, max is 60k
        List<Employee> emps = org(
                new Employee(1, "CEO", "X", 100000, null),
                new Employee(2, "Mgr", "Y", 70000, 1),
                new Employee(3, "Dev", "A", 40000, 2),
                new Employee(4, "Dev", "B", 40000, 2)
        );

        OrganizationAnalyzer a = new OrganizationAnalyzer(emps);
        Map<Employee, Double> res = a.findOverpaidManagers();

        assertEquals(1, res.size());
        assertEquals(10000, res.get(emps.get(1)), 1);
    }

    @Test
    void atThresholdsIsOk() {
        // exactly at 20% min and 50% max should not flag
        List<Employee> emps = org(
                new Employee(1, "CEO", "X", 72000, null),
                new Employee(2, "Mgr", "Y", 48000, 1), // exactly 20% above 40k
                new Employee(3, "Dev", "A", 40000, 2),
                new Employee(4, "Dev", "B", 40000, 2)
        );

        OrganizationAnalyzer a = new OrganizationAnalyzer(emps);
        assertTrue(a.findUnderpaidManagers().isEmpty());
        assertTrue(a.findOverpaidManagers().isEmpty());
    }

    @Test
    void longChain() {
        // CEO -> L1 -> L2 -> L3 -> L4 -> L5 -> Worker
        // Worker has 5 mgrs between (L5,L4,L3,L2,L1), over by 1
        List<Employee> emps = org(
                new Employee(1, "CEO", "X", 200000, null),
                new Employee(2, "L1", "M", 150000, 1),
                new Employee(3, "L2", "M", 120000, 2),
                new Employee(4, "L3", "M", 100000, 3),
                new Employee(5, "L4", "M", 80000, 4),
                new Employee(6, "L5", "M", 60000, 5),
                new Employee(7, "Worker", "W", 40000, 6)
        );

        OrganizationAnalyzer a = new OrganizationAnalyzer(emps);
        Map<Employee, Integer> res = a.findEmployeesWithLongReportingLine();

        assertEquals(1, res.size());
        assertEquals(1, res.get(emps.get(6)).intValue()); // 5-4=1
    }

    @Test
    void exactly4MgrsOk() {
        // 4 managers between worker and CEO is allowed
        List<Employee> emps = org(
                new Employee(1, "CEO", "X", 200000, null),
                new Employee(2, "L1", "M", 150000, 1),
                new Employee(3, "L2", "M", 120000, 2),
                new Employee(4, "L3", "M", 100000, 3),
                new Employee(5, "L4", "M", 80000, 4),
                new Employee(6, "Worker", "W", 60000, 5)
        );

        OrganizationAnalyzer a = new OrganizationAnalyzer(emps);
        assertTrue(a.findEmployeesWithLongReportingLine().isEmpty());
    }

    @Test
    void ceoOnly() {
        List<Employee> emps = org(new Employee(1, "Solo", "CEO", 100000, null));
        OrganizationAnalyzer a = new OrganizationAnalyzer(emps);

        assertTrue(a.findUnderpaidManagers().isEmpty());
        assertTrue(a.findOverpaidManagers().isEmpty());
        assertTrue(a.findEmployeesWithLongReportingLine().isEmpty());
    }

    @Test
    void validation() {
        // dup id
        assertThrows(Exception.class, () -> new OrganizationAnalyzer(org(
                new Employee(1, "A", "B", 100000, null),
                new Employee(1, "C", "D", 50000, 1)
        )));

        // two ceos
        assertThrows(Exception.class, () -> new OrganizationAnalyzer(org(
                new Employee(1, "CEO1", "X", 100000, null),
                new Employee(2, "CEO2", "Y", 100000, null)
        )));

        // no ceo
        assertThrows(Exception.class, () -> new OrganizationAnalyzer(org(
                new Employee(1, "A", "B", 50000, 2),
                new Employee(2, "C", "D", 50000, 1)
        )));
    }

    @Test
    void exampleFromSpec() {
        List<Employee> emps = org(
                new Employee(123, "Joe", "Doe", 60000, null),
                new Employee(124, "Martin", "Chekov", 45000, 123),
                new Employee(125, "Bob", "Ronstad", 47000, 123),
                new Employee(300, "Alice", "Hasacat", 50000, 124),
                new Employee(305, "Brett", "Hardleaf", 34000, 300)
        );

        OrganizationAnalyzer a = new OrganizationAnalyzer(emps);

        // Martin underpaid: team avg 50k, min 60k, has 45k -> short 15k
        Map<Employee, Double> under = a.findUnderpaidManagers();
        assertEquals(1, under.size());
        assertEquals(15000, under.get(emps.get(1)), 1);

        assertTrue(a.findOverpaidManagers().isEmpty());
        assertTrue(a.findEmployeesWithLongReportingLine().isEmpty());
    }
}
