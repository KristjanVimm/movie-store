package com.kristjan.movie.store.controller;

import com.kristjan.movie.store.entity.Cart;
import com.kristjan.movie.store.entity.Film;
import com.kristjan.movie.store.entity.FilmType;
import com.kristjan.movie.store.model.FilmAddDTO;
import com.kristjan.movie.store.repository.FilmRepository;
import com.kristjan.movie.store.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
public class FilmController {

    @Autowired
    FilmRepository filmRepository;
    @Autowired
    PersonRepository personRepository;

    @PostMapping("films")
    public List<Film> addFilm(@RequestBody FilmAddDTO filmAdd) {
        Film film = new Film();
        film.setName(filmAdd.getName());
        film.setType(filmAdd.getType());
        filmRepository.save(film);
        return filmRepository.findAll();
    }

    @DeleteMapping("films")
    public List<Film> deleteFilm(@RequestParam Long filmId) {
        filmRepository.deleteById(filmId);
        return filmRepository.findAll();
    }

    @PatchMapping("film-type")
    public void changeGenre(@RequestParam Long filmId, @RequestParam FilmType newType) {
        Film film = filmRepository.getReferenceById(filmId);
        film.setType(newType);
        filmRepository.save(film);
    }

    @GetMapping("all-films")
    public List<Film> getFilms() {
        return filmRepository.findByOrderByIdAsc();
    }

    @GetMapping("available-films")
    public List<Film> getAvailableFilms() {
        return filmRepository.findByRentalNullOrderByIdAsc();
    }

    @GetMapping("cart-films")
    public List<Film> getCartFilms(@RequestParam Long personId) {
        Cart cart = personRepository.findById(personId).orElseThrow().getCart();
        if (cart == null) {
            return new ArrayList<>();
        }
        return filmRepository.findByCart_IdOrderByIdAsc(cart.getId());
    }

    @GetMapping("rented-films")
    public List<Film> getRentedFilms(@RequestParam Long personId) {
        return filmRepository.findByRental_Person_Id(personId);
    }

}
