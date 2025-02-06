package com.kristjan.movie.store.controller;

import com.kristjan.movie.store.entity.Person;
import com.kristjan.movie.store.entity.Rental;
import com.kristjan.movie.store.model.FilmRentalDTO;
import com.kristjan.movie.store.repository.PersonRepository;
import com.kristjan.movie.store.repository.RentalRepository;
import com.kristjan.movie.store.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
public class RentalController {

    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    RentalService rentalService;
    @Autowired
    PersonRepository personRepository;

    @GetMapping("rentals")
    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    @PostMapping("start-rental")
    public List<Rental> startRental(
            @RequestBody List<FilmRentalDTO> films,
            @RequestParam Long personId,
            @RequestParam(required=false, defaultValue = "0") int bonusDays) {
        Person person = personRepository.findById(personId).orElseThrow();
        rentalService.checkIfAllAvailable(films, person, bonusDays);
        rentalService.clearCart(films, person);
        rentalService.saveRental(films, person, bonusDays);
        return rentalRepository.findAll();
    }

    @PostMapping("end-rental")
    public List<Rental> endRental(@RequestBody List<FilmRentalDTO> returns, @RequestParam Long personId) {
        rentalService.checkIfAllRented(returns, personId);
        rentalService.calculateLateFee(returns);
        return rentalRepository.findAll();
    }

    @DeleteMapping("rentals")
    public List<Rental> deleteRental(@RequestParam Long rentalId) {
        rentalRepository.deleteById(rentalId);
        return rentalRepository.findAll();
    }

}
