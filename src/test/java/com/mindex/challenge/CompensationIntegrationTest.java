package com.mindex.challenge;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationIntegrationTest {

    private String employeeUrl;
    private String compensationUrl;
    private String compensationEmployeeIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationEmployeeIdUrl = "http://localhost:" + port + "/compensation/{employeeId}";
    }

    @Test
    public void testCreateCompensationIntegration() {
        // Given
        // TODO: Make compensation take an employee id instead of an employee
        String firstName = "Jack";
        Employee employee = createEmployee(firstName);
        BigDecimal salary = new BigDecimal("110500.05");
        LocalDate effectiveDate = LocalDate.of(2024, 5, 5);

        // When
        Compensation createdCompensation = createCompensation(employee, salary, effectiveDate);

        // Then
        assertNotNull(createdCompensation);
        assertEquals(firstName, createdCompensation.getEmployee().getFirstName());
        assertEquals(employee.getEmployeeId(), createdCompensation.getEmployee().getEmployeeId());
        assertEquals(salary, createdCompensation.getSalary());
        assertEquals(effectiveDate, createdCompensation.getEffectiveDate());
    }

    @Test
    public void testReadCompensationIntegration() {
        // Given
        String firstName = "Jack";
        Employee employee = createEmployee(firstName);
        BigDecimal salary = new BigDecimal("110500.05");
        LocalDate effectiveDate = LocalDate.of(2024, 5, 5);

        Compensation createdCompensation = createCompensation(employee, salary, effectiveDate);

        // When
        Compensation readCompensation = restTemplate.getForEntity(
                compensationEmployeeIdUrl,
                Compensation.class,
                employee.getEmployeeId()
        ).getBody();

        // Then
        assertNotNull(readCompensation);
        assertEquals(createdCompensation.getEmployee().getFirstName(), readCompensation.getEmployee().getFirstName());
        assertEquals(createdCompensation.getEmployee().getEmployeeId(), readCompensation.getEmployee().getEmployeeId());
        assertEquals(createdCompensation.getSalary(), readCompensation.getSalary());
        assertEquals(createdCompensation.getEffectiveDate(), readCompensation.getEffectiveDate());
    }

    private Compensation createCompensation(Employee employee, BigDecimal salary, LocalDate effectiveDate) {
        Compensation compensation = new Compensation(employee, salary, effectiveDate);

        return restTemplate.postForEntity(
                compensationUrl,
                compensation,
                Compensation.class
        ).getBody();
    }

    private Employee createEmployee(String firstName) {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName(firstName);

        return restTemplate.postForEntity(employeeUrl, newEmployee, Employee.class).getBody();
    }
}
