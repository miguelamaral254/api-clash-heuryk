package br.com.clashproject.domain;

import br.com.clashproject.domain.dtos.BattleCreateDTO;
import br.com.clashproject.domain.dtos.BattleResponseDTO;
import br.com.clashproject.domain.dtos.PlayerDTO;
import br.com.clashproject.domain.entities.Battle;
import br.com.clashproject.domain.entities.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface BattleMapper {

    @Mapping(target = "player1", source = "player1")
    @Mapping(target = "player2", source = "player2")
    BattleResponseDTO toResponseDTO(Battle battle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "player1", source = "player1", qualifiedByName = "mapPlayer")
    @Mapping(target = "player2", source = "player2", qualifiedByName = "mapPlayer")
    Battle toEntityFromCreateDTO(BattleCreateDTO dto);

    @Named("mapPlayer")
    default Player mapPlayer(PlayerDTO dto) {
        return Player.builder()
                .nickname(dto.nickname())
                .trophies(dto.trophies())
                .level(dto.level())
                .towersDestroyed(0)
                .deck(dto.deck())
                .build();
    }

    default Page<BattleResponseDTO> toResponsePage(Page<Battle> page) {
        return page.map(this::toResponseDTO);
    }
}