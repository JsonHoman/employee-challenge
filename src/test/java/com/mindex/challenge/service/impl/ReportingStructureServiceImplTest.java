package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest()
public class ReportingStructureServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ReportingStructureServiceImpl reportingStructureService;

    @Test
    public void getNumberOfDirectReportsTest() {
        // Given
        Employee seniorEmployee1 = new Employee();
        String employeeId = "123";
        seniorEmployee1.setEmployeeId(employeeId);

        List<Employee> senior1DirectReports = new ArrayList<>();
        Employee midEmployee1 = new Employee();
        Employee juniorEmployee1 = new Employee();
        senior1DirectReports.add(midEmployee1);
        senior1DirectReports.add(juniorEmployee1);
        seniorEmployee1.setDirectReports(senior1DirectReports);

        List<Employee> mid1DirectReports = new ArrayList<>();
        Employee juniorEmployee2 = new Employee();
        mid1DirectReports.add(juniorEmployee2);
        midEmployee1.setDirectReports(mid1DirectReports);

        Mockito.when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(seniorEmployee1);

        // When
        ReportingStructure reportingStructure = reportingStructureService.getNumberOfDirectReports(employeeId);

        // Then
        int totalSenior1DirectReports = senior1DirectReports.size() + mid1DirectReports.size();
        assertEquals(totalSenior1DirectReports, reportingStructure.getNumberOfReports());
        assertEquals(seniorEmployee1, reportingStructure.getEmployee());

        Mockito.verify(employeeRepository, Mockito.times(1)).findByEmployeeId(employeeId);
    }
}