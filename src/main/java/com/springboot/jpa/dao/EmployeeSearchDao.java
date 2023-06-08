package com.springboot.jpa.dao;

import com.springboot.jpa.models.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeSearchDao {

    private final EntityManager entityManager;


    public List<Employee> findAllBySimpleQuery(String firstName,
                                               String lastName,
                                               String email
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        //select * from employee
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        // prepare where clause
        // WHERE  firstlname like '%firstName%' AND lastName like '%lastName%' AND email like '%email%'
        Predicate firstNamePredicate = criteriaBuilder
                .like(employeeRoot.get("firstName"), "%" + firstName + "%");
        Predicate lastNamePredicate = criteriaBuilder
                .like(employeeRoot.get("lastName"), "%" + lastName + "%");
        Predicate emailPredicate = criteriaBuilder.like(employeeRoot.get("email"), "%" + email + "%");
        criteriaQuery.where(firstNamePredicate, lastNamePredicate, emailPredicate);

        Predicate firstNameOrLasteNamePredicate = criteriaBuilder.or(
                firstNamePredicate,
                lastNamePredicate
        );
        // => final query ==> select * from employee where firstName like '%firstName%' OR lastName like '%lastName%' And email like '%email%'
        var andEmailPredicate = criteriaBuilder.and(
                        firstNameOrLasteNamePredicate,
                        emailPredicate
        );
        criteriaQuery.where(andEmailPredicate);
        TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }


    public List<Employee> findAllByCriteria(
            SearchRequest searchRequest
    ){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        //select * from employee
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        if(searchRequest.getFirstName() != null){
            Predicate firstNamePredicate = criteriaBuilder
                    .like(employeeRoot.get("firstName"), "%" + searchRequest.getFirstName() + "%");
            predicates.add(firstNamePredicate);
        }
        if(searchRequest.getLastName() != null){
            Predicate lastNamePredicate = criteriaBuilder
                    .like(employeeRoot.get("lastName"), "%" + searchRequest.getLastName() + "%");
            predicates.add(lastNamePredicate);
        }
        if(searchRequest.getEmail() != null){
            Predicate emailPredicate = criteriaBuilder.like(employeeRoot.get("email"), "%" + searchRequest.getEmail() + "%");
            predicates.add(emailPredicate);
        }

        criteriaQuery.where(
                criteriaBuilder.or(predicates.toArray(new Predicate[0]))
        );
        TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }


}
