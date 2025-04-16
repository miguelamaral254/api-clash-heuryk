package br.com.clashproject.domain;

import br.com.clashproject.core.entities.*;
import br.com.clashproject.domain.dtos.BetterWinrateCardLowLevelDTO;
import br.com.clashproject.domain.dtos.CardStatsDTO;
import br.com.clashproject.domain.dtos.WorstWinrateCardDTO;
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

    @Aggregation(pipeline = {
            "{ $match: { " +
                    "  timestamp: { $gte: ?0, $lte: ?1 }, " +
                    "  $or: [ " +
                    "    { 'player1.deck': { $all: ?2 } }, " +
                    "    { 'player2.deck': { $all: ?2 } } " +
                    "  ] " +
                    "} }",

            "{ $addFields: { " +
                    "  trophyDifference: { " +
                    "    $cond: { " +
                    "      if: { $eq: ['$winner', 'player1'] }, " +
                    "      then: { $subtract: ['$player2.trophies', '$player1.trophies'] }, " +
                    "      else: { $subtract: ['$player1.trophies', '$player2.trophies'] } " +
                    "    } " +
                    "  } " +
                    "} }",

            "{ $match: { " +
                    "  $expr: { " +
                    "    $lt: [ " +
                    "      '$trophyDifference', " +
                    "      { $multiply: [{ $divide: ['$player1.trophies', 100] }, ?3] } " +
                    "    ] " +
                    "  } " +
                    "} }",

            "{ $match: { " +
                    "  $expr: { " +
                    "    $lt: [ { $subtract: ['$timestamp', '$timestamp'] }, 120000 ] " +
                    "  }, " +
                    "  $or: [ " +
                    "    { 'player1.towersDestroyed': { $gte: 2 }, 'winner': { $ne: 'player1' } }, " +
                    "    { 'player2.towersDestroyed': { $gte: 2 }, 'winner': { $ne: 'player2' } } " +
                    "  ] " +
                    "} }",

            "{ $count: 'victories_with_conditions' }"
    })
    List<Long> countVictoriesWithConditions(Date start, Date end, List<String> cardCombo, int trophyPercentage);



    @Aggregation(pipeline = {
            "{ $match: { " +
                    "    timestamp: { $gte: ?0, $lte: ?1 }, " +
                    "    $or: [ " +
                    "        { 'player1.deck': { $size: ?2 } }, " +
                    "        { 'player2.deck': { $size: ?2 } } " +
                    "    ] " +
                    "} }",
            "{ $project: { " +
                    "    allDecks: [ " +
                    "        { deck: '$player1.deck', isPlayer1: true, winner: '$winner' }, " +
                    "        { deck: '$player2.deck', isPlayer1: false, winner: '$winner' } " +
                    "    ] " +
                    "} }",
            "{ $unwind: '$allDecks' }",
            "{ $addFields: { " +
                    "    combos: { " +
                    "        $map: { " +
                    "            input: { $range: [0, { $subtract: [{ $size: '$allDecks.deck' }, ?3] }] }, " +
                    "            as: 'i', " +
                    "            in: { $slice: ['$allDecks.deck', '$$i', ?3] } " +
                    "        } " +
                    "    } " +
                    "} }",
            "{ $unwind: '$combos' }",
            "{ $group: { " +
                    "    _id: '$combos', " +
                    "    totalMatches: { $sum: 1 }, " +
                    "    totalWins: { " +
                    "        $sum: { " +
                    "            $cond: [ " +
                    "                { $and: [ " +
                    "                    { $eq: ['$allDecks.isPlayer1', true] }, " +
                    "                    { $eq: ['$allDecks.winner', 'player1'] } " +
                    "                ] }, " +
                    "                1, " +
                    "                { $cond: [ " +
                    "                    { $and: [ " +
                    "                        { $eq: ['$allDecks.isPlayer1', false] }, " +
                    "                        { $eq: ['$allDecks.winner', 'player2'] } " +
                    "                    ] }, " +
                    "                    1, " +
                    "                    0 " +
                    "                ] } " +
                    "            ] " +
                    "        } " +
                    "    } " +
                    "} }",
            "{ $addFields: { " +
                    "    totalLosses: { $subtract: ['$totalMatches', '$totalWins'] }, " +
                    "    winPercentage: { " +
                    "        $round: [ " +
                    "            { $multiply: [{ $divide: ['$totalWins', '$totalMatches'] }, 100] }, " +
                    "            2 " +
                    "        ] " +
                    "    } " +
                    "} }",
            "{ $match: { " +
                    "    winPercentage: { $gte: ?4 } " +
                    "} }",
            "{ $project: { " +
                    "    _id: 0, " +
                    "    combo: '$_id', " +
                    "    totalMatches: 1, " +
                    "    totalWins: 1, " +
                    "    totalLosses: 1, " +
                    "    winPercentage: 1 " +
                    "} }",
            "{ $sort: { totalMatches: -1, winPercentage: -1 } }"
    })
    List<ComboStats> findComboStats(Date start, Date end, int deckSize, int comboSize, double minWinPercentage);

    // lista de cartas que aparecem nos decks vencedores low elo
    @Aggregation(pipeline = {
            "{ $addFields: { averageLevel: { $avg: ['$player1.level', '$player2.level'] } } }",
            "{ $match: { averageLevel: { $lt: ?0 }, timestamp: { $gte: ?1, $lte: ?2 } } }",
            "{ $project: { winningDeck: { $cond: { if: { $eq: ['$winner', 'player1'] }, then: '$player1.deck', else: '$player2.deck' } } } }",
            "{ $unwind: '$winningDeck' }",
            "{ $group: { _id: '$winningDeck', count: { $sum: 1 } } }",
            "{ $sort: { count: -1 } }"
    })
    List<DeckWinRateLowElo> findDecksPerLowLevel(double maxAvgLevel, Date start, Date end);

    // Cartas mais presentes em decks derrotados (armadilha)
    @Aggregation(pipeline = {
            "{ $project: { losingDeck: { $cond: [ { $eq: ['$winner', 'player1'] }, '$player2.deck', '$player1.deck' ] } } }",
            "{ $unwind: '$losingDeck' }",
            "{ $group: { _id: '$losingDeck', derrotas: { $sum: 1 } } }",
            "{ $project: { carta: '$_id', derrotas: 1, _id: 0 } }",
            "{ $sort: { derrotas: -1 } }"
    })
    List<CardWinRate> findWorstWinrateCard();

    // Encontrar cartas que aparecem com mais frequência em partidas com mesmo numero de torres destruídas, pequena diferença de trofeus e resultado empatado
    @Aggregation(pipeline = {
            "{ $match: { " +
                    "    timestamp: { $gte: ?0, $lte: ?1 }, " +
                    "    $expr: { $eq: ['$player1.torres_destruidas', '$player2.torres_destruidas'] }, " +
                    "    $expr: { $lte: [ { $abs: { $subtract: ['$player1.trofeus', '$player2.trofeus'] } }, ?2 ] }, " +
                    "    $or: [ { winner: null }, { winner: 'draw' } ] " +
                    "} }",

            "{ $project: { " +
                    "    allCards: { $concatArrays: ['$player1.deck', '$player2.deck'] } " +
                    "} }",

            "{ $unwind: '$allCards' }",

            "{ $group: { _id: '$allCards', empateCount: { $sum: 1 } } }",

            "{ $sort: { empateCount: -1 } }"
    })
    List<CardStatsDTO> findCardsInBalancedDraws(Date start, Date end, int maxTrophyDifference);


}