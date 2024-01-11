package com.mindex.challenge;

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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureIntegrationTest {

    private String employeeUrl;
    private String reportingStructureEmployeeIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureEmployeeIdUrl = "http://localhost:" + port + "/reporting-structure/{employeeId}";
    }

    @Test
    public void testReadReportingStructureIntegration() {
        // Given
        Employee juniorEmployee1 = createEmployee("Junior");
        Employee midEmployee1 = createEmployee("Mid", juniorEmployee1);
        Employee midEmployee2 = createEmployee("Mid");
        Employee seniorEmployee1 = createEmployee("Senior", midEmployee1, midEmployee2);

        // When
        ReportingStructure readReportingStructure = restTemplate.getForEntity(
                reportingStructureEmployeeIdUrl,
                ReportingStructure.class,
                seniorEmployee1.getEmployeeId()
        ).getBody();

        // Then
        assertNotNull(readReportingStructure);
        assertEquals(3, readReportingStructure.getNumberOfReports());
        assertEquals("Senior", readReportingStructure.getEmployee().getPosition());
    }

    private Employee createEmployee(String position, Employee... directReports) {
        Employee newEmployee = new Employee();
        newEmployee.setPosition(position);

        if (directReports.length > 0) {
            List<Employee> directReportsList = Arrays.asList(directReports);
            newEmployee.setDirectReports(directReportsList);
        }

        return restTemplate.postForEntity(employeeUrl, newEmployee, Employee.class).getBody();
    }
}
