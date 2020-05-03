package com.mission004.mis004;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, Long>
{ }
/*
This interface, though empty on the surface, packs a punch given it supports:

Creating new instances
Updating existing ones
Deleting
Finding (one, all, by simple or complex properties)

* */