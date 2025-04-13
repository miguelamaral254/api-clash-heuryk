package br.com.clashproject.domain;

import br.com.clashproject.domain.dtos.BattleCreateDTO;
import br.com.clashproject.domain.dtos.BattleResponseDTO;
import br.com.clashproject.domain.dtos.PlayerDTO;
import br.com.clashproject.domain.entities.Battle;
import br.com.clashproject.domain.entities.Player;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-13T01:32:19-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class BattleMapperImpl implements BattleMapper {

    @Override
    public BattleResponseDTO toResponseDTO(Battle battle) {
        if ( battle == null ) {
            return null;
        }

        PlayerDTO player1 = null;
        PlayerDTO player2 = null;
        String id = null;
        Instant timestamp = null;
        String winner = null;

        player1 = playerToPlayerDTO( battle.getPlayer1() );
        player2 = playerToPlayerDTO( battle.getPlayer2() );
        id = battle.getId();
        timestamp = battle.getTimestamp();
        winner = battle.getWinner();

        BattleResponseDTO battleResponseDTO = new BattleResponseDTO( id, timestamp, player1, player2, winner );

        return battleResponseDTO;
    }

    @Override
    public Battle toEntityFromCreateDTO(BattleCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Battle.BattleBuilder battle = Battle.builder();

        battle.player1( mapPlayer( dto.player1() ) );
        battle.player2( mapPlayer( dto.player2() ) );
        battle.winner( dto.winner() );

        return battle.build();
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
