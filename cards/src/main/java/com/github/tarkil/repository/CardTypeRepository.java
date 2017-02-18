package com.github.tarkil.repository;

import com.github.tarkil.domain.CardType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CardType entity.
 */
@SuppressWarnings("unused")
public interface CardTypeRepository extends JpaRepository<CardType,Long> {

}
