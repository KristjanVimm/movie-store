package com.kristjan.movie.store.controller;

import com.kristjan.movie.store.entity.Cart;
import com.kristjan.movie.store.entity.Film;
import com.kristjan.movie.store.entity.Person;
import com.kristjan.movie.store.repository.CartRepository;
import com.kristjan.movie.store.repository.FilmRepository;
import com.kristjan.movie.store.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
public class CartController {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    private FilmRepository filmRepository;

    @GetMapping("carts")
    public List<Cart> getCarts() {
        return cartRepository.findAll();
    }

    @PostMapping("carts")
    public List<Cart> addToCart(@RequestParam Long personId, Long filmId, int daysRented) {
        Person person = personRepository.findById(personId).orElseThrow();
        Cart cart = person.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setStatus("open");
            cartRepository.save(cart);
            person.setCart(cart);
            personRepository.save(person);
        }
        Film dbFilm = filmRepository.findById(filmId).orElseThrow();
        dbFilm.setCart(cart);
        dbFilm.setDaysRented(daysRented);
        filmRepository.save(dbFilm);
        return cartRepository.findAll();
    }

    @PatchMapping("carts")
    public List<Cart> removeFromCart(@RequestParam Long filmId) {
        Film dbFilm = filmRepository.findById(filmId).orElseThrow();
        dbFilm.setCart(null);
        dbFilm.setDaysRented(0);
        filmRepository.save(dbFilm);
        return cartRepository.findAll();
    }

    @DeleteMapping("cart")
    public List<Cart> deleteCart(@RequestParam Long cartId) {
        cartRepository.deleteById(cartId);
        return cartRepository.findAll();
    }

}
