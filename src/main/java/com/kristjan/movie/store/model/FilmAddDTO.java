package com.kristjan.movie.store.model;

import com.kristjan.movie.store.entity.FilmType;
import lombok.Data;

@Data
public class FilmAddDTO {
    private String name;
    private FilmType type;
}
