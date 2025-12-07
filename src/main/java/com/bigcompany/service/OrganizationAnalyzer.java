package com.bigcompany.service;

import com.bigcompany.model.Employee;

import java.util.*;

public class OrganizationAnalyzer {

    private Map<Integer, Employee> empById = new HashMap<>();
    private Map<Integer, List<Employee>> directReports = new HashMap<>();
    private Employee ceo;

    public OrganizationAnalyzer(List<Employee> employees) {

        for (Employee e : employees) {
            if (empById.put(e.getId(), e) != null)
                throw new IllegalArgumentException("duplicate id" + e.getId());

            if (e.isCeo()) {
                if (ceo != null) throw new IllegalArgumentException("multiple CEOs");
                ceo = e;
            }
        }

        if (ceo == null) {
            throw new IllegalArgumentException("no CEO found");
        }

        for (Employee e : employees) {
            if (e.getManagerId() != null) {
                directReports.computeIfAbsent(e.getManagerId(), k -> new ArrayList<>()).add(e);
            }
        }
    }

    public Map<Employee, Double> findUnderpaidManagers() {

        Map<Employee, Double> result = new HashMap<>();

        for (Integer mgrId : directReports.keySet()) {
            Employee mgr = empById.get(mgrId);
            if (mgr == null) continue;

            List<Employee> team = directReports.get(mgrId);
            double avg = avgSalary(team);
            double minRequired = avg * 1.2;

            if (mgr.getSalary() < minRequired) {
                double diff = minRequired - mgr.getSalary();
                result.put(mgr, diff);
            }
        }
        return result;
    }

    public Map<Employee, Double> findOverpaidManagers() {
        Map<Employee, Double> result = new HashMap<>();

        for (Integer mgrId : directReports.keySet()) {
            Employee mgr = empById.get(mgrId);
            if (mgr == null) continue;

            List<Employee> team = directReports.get(mgrId);
            double avg = avgSalary(team);
            double maxAllowed = avg * 1.5;

            if (mgr.getSalary() > maxAllowed) {
                double diff = mgr.getSalary() - maxAllowed;
                result.put(mgr, diff);
            }
        }
        return result;
    }

    public Map<Employee, Integer> findEmployeesWithLongReportingLine() {
        Map<Employee, Integer> result = new HashMap<>();

        for (Employee emp : empById.values()) {
            if (emp.isCeo()) continue;

            int managerBetween = countManagersBetween(emp);

            if (managerBetween > 4) {
                result.put(emp, managerBetween -4);
            }
        }
        return result;
    }

    private int countManagersBetween(Employee emp) {
        int count = 0;
        Set<Integer> seen = new HashSet<>();
        Integer mgrId = emp.getManagerId();

        while (mgrId != null) {
            if (!seen.add(mgrId)) {
                throw new IllegalStateException("same manager repeated for emp "+emp.getId());
            }

            Employee mgr = empById.get(mgrId);
            if (mgr == null) {
                throw new IllegalStateException("missing manager "+mgrId);
            }

            if(mgr.isCeo()) {
                break;
            }

            count++;
            mgrId = mgr.getManagerId();
        }

        return count;
    }

    private double avgSalary(List<Employee> list) {
        double sum = 0;
        for (Employee e : list) sum += e.getSalary();
        return !list.isEmpty() ? sum / list.size() : 0;
    }
}
