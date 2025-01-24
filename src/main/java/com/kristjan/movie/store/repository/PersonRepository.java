package com.kristjan.movie.store.repository;

import com.kristjan.movie.store.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
