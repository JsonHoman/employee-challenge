package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReportingStructureServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ReportingStructure getNumberOfDirectReports(String employeeId) {
        LOG.debug("Calculating reporting structure with employeeId [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        int directReportCount = getDirectReports(employee);

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setNumberOfReports(directReportCount);
        reportingStructure.setEmployee(employee);

        return reportingStructure;
    }

    private int getDirectReports(Employee employee) {
        int directReportCount = 0;

        if (employee != null) {
            List<Employee> directReports = employee.getDirectReports();

            if (directReports != null) {
                for (Employee directReport : directReports) {
                    directReportCount++;
                    directReportCount += getDirectReports(directReport);
                }
            }
        }

        return directReportCount;
    }
}
