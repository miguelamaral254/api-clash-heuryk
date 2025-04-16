package br.com.clashproject.domain;

import br.com.clashproject.core.BaseMapper;
import br.com.clashproject.core.entities.*;
import br.com.clashproject.domain.dtos.*;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface BattleMapper extends BaseMapper<Battle, BattleResponseDTO> {

    ComboStatsDTO toComboStatsDTO(ComboStats comboStats);

    ComboStats toComboStats(ComboStatsDTO comboStatsDTO);

    Battle toEntityFromCreateDTO(BattleCreateDTO battleCreateDTO);

    BattleStatsDTO toBattleStatsDTO(BattleStats battleStats);

    DeckWinRateDTO toDeckWinRateDTO(DeckWinRate deckWinRate);

    FrequentCardDTO toFrequentCardDTO(FrequentCard frequentCard);

    default Page<BattleResponseDTO> toResponsePage(Page<Battle> page) {
        return page.map(this::toDto);
    }

    default Page<ComboStatsDTO> toComboStatsDTOPage(Page<ComboStats> page) {
        return page.map(this::toComboStatsDTO);
    }

    default Page<DeckWinRateDTO> toDeckWinRateDTOPage(Page<DeckWinRate> page) {
        return page.map(this::toDeckWinRateDTO);
    }

    default Page<FrequentCardDTO> toFrequentCardDTOPage(Page<FrequentCard> page) {
        return page.map(this::toFrequentCardDTO);
    }

    default Player mapPlayer(PlayerDTO dto) {
        return Player.builder()
                .nickname(dto.nickname())
                .trophies(dto.trophies())
                .level(dto.level())
                .towersDestroyed(0)
                .deck(dto.deck())
                .build();
    }

}