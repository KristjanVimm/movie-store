package com.kristjan.movie.store.repository;

import com.kristjan.movie.store.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {

}
