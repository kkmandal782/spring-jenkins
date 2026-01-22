package com.ems.ems_backend.service.impl;

import com.ems.ems_backend.dto.EmployeeDto;
import com.ems.ems_backend.entity.Employee;
import com.ems.ems_backend.exception.EmailAlreadyExistsException;
import com.ems.ems_backend.exception.ResourceNotFoundException;
import com.ems.ems_backend.mapper.EmployeeMapper;
import com.ems.ems_backend.repository.EmployeeRepository;
import com.ems.ems_backend.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee  savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
      Employee employee=  employeeRepository.findById(employeeId)
                .orElseThrow(()-> new ResourceNotFoundException("Employee Does Not Exits : "+ employeeId));
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
       List<Employee> employees = employeeRepository.findAll();
       return employees.stream()
               .map(EmployeeMapper::mapToEmployeeDto)
               .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {

       Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee Not Found With Id "+ employeeId)
        );
        employeeRepository.findByEmail(updatedEmployee.getEmail())
                .filter(existingEmployee -> !Objects.equals(existingEmployee.getId(), employeeId))
                .ifPresent(e -> {
                    throw new EmailAlreadyExistsException("Email already exists: " + updatedEmployee.getEmail());
                });


        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());

        return EmployeeMapper.mapToEmployeeDto(employeeRepository.save(employee));

    }

    @Override
    public void deleteEmployee(Long employeeId) {
       Employee employee = employeeRepository.findById(employeeId)
               .orElseThrow(
                       ()-> new ResourceNotFoundException("Employee Not Found :" +employeeId)
               );

       employeeRepository.deleteById(employeeId);

    }
}
