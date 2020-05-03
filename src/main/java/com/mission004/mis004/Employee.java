package com.mission004.mis004;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
class Employee
{

    private @Id @GeneratedValue Long id;
    private String name;
    private String role;

    Employee() {}

    Employee(String name, String role)
    {
        this.name = name;
        this.role = role;
    }
}

/*
@Data is a Lombok annotation to create all the getters, setters, equals, hash, and toString methods, based on the fields.

@Entity is a JPA annotation to make this object ready for storage in a JPA-based data store.

id, name, and role are the attribute for our domain object, the first being marked with more JPA
annotations to indicate it’s the primary key and automatically populated by the JPA provider.

a custom constructor is created when we need to create a new instance, but don’t yet have an id.
 */