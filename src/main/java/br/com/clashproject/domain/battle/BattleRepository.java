package br.com.clashproject.domain.battle;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BattleRepository extends MongoRepository<Battle, String> {

    @Query("{ 'timestamp': { $gte: ?0, $lte: ?1 }, $or: [ { 'player1.deck': ?2 }, { 'player2.deck': ?2 } ] }")
    List<Battle> findBattlesByCardAndTimestampRange(Date start, Date end, String cardName);

    @Query("{ 'timestamp': { $gte: ?0, $lte: ?1 } }")
    List<Battle> findBattlesByTimestampRange(Date start, Date end);

    @Aggregation(pipeline = {
            "{ $match: { " +
                    "  timestamp: { $gte: ?0, $lte: ?1 }, " +
                    "  $or: [ " +
                    "    { 'player1.deck': { $all: ?2 } }, " +
                    "    { 'player2.deck': { $all: ?2 } } " +
                    "  ] " +
                    "} }",

            // Agora verificamos se o jogador com o combo perdeu a partida
            "{ $match: { " +
                    "  $or: [ " +
                    "    { 'player1.deck': { $all: ?2 }, 'winner': { $ne: 'player1' } }, " +
                    "    { 'player2.deck': { $all: ?2 }, 'winner': { $ne: 'player2' } } " +
                    "  ] " +
                    "} }",

            "{ $count: 'defeats_with_combo' }"
    })
    long countDefeatsByCardComboAndTimestampRange(Date start, Date end, List<String> cardCombo);
}