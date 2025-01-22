package com.kristjan.movie.store.controller;

import com.kristjan.movie.store.entity.Film;
import com.kristjan.movie.store.entity.Rental;
import com.kristjan.movie.store.model.FilmRentalDTO;
import com.kristjan.movie.store.repository.FilmRepository;
import com.kristjan.movie.store.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RentalController {

    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    FilmRepository filmRepository;

    @GetMapping("rentals")
    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    @PostMapping("start-rental")
    public List<Rental> startRental(@RequestBody List<FilmRentalDTO> films) {

        // TODO: ära luba laenutada filme, mis on juba laenutatud

        Rental rental = new Rental();
        rental = rentalRepository.save(rental);
        double sum = 0;
        for (FilmRentalDTO f: films) {
            Film dbFilm = filmRepository.findById(f.getId()).orElseThrow();
            dbFilm.setDaysRented(f.getDays());
            dbFilm.setRental(rental);
            filmRepository.save(dbFilm);
            // TODO: 1. arvuta iga filmi maksumus
            sum += 5;
        }
        rental.setInitialFee(sum);
        rentalRepository.save(rental);
        return rentalRepository.findAll();
    }

    // TODO: 3. Tagastamine
    // ID + days
    // panema filmi küljes Rentali null, days null ja arvutama
}
