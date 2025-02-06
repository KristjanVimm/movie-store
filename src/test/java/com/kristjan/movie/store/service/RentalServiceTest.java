package com.kristjan.movie.store.service;

import com.kristjan.movie.store.entity.Film;
import com.kristjan.movie.store.entity.FilmType;
import com.kristjan.movie.store.entity.Person;
import com.kristjan.movie.store.entity.Rental;
import com.kristjan.movie.store.model.FilmRentalDTO;
import com.kristjan.movie.store.repository.FilmRepository;
import com.kristjan.movie.store.repository.PersonRepository;
import com.kristjan.movie.store.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RentalServiceTest {

    @Mock
    RentalRepository rentalRepository;
    @Mock
    FilmRepository filmRepository;
    @Mock
    PersonRepository personRepository;
    @InjectMocks
    RentalService rentalService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void givenFilmIsAlreadyRented_whenRentalStarts_thenFilmRentedExceptionIsThrown() {
        Film film = new Film();
        film.setRental(new Rental());
        when(filmRepository.findById(any())).thenReturn(Optional.of(film));
        List<FilmRentalDTO> filmRentalDTOs = new ArrayList<>();
        FilmRentalDTO filmRentalDTO = new FilmRentalDTO();
        filmRentalDTOs.add(filmRentalDTO);
        Person person = new Person();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> rentalService.checkIfAllAvailable(filmRentalDTOs, person, 0));
        assertTrue(exception.getMessage().startsWith("ERROR_FILM_RENTED"));
    }

    @Test
    void givenNotEnoughPoints_whenRentalStarts_thenNotEnoughPointsExceptionIsThrown() {
        Film film = new Film();
        film.setType(FilmType.NEW);
        when(filmRepository.findById(any())).thenReturn(Optional.of(film));
        List<FilmRentalDTO> filmRentalDTOs = new ArrayList<>();
        FilmRentalDTO filmRentalDTO = new FilmRentalDTO();
        filmRentalDTOs.add(filmRentalDTO);
        Person person = new Person();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> rentalService.checkIfAllAvailable(filmRentalDTOs, person, 1));
        assertTrue(exception.getMessage().startsWith("ERROR_NOT_ENOUGH_BONUS_POINTS"));
    }

    @Test
    void givenNewFilmAndBonusDaysNotZeroAndPersonHasEnoughPoints_whenRentalStarts_thenNoExceptionIsThrown() {
        Film film = new Film();
        film.setType(FilmType.NEW);
        when(filmRepository.findById(any())).thenReturn(Optional.of(film));
        List<FilmRentalDTO> filmRentalDTOs = new ArrayList<>();
        FilmRentalDTO filmRentalDTO = new FilmRentalDTO();
        filmRentalDTOs.add(filmRentalDTO);
        Person person = new Person();
        person.setBonusPoints(25);
        assertDoesNotThrow(() -> rentalService.checkIfAllAvailable(filmRentalDTOs, person, 1));
    }

    @Test
    void givenFilmIsOldAndRentedForFiveDays_whenRentalStarts_thenInitialFeeIs3() {
        Rental rental = new Rental();
        when(rentalRepository.save(any())).thenReturn(rental);
        Film film = new Film();
        film.setType(FilmType.OLD);
        when(filmRepository.findById(any())).thenReturn(Optional.of(film));
        List<FilmRentalDTO> filmRentalDTOs = new ArrayList<>();
        FilmRentalDTO filmRentalDTO = new FilmRentalDTO();
        filmRentalDTO.setDays(5);
        filmRentalDTOs.add(filmRentalDTO);
        rentalService.saveRental(filmRentalDTOs, new Person(), 0);
        assertEquals(3, rental.getInitialFee());
    }

    @Test
    void givenFilmIsNewAndRentedForFiveDays_whenRentalStarts_thenInitialFeeIs20() {
        Rental rental = new Rental();
        when(rentalRepository.save(any())).thenReturn(rental);
        Film film = new Film();
        film.setType(FilmType.NEW);
        when(filmRepository.findById(any())).thenReturn(Optional.of(film));
        List<FilmRentalDTO> filmRentalDTOs = new ArrayList<>();
        FilmRentalDTO filmRentalDTO = new FilmRentalDTO();
        filmRentalDTO.setDays(5);
        filmRentalDTOs.add(filmRentalDTO);
        rentalService.saveRental(filmRentalDTOs, new Person(), 0);
        assertEquals(20, rental.getInitialFee());
    }

    @Test
    void checkIfAllRented() {
    }

//  TODO: kodus teha
    @Test
    void calculateLateFee() {
    }

//  givenX_whenY_thenZ
    @Test
    void givenFilmIsNewAndLateForOneDay_whenFilmIsReturned_thenLateFeeIs4() {
        Film film = new Film();
        film.setType(FilmType.NEW);
        film.setDaysRented(3);
        double filmCost = rentalService.getFilmLateFee(film, 4);
        assertEquals(4.0, filmCost);
    }

    @Test
    void givenFilmIsOldAndLateForFourDays_whenFilmIsReturned_thenLateFeeIs12() {
        Film film = new Film();
        film.setType(FilmType.OLD);
        film.setDaysRented(3);
        double filmCost = rentalService.getFilmLateFee(film, 7);
        assertEquals(12.0, filmCost);
    }

    @Test
    void givenFilmIsRegularAndLateForThreeDays_whenFilmIsReturned_thenLateFeeIs9() {
        Film film = new Film();
        film.setType(FilmType.REGULAR);
        film.setDaysRented(3);
        double filmCost = rentalService.getFilmLateFee(film, 6);
        assertEquals(9.0, filmCost);
    }

    @Test
    void clearCart() {
    }
}