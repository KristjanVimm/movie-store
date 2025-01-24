package com.kristjan.movie.store.entity;

import lombok.Getter;

@Getter
public enum FilmType {

    NEW(4, 0), REGULAR(3, 3), OLD(3, 5);

    private final int price;
    private final int numberOfInitialDays;

    FilmType(int price, int numberOfInitialDays) {
        this.price = price;
        this.numberOfInitialDays = numberOfInitialDays;
    }
}
