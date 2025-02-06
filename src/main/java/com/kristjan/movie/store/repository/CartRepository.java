package com.kristjan.movie.store.repository;

import com.kristjan.movie.store.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
