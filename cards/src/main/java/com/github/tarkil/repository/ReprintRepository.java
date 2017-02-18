package com.github.tarkil.repository;

import com.github.tarkil.domain.Reprint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Reprint entity.
 */
@SuppressWarnings("unused")
public interface ReprintRepository extends JpaRepository<Reprint,Long> {

}
