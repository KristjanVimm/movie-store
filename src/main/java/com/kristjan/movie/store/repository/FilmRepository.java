package com.kristjan.movie.store.repository;

import com.kristjan.movie.store.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("select f from Film f where f.cart.id = ?1 order by f.id")
    List<Film> findByCart_IdOrderByIdAsc(Long id);

    List<Film> findByRental_Person_Id(Long id);

    @Query("select f from Film f order by f.id")
    List<Film> findByOrderByIdAsc();

    List<Film> findByRentalNullOrderByIdAsc();

}
