package com.github.tarkil.service.mapper;

import com.github.tarkil.domain.*;
import com.github.tarkil.service.dto.CardDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Card and its DTO CardDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CardMapper {

    @Mapping(source = "type.id", target = "typeId")
    CardDTO cardToCardDTO(Card card);

    List<CardDTO> cardsToCardDTOs(List<Card> cards);

    @Mapping(source = "typeId", target = "type")
    @Mapping(target = "reprints", ignore = true)
    Card cardDTOToCard(CardDTO cardDTO);

    List<Card> cardDTOsToCards(List<CardDTO> cardDTOs);

    default CardType cardTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        CardType cardType = new CardType();
        cardType.setId(id);
        return cardType;
    }
}
