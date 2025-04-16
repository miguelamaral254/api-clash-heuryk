package br.com.clashproject.domain;

import br.com.clashproject.core.entities.Battle;
import br.com.clashproject.core.entities.BattleStats;
import br.com.clashproject.core.entities.ComboStats;
import br.com.clashproject.core.entities.DeckWinRate;
import br.com.clashproject.core.entities.FrequentCard;
import br.com.clashproject.core.entities.Player;
import br.com.clashproject.domain.dtos.BattleCreateDTO;
import br.com.clashproject.domain.dtos.BattleResponseDTO;
import br.com.clashproject.domain.dtos.BattleStatsDTO;
import br.com.clashproject.domain.dtos.ComboStatsDTO;
import br.com.clashproject.domain.dtos.DeckWinRateDTO;
import br.com.clashproject.domain.dtos.FrequentCardDTO;
import br.com.clashproject.domain.dtos.PlayerDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-16T13:15:48-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class BattleMapperImpl implements BattleMapper {

    @Override
    public BattleResponseDTO toDto(Battle entity) {
        if ( entity == null ) {
            return null;
        }

        String id = null;
        Instant timestamp = null;
        PlayerDTO player1 = null;
        PlayerDTO player2 = null;
        String winner = null;

        id = entity.getId();
        timestamp = entity.getTimestamp();
        player1 = playerToPlayerDTO( entity.getPlayer1() );
        player2 = playerToPlayerDTO( entity.getPlayer2() );
        winner = entity.getWinner();

        BattleResponseDTO battleResponseDTO = new BattleResponseDTO( id, timestamp, player1, player2, winner );

        return battleResponseDTO;
    }

    @Override
    public Battle toEntity(BattleResponseDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Battle.BattleBuilder battle = Battle.builder();

        battle.id( dto.id() );
        battle.timestamp( dto.timestamp() );
        battle.player1( mapPlayer( dto.player1() ) );
        battle.player2( mapPlayer( dto.player2() ) );
        battle.winner( dto.winner() );

        return battle.build();
    }

    @Override
    public void updateFromDto(BattleResponseDTO dto, Battle entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.id() != null ) {
            entity.setId( dto.id() );
        }
        if ( dto.timestamp() != null ) {
            entity.setTimestamp( dto.timestamp() );
        }
        if ( dto.player1() != null ) {
            entity.setPlayer1( mapPlayer( dto.player1() ) );
        }
        if ( dto.player2() != null ) {
            entity.setPlayer2( mapPlayer( dto.player2() ) );
        }
        if ( dto.winner() != null ) {
            entity.setWinner( dto.winner() );
        }
    }

    @Override
    public ComboStatsDTO toComboStatsDTO(ComboStats comboStats) {
        if ( comboStats == null ) {
            return null;
        }

        List<String> combo = null;
        int totalMatches = 0;
        int totalWins = 0;
        int totalLosses = 0;
        double winPercentage = 0.0d;

        List<String> list = comboStats.getCombo();
        if ( list != null ) {
            combo = new ArrayList<String>( list );
        }
        totalMatches = comboStats.getTotalMatches();
        totalWins = comboStats.getTotalWins();
        totalLosses = comboStats.getTotalLosses();
        winPercentage = comboStats.getWinPercentage();

        ComboStatsDTO comboStatsDTO = new ComboStatsDTO( combo, totalMatches, totalWins, totalLosses, winPercentage );

        return comboStatsDTO;
    }

    @Override
    public ComboStats toComboStats(ComboStatsDTO comboStatsDTO) {
        if ( comboStatsDTO == null ) {
            return null;
        }

        ComboStats comboStats = new ComboStats();

        List<String> list = comboStatsDTO.combo();
        if ( list != null ) {
            comboStats.setCombo( new ArrayList<String>( list ) );
        }
        comboStats.setTotalMatches( comboStatsDTO.totalMatches() );
        comboStats.setTotalWins( comboStatsDTO.totalWins() );
        comboStats.setTotalLosses( comboStatsDTO.totalLosses() );
        comboStats.setWinPercentage( comboStatsDTO.winPercentage() );

        return comboStats;
    }

    @Override
    public Battle toEntityFromCreateDTO(BattleCreateDTO battleCreateDTO) {
        if ( battleCreateDTO == null ) {
            return null;
        }

        Battle.BattleBuilder battle = Battle.builder();

        battle.player1( mapPlayer( battleCreateDTO.player1() ) );
        battle.player2( mapPlayer( battleCreateDTO.player2() ) );
        battle.winner( battleCreateDTO.winner() );

        return battle.build();
    }

    @Override
    public BattleStatsDTO toBattleStatsDTO(BattleStats battleStats) {
        if ( battleStats == null ) {
            return null;
        }

        int totalMatches = 0;
        int totalWins = 0;
        int totalLosses = 0;
        double winPercentage = 0.0d;
        double lossPercentage = 0.0d;

        totalMatches = battleStats.getTotalMatches();
        totalWins = battleStats.getTotalWins();
        totalLosses = battleStats.getTotalLosses();
        winPercentage = battleStats.getWinPercentage();
        lossPercentage = battleStats.getLossPercentage();

        BattleStatsDTO battleStatsDTO = new BattleStatsDTO( totalMatches, totalWins, totalLosses, winPercentage, lossPercentage );

        return battleStatsDTO;
    }

    @Override
    public DeckWinRateDTO toDeckWinRateDTO(DeckWinRate deckWinRate) {
        if ( deckWinRate == null ) {
            return null;
        }

        List<String> deck = null;
        int totalMatches = 0;
        int totalWins = 0;
        double winPercentage = 0.0d;

        List<String> list = deckWinRate.getDeck();
        if ( list != null ) {
            deck = new ArrayList<String>( list );
        }
        totalMatches = deckWinRate.getTotalMatches();
        totalWins = deckWinRate.getTotalWins();
        winPercentage = deckWinRate.getWinPercentage();

        DeckWinRateDTO deckWinRateDTO = new DeckWinRateDTO( deck, totalMatches, totalWins, winPercentage );

        return deckWinRateDTO;
    }

    @Override
    public FrequentCardDTO toFrequentCardDTO(FrequentCard frequentCard) {
        if ( frequentCard == null ) {
            return null;
        }

        String card = null;
        long count = 0L;

        card = frequentCard.getCard();
        count = frequentCard.getCount();

        FrequentCardDTO frequentCardDTO = new FrequentCardDTO( card, count );

        return frequentCardDTO;
    }

    protected PlayerDTO playerToPlayerDTO(Player player) {
        if ( player == null ) {
            return null;
        }

        String nickname = null;
        int trophies = 0;
        int level = 0;
        List<String> deck = null;

        nickname = player.getNickname();
        trophies = player.getTrophies();
        level = player.getLevel();
        List<String> list = player.getDeck();
        if ( list != null ) {
            deck = new ArrayList<String>( list );
        }

        PlayerDTO playerDTO = new PlayerDTO( nickname, trophies, level, deck );

        return playerDTO;
    }
}
