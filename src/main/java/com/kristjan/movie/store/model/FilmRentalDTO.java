package com.kristjan.movie.store.model;

import lombok.Data;

// DTO - data transfer object
@Data
public class FilmRentalDTO {
    private Long id;
    private int days;
}
