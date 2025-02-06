package com.kristjan.movie.store.controller;

import com.kristjan.movie.store.entity.Person;
import com.kristjan.movie.store.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("persons")
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @PostMapping("persons")
    public List<Person> addPerson(@RequestBody Person person) {
        person.setBonusPoints(0);
        personRepository.save(person);
        return personRepository.findAll();
    }

    @GetMapping("password")
    public boolean checkPassword(@RequestParam Long personId, String password) {
        Person person = personRepository.findById(personId).orElseThrow();
        return person.getPassword().equals(password);
    }

    @DeleteMapping("persons")
    public List<Person> deletePerson(@RequestParam Long personId) {
        personRepository.deleteById(personId);
        return personRepository.findAll();
    }

    @GetMapping("bonus-points")
    public int getBonusPoints(@RequestParam Long personId) {
        return personRepository.findById(personId).orElseThrow().getBonusPoints();
    }

}
