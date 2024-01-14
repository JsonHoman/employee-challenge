package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest()
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    public void createTest() {
        // Given
        Employee employee = new Employee();
        Mockito.when(employeeRepository.insert(Mockito.any(Employee.class))).thenReturn(employee);

        // When
        Employee createdEmployee = employeeService.create(employee);

        // Then
        assertNotNull(createdEmployee);
        UUID.fromString(createdEmployee.getEmployeeId());
        assertEquals(employee, createdEmployee);
    }

    @Test
    public void readTest() {
        // Given
        Employee employee = new Employee();
        Mockito.when(employeeRepository.findByEmployeeId(Mockito.any(String.class))).thenReturn(employee);

        // When
        Employee readEmployee = employeeService.read("id");

        // Then
        assertNotNull(readEmployee);
        assertEquals(employee, readEmployee);
    }

    @Test
    public void readTestEmployeeNull() {
        // Given
        Mockito.when(employeeRepository.findByEmployeeId(Mockito.any(String.class))).thenReturn(null);

        // When/Then
        assertThrows(RuntimeException.class, () -> employeeService.read("id"));
    }

    @Test
    public void updateTest() {
        // Given
        Employee employee = new Employee();
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        // When
        Employee updatedEmployee = employeeService.update(employee);

        // Then
        assertNotNull(updatedEmployee);
        assertEquals(employee, updatedEmployee);
    }
}