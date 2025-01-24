package com.kristjan.movie.store.service;

import com.kristjan.movie.store.entity.Film;
import com.kristjan.movie.store.entity.FilmType;
import com.kristjan.movie.store.entity.Person;
import com.kristjan.movie.store.entity.Rental;
import com.kristjan.movie.store.model.FilmRentalDTO;
import com.kristjan.movie.store.repository.FilmRepository;
import com.kristjan.movie.store.repository.PersonRepository;
import com.kristjan.movie.store.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {

    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    private PersonRepository personRepository;

    private final int PREMIUM_PRICE = 4;
    private final int BASIC_PRICE = 3;

    public void saveRental(List<FilmRentalDTO> films, Person person, int bonusDays) {
        Rental rental = new Rental();
        rental = rentalRepository.save(rental);
        double initialFee = 0;
        for (FilmRentalDTO f: films) {
            Film dbFilm = filmRepository.findById(f.getId()).orElseThrow();
            dbFilm.setDaysRented(f.getDays());
            dbFilm.setRental(rental);
            filmRepository.save(dbFilm);
            initialFee += calculateFilmFee(dbFilm, person, bonusDays);
        }
        rental.setInitialFee(initialFee);
        rental.setPerson(person);
        rentalRepository.save(rental);
    }

    private double calculateFilmFee(Film dbFilm, Person person, int bonusDays) {
        switch (dbFilm.getType()) {
            case NEW -> {
                Rental rental = dbFilm.getRental();
                int bonusDaysToUse = bonusDays - rental.getBonusDaysUsed();
                if (dbFilm.getDaysRented() < bonusDaysToUse) {
                    bonusDaysToUse = dbFilm.getDaysRented();
                }
                if (bonusDays > 0) {
                    person.setBonusPoints(person.getBonusPoints() - 25 * bonusDaysToUse);
                }
                person.setBonusPoints(person.getBonusPoints() + 2);
                personRepository.save(person);

                rental.setBonusDaysUsed(rental.getBonusDaysUsed() + bonusDaysToUse);
                rentalRepository.save(rental);

                return (dbFilm.getDaysRented() - bonusDaysToUse) * PREMIUM_PRICE;
            } case REGULAR -> {
                person.setBonusPoints(person.getBonusPoints() + 1);
                personRepository.save(person);
                return (dbFilm.getDaysRented() <= 3 ? 1 : dbFilm.getDaysRented() - 2 ) * BASIC_PRICE;
            } case OLD -> {
                person.setBonusPoints(person.getBonusPoints() + 1);
                personRepository.save(person);
                return (dbFilm.getDaysRented() <= 5 ? 1 : dbFilm.getDaysRented() - 4 ) * BASIC_PRICE;
            } case null, default -> {
                return 0;
            }
        }
    }

    public void checkIfAllAvailable(List<FilmRentalDTO> films, Person person, int bonusDays) {
        for (FilmRentalDTO f : films) {
            Film dbFilm = filmRepository.findById(f.getId()).orElseThrow();
            if (dbFilm.getRental() != null || dbFilm.getDaysRented() > 0) {
                throw new RuntimeException("ERROR_FILM_RENTED, ID: " + dbFilm.getId());
            }
            if (dbFilm.getType() == FilmType.NEW && bonusDays > 0 && person.getBonusPoints() < bonusDays * 25) {
                throw new RuntimeException("ERROR_NOT_ENOUGH_BONUS_POINTS");
            }
        }
    }

    public void calculateLateFee(List<FilmRentalDTO> returns) {
        double lateFee = 0;
        for (FilmRentalDTO rentalDTO : returns) {
            Film dbFilm = filmRepository.findById(rentalDTO.getId()).orElseThrow();
            if (dbFilm.getDaysRented() < rentalDTO.getDays()) {
                Rental filmRental = dbFilm.getRental();
                if (filmRental == null) {
                    throw new RuntimeException("ERROR_FILM_NOT_RENTED: " + dbFilm.getId());
                }
                filmRental.setLateFee(filmRental.getLateFee() + getFilmLateFee(dbFilm, rentalDTO.getDays()));
                rentalRepository.save(filmRental);
            }
            dbFilm.setRental(null);
            dbFilm.setDaysRented(0);
            filmRepository.save(dbFilm);
        }
    }

    private double getFilmLateFee(Film dbFilm, int days) {
        switch (dbFilm.getType()) {
            case NEW -> {
                return (days - dbFilm.getDaysRented()) * PREMIUM_PRICE;
            }
            case OLD, REGULAR -> {
                return (days - dbFilm.getDaysRented()) * BASIC_PRICE;
            }
            case null, default -> {
                return 0;
            }
        }
    }
}
