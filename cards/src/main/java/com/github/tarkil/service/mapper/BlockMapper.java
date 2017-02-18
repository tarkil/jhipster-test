package com.github.tarkil.service.mapper;

import com.github.tarkil.domain.*;
import com.github.tarkil.service.dto.BlockDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Block and its DTO BlockDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BlockMapper {

    @Mapping(source = "game.id", target = "gameId")
    BlockDTO blockToBlockDTO(Block block);

    List<BlockDTO> blocksToBlockDTOs(List<Block> blocks);

    @Mapping(target = "editions", ignore = true)
    @Mapping(source = "gameId", target = "game")
    Block blockDTOToBlock(BlockDTO blockDTO);

    List<Block> blockDTOsToBlocks(List<BlockDTO> blockDTOs);

    default Game gameFromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
