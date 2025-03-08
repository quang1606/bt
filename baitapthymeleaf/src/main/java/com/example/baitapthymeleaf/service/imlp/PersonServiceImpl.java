package com.example.baitapthymeleaf.service.imlp;

import com.example.baitapthymeleaf.model.Person;
import com.example.baitapthymeleaf.repository.PersonRepository;
import com.example.baitapthymeleaf.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> getPeople() {
        return personRepository.getPeople();
    }

    @Override
    public List<Person> sortJob() {
        return personRepository.sortJob();
    }

    @Override
    public List<Person> sortPeople() {
        return personRepository.sortPeople();
    }

    @Override
    public List<Person> sortCity() {
        return personRepository.sortCity();
    }

    @Override
    public ResponseEntity<?> getPeopleByCity() {
        return personRepository.getPeopleByCity();
    }

    @Override
    public ResponseEntity<?> getJobCount() {
        return personRepository.getJobCount();
    }

    @Override
    public List<Person> getAverageSalary() {
        return personRepository.getAverageSalary();
    }

    @Override
    public List<Person> getLongsName() {
        return personRepository.getLongsName();
    }

    @Override
    public List<Person> getPersonById(String id) {
        return personRepository.getPersonById(id);
    }
}
