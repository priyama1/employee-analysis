package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.util.CSVEmployeeParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvEmployeeParserTest {

    @TempDir Path tmp;

    Path write(String name, String content) throws IOException {
        Path p = tmp.resolve(name);
        Files.write(p, content.getBytes());
        return p;
    }

    @Test
    void parsesValidFile() throws Exception {
        String csv = "Id,firstName,lastName,salary,managerId\n" +
                "1,John,Doe,50000,\n" +
                "2,Jane,Smith,40000,1\n";
        Path file = write("valid.csv", csv);

        List<Employee> list = new CSVEmployeeParser().parseFile(file.toString());

        assertEquals(2, list.size());
        assertTrue(list.get(0).isCeo());
        assertEquals("Jane", list.get(1).getFirstName());
        assertEquals(Integer.valueOf(1), list.get(1).getManagerId());
    }

    @Test
    void handlesWhitespaceAndEmptyLines() throws Exception {
        String csv = "Id,firstName,lastName,salary,managerId\n" +
                "  1 , Bob , Lee , 60000.50 ,\n" +
                "\n" +  // empty line
                "2,Sue,Kim,45000,1\n";
        Path file = write("ws.csv", csv);

        List<Employee> list = new CSVEmployeeParser().parseFile(file.toString());

        assertEquals(2, list.size());
        assertEquals("Bob", list.getFirst().getFirstName());
        assertEquals(60000.50, list.getFirst().getSalary(), 0.01);
    }

    @Test
    void throwsOnBadData() throws Exception {
        // wrong column count
        Path bad1 = write("bad1.csv", "Id,firstName,lastName,salary,managerId\n1,X,Y,100\n");
        assertThrows(Exception.class, () -> new CSVEmployeeParser().parseFile(bad1.toString()));

        // invalid number
        Path bad2 = write("bad2.csv", "Id,firstName,lastName,salary,managerId\nXX,A,B,100,\n");
        assertThrows(Exception.class, () -> new CSVEmployeeParser().parseFile(bad2.toString()));
    }

    @Test
    void throwsOnMissingFile() {
        assertThrows(IOException.class,
                () -> new CSVEmployeeParser().parseFile("/does/not/exist.csv"));
    }

    @Test
    void emptyFileReturnsEmptyList() throws Exception {
        Path file = write("empty.csv", "Id,firstName,lastName,salary,managerId\n");
        List<Employee> list = new CSVEmployeeParser().parseFile(file.toString());
        assertTrue(list.isEmpty());
    }
}
