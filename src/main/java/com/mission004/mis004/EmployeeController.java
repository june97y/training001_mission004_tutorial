package com.mission004.mis004;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

@RestController
class EmployeeController
{
    private final EmployeeRepository repository;

    private final EmployeeModelAssembler assembler;

    EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler)
    {
        this.repository = repository;
        this.assembler = assembler;
    }

    //Aggregate root
    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all()
    {
        /*collectionModel<> is another Spring HATEOAS container aimed at encapsulating collections of employee resources*/

        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());

        /*List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(employee -> new EntityModel<>(employee,
                        linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
                .collect(Collectors.toList());
       */

        /*
        there is a top-level "self" link. The "collection" is listed
        underneath the "_embedded" section. This is how HAL represents collections
        */
    }

    /*@GetMapping("/employees")
    List<Employee> all()
    {
        return repository.findAll();
    }*/

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee)
    {
        return repository.save(newEmployee);
    }

    //Single item
    @GetMapping("/employees/{id}") //more restful
    EntityModel<Employee> one(@PathVariable Long id)
    {
        /*The return type of the method has changed from Employee to EntityModel<Employee>.
        EntityModel<T> is a generic container from Spring HATEOAS that includes not only the
        data but a collection of links.
         */

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);

        /*
        return new EntityModel<>(employee,
                linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));*/

        /*linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel() asks that
        Spring HATEOAS build a link to the EmployeeController 's one() method, and flag it as a self link.*/

        /*linkTo(methodOn(EmployeeController.class).all()).withRel("employees") asks
        Spring HATEOAS to build a link to the aggregate root, all(), and call it "employees".*/
    }

    /*
    @GetMapping("/employees/{id}") //not so restful
    Employee one(@PathVariable Long id)
    {
        return repository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException(id));
    }
    */

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id)
    {
        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(()->{
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employee/{id}")
    void deleteEmployee(@PathVariable Long id)
    {
        repository.deleteById(id);
    }
}

/*
@RestController indicates that the data returned by each method will be written straight into the response body instead of rendering a template.

An EmployeeRepository is injected by constructor into the controller.

We have routes for each operations (@GetMapping, @PostMapping, @PutMapping and @DeleteMapping, corresponding to HTTP GET, POST, PUT, and DELETE calls).
(NOTE: It’s useful to read each method and understand what they do.)

EmployeeNotFoundException is an exception used to indicate when an employee is looked up but not found.
*/