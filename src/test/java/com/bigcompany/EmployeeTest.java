package com.bigcompany;

import com.bigcompany.model.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void basicFields() {
        Employee e = new Employee(42, "Alice", "Wong", 75000, 10);

        assertEquals(42, e.getId());
        assertEquals("Alice", e.getFirstName());
        assertEquals("Wong", e.getLastName());
        assertEquals("Alice Wong", e.getFullName());
        assertEquals(75000, e.getSalary(), 0.01);
        assertEquals(Integer.valueOf(10), e.getManagerId());
    }

    @Test
    void ceoCheck() {
        Employee ceo = new Employee(1, "Jane", "CEO", 500000, null);
        Employee mgr = new Employee(2, "Bob", "Mgr", 80000, 1);

        assertTrue(ceo.isCeo());
        assertNull(ceo.getManagerId());
        assertFalse(mgr.isCeo());
    }

    @Test
    void equalsAndHash() {
        Employee a = new Employee(99, "X", "Y", 1000, 1);
        Employee b = new Employee(99, "Z", "W", 2000, 2); // same id
        Employee c = new Employee(100, "X", "Y", 1000, 1); // diff id

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
