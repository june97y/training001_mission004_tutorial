package com.mission004.mis004;

class EmployeeNotFoundException extends RuntimeException
{
    EmployeeNotFoundException(Long id)
    {
        super("Could not find employee " + id);
    }
}
