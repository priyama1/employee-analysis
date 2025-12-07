package com.bigcompany;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeAnalysisAppTest {

    private PrintStream origOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setup() {
        origOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void restore() {
        System.setOut(origOut);
    }

    @Test
    void showsAllThreeSections() {
        EmployeeAnalysisApp.main(new String[]{});
        String s = out.toString();

        assertTrue(s.contains("Underpaid"));
        assertTrue(s.contains("Overpaid"));
        assertTrue(s.contains("Long Reporting"));
    }

    @Test
    void showsResults() {
        EmployeeAnalysisApp.main(new String[]{});
        String s = out.toString();

        // we know sample data has issues
        assertTrue(s.contains("underpaid by") || s.contains("None found"));
        assertTrue(s.contains("overpaid by") || s.contains("None found"));
    }

    @Test
    void longLinesFormat() {
        EmployeeAnalysisApp.main(new String[]{});
        String s = out.toString();

        // check new output format
        if (s.contains("number of managers between")) {
            assertTrue(s.contains("and CEO"));
        }
    }
}
