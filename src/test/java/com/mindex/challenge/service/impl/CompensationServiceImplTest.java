package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest()
public class CompensationServiceImplTest {

    @Mock
    private CompensationRepository compensationRepository;

    @InjectMocks
    private CompensationServiceImpl compensationService;

    @Test
    public void createTest() {
        // Given
        Employee employee = new Employee();
        BigDecimal salary = new BigDecimal("110500.05");
        LocalDate effectiveDate = LocalDate.of(2024, 5, 5);

        Compensation compensation = new Compensation(employee, salary, effectiveDate);

        Mockito.when(compensationRepository.insert(Mockito.any(Compensation.class))).thenReturn(compensation);

        // When
        Compensation createdCompensation = compensationService.create(compensation);

        // Then
        assertNotNull(createdCompensation);
        assertEquals(compensation, createdCompensation);
    }

    @Test
    public void readTest() {
        // Given
        Employee employee = new Employee();
        BigDecimal salary = new BigDecimal("110500.05");
        LocalDate effectiveDate = LocalDate.of(2024, 5, 5);

        Compensation compensation = new Compensation(employee, salary, effectiveDate);

        Mockito.when(
                    compensationRepository.findByEmployee_EmployeeId(Mockito.any(String.class))
                ).thenReturn(compensation);

        // When
        Compensation readCompensation = compensationService.read("employeeId");

        // Then
        assertNotNull(readCompensation);
        assertEquals(compensation, readCompensation);
    }
}