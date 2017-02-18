package com.github.tarkil.repository;

import com.github.tarkil.domain.Block;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Block entity.
 */
@SuppressWarnings("unused")
public interface BlockRepository extends JpaRepository<Block,Long> {

}
