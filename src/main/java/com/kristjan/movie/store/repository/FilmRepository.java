package com.kristjan.movie.store.repository;

import com.kristjan.movie.store.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("select f from Film f where f.rental is null")
    List<Film> findByRentalNull();

}
