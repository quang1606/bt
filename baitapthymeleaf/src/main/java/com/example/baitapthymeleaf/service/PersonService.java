package com.example.baitapthymeleaf.service;

import com.example.baitapthymeleaf.model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface PersonService {
    public List<Person> getPeople();
    public List<Person> sortJob();
    public List<Person> sortPeople();
    public List<Person> sortCity();
    public ResponseEntity<?> getPeopleByCity();
    public ResponseEntity<?> getJobCount();
    public List<Person> getAverageSalary();
    public List<Person> getLongsName();
    public List<Person> getPersonById(String id);
}
