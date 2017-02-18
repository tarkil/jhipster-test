package com.github.tarkil.service.mapper;

import com.github.tarkil.domain.*;
import com.github.tarkil.service.dto.CardTypeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CardType and its DTO CardTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CardTypeMapper {

    @Mapping(source = "game.id", target = "gameId")
    CardTypeDTO cardTypeToCardTypeDTO(CardType cardType);

    List<CardTypeDTO> cardTypesToCardTypeDTOs(List<CardType> cardTypes);

    @Mapping(source = "gameId", target = "game")
    CardType cardTypeDTOToCardType(CardTypeDTO cardTypeDTO);

    List<CardType> cardTypeDTOsToCardTypes(List<CardTypeDTO> cardTypeDTOs);

    default Game gameFromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
