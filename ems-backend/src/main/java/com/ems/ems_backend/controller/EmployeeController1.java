package com.ems.ems_backend.controller;

import com.ems.ems_backend.dto.EmployeeDto;
import com.ems.ems_backend.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController1 {
    private EmployeeService employeeService;
    public EmployeeController1(EmployeeService employeeService){
        this.employeeService=employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }
}
