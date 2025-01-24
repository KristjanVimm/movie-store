package com.kristjan.movie.store.controller;

import com.kristjan.movie.store.entity.Person;
import com.kristjan.movie.store.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @PostMapping("persons")
    public List<Person> addPerson(@RequestBody Person person) {
        person.setBonusPoints(0);
        personRepository.save(person);
        return personRepository.findAll();
    }

    @DeleteMapping("persons")
    public List<Person> deletePerson(@RequestParam Long personId) {
        personRepository.deleteById(personId);
        return personRepository.findAll();
    }

    @GetMapping("persons")
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @GetMapping("bonus-points")
    public int getBonusPoints(@RequestParam Long personId) {
        return personRepository.findById(personId).orElseThrow().getBonusPoints();
    }

    @PatchMapping("bonus-points")
    public void setBonusPoints(@RequestParam Long personId, int newPoints) {
        Person person = personRepository.findById(personId).orElseThrow();
        person.setBonusPoints(newPoints);
        personRepository.save(person);
    }
}
