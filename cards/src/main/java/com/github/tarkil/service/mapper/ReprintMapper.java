package com.github.tarkil.service.mapper;

import com.github.tarkil.domain.*;
import com.github.tarkil.service.dto.ReprintDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Reprint and its DTO ReprintDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReprintMapper {

    @Mapping(source = "card.id", target = "cardId")
    @Mapping(source = "edition.id", target = "editionId")
    ReprintDTO reprintToReprintDTO(Reprint reprint);

    List<ReprintDTO> reprintsToReprintDTOs(List<Reprint> reprints);

    @Mapping(source = "cardId", target = "card")
    @Mapping(source = "editionId", target = "edition")
    Reprint reprintDTOToReprint(ReprintDTO reprintDTO);

    List<Reprint> reprintDTOsToReprints(List<ReprintDTO> reprintDTOs);

    default Card cardFromId(Long id) {
        if (id == null) {
            return null;
        }
        Card card = new Card();
        card.setId(id);
        return card;
    }

    default Edition editionFromId(Long id) {
        if (id == null) {
            return null;
        }
        Edition edition = new Edition();
        edition.setId(id);
        return edition;
    }
}
