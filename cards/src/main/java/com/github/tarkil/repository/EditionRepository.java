package com.github.tarkil.repository;

import com.github.tarkil.domain.Edition;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Edition entity.
 */
@SuppressWarnings("unused")
public interface EditionRepository extends JpaRepository<Edition,Long> {

    @Query("select distinct edition from Edition edition left join fetch edition.cards")
    List<Edition> findAllWithEagerRelationships();

    @Query("select edition from Edition edition left join fetch edition.cards where edition.id =:id")
    Edition findOneWithEagerRelationships(@Param("id") Long id);

}
