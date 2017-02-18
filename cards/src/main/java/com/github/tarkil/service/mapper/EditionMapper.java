package com.github.tarkil.service.mapper;

import com.github.tarkil.domain.*;
import com.github.tarkil.service.dto.EditionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Edition and its DTO EditionDTO.
 */
@Mapper(componentModel = "spring", uses = {CardMapper.class, })
public interface EditionMapper {

    @Mapping(source = "block.id", target = "blockId")
    EditionDTO editionToEditionDTO(Edition edition);

    List<EditionDTO> editionsToEditionDTOs(List<Edition> editions);

    @Mapping(source = "blockId", target = "block")
    Edition editionDTOToEdition(EditionDTO editionDTO);

    List<Edition> editionDTOsToEditions(List<EditionDTO> editionDTOs);

    default Card cardFromId(Long id) {
        if (id == null) {
            return null;
        }
        Card card = new Card();
        card.setId(id);
        return card;
    }

    default Block blockFromId(Long id) {
        if (id == null) {
            return null;
        }
        Block block = new Block();
        block.setId(id);
        return block;
    }
}
