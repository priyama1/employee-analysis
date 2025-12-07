package com.bigcompany.util;

import com.bigcompany.model.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVEmployeeParser {

    public List<Employee> parseFile(String filePath) throws IOException {
        List<Employee> employees = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line;
        boolean firstLine = true;
        int lineNum = 0;

        while ((line = br.readLine()) != null) {
            lineNum++;
            if (firstLine) {
                firstLine = false;
                continue;
            }

            if (line.trim().isEmpty()) continue;

            String[] columns = line.split(",", -1);
            if (columns.length != 5) {
                br.close();
                throw new IllegalArgumentException("Bad format on line "+lineNum);
            }

            int id = Integer.parseInt(columns[0].trim());
            String firstName = columns[1].trim();
            String lastName = columns[2].trim();
            double sal = Double.parseDouble(columns[3].trim());

            Integer mgr = null;
            String mgrStr = columns[4].trim();
            if (!mgrStr.isEmpty()) {
                mgr = Integer.parseInt(mgrStr);
            }

            employees.add(new Employee(id, firstName, lastName, sal, mgr));
        }
        br.close();

        return employees;
    }
}
