package br.com.clashproject.domain.battle;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;

    public List<Battle> getAllBattles() {
        return battleRepository.findAll();
    }

    public Battle saveBattle(Battle battle) {
        return battleRepository.save(battle);
    }

    public BattleStats calculateWinLossPercentage(String start, String end, String cardName) {
        Date startDate = Date.from(Instant.parse(start));
        Date endDate = Date.from(Instant.parse(end));

        List<Battle> battles = battleRepository.findBattlesByCardAndTimestampRange(startDate, endDate, cardName);

        int totalMatches = battles.size();
        int totalWins = 0;
        int totalLosses = 0;

        for (Battle battle : battles) {
            boolean player1UsedCard = battle.getPlayer1().getDeck().contains(cardName);
            boolean player2UsedCard = battle.getPlayer2().getDeck().contains(cardName);

            if (battle.getWinner().equals("player1") && player1UsedCard) {
                totalWins++;
            } else if (battle.getWinner().equals("player2") && player2UsedCard) {
                totalWins++;
            } else if (battle.getWinner().equals("player1") && player2UsedCard) {
                totalLosses++;
            } else if (battle.getWinner().equals("player2") && player1UsedCard) {
                totalLosses++;
            }
        }

        double winPercentage = totalMatches > 0 ? (totalWins * 100.0) / totalMatches : 0.0;
        double lossPercentage = totalMatches > 0 ? (totalLosses * 100.0) / totalMatches : 0.0;

        return new BattleStats(totalMatches, totalWins, totalLosses, winPercentage, lossPercentage);
    }
    public List<DeckWinRateDTO> getDeckWinRates(String start, String end, double minWinRate) {
        Date startDate = Date.from(Instant.parse(start));
        Date endDate = Date.from(Instant.parse(end));

        List<Battle> battles = battleRepository.findBattlesByTimestampRange(startDate, endDate);

        Map<List<String>, DeckStats> deckStatsMap = new HashMap<>();

        for (Battle battle : battles) {
            List<DeckEntry> players = List.of(
                    new DeckEntry(battle.getPlayer1().getDeck(), "player1".equals(battle.getWinner())),
                    new DeckEntry(battle.getPlayer2().getDeck(), "player2".equals(battle.getWinner()))
            );

            for (DeckEntry entry : players) {
                List<String> sortedDeck = new ArrayList<>(entry.getDeck());
                Collections.sort(sortedDeck);

                DeckStats stats = deckStatsMap.getOrDefault(
                        sortedDeck,
                        new DeckStats(
                                sortedDeck,
                                0,
                                0,
                                0.0
                        ));
                stats.setTotalMatches(stats.getTotalMatches() + 1);
                if (entry.isWon()) {
                    stats.setTotalWins(stats.getTotalWins() + 1);
                }

                deckStatsMap.put(sortedDeck, stats);
            }
        }

        return deckStatsMap.values().stream()
                .peek(stats -> {
                    double winPercentage = stats.getTotalMatches() > 0
                            ? (stats.getTotalWins() * 100.0) / stats.getTotalMatches()
                            : 0.0;
                    stats.setWinPercentage(Math.round(winPercentage * 100.0) / 100.0);
                })
                .filter(stats -> stats.getWinPercentage() >= minWinRate)
                .sorted(Comparator.comparingDouble(DeckStats::getWinPercentage).reversed())
                .map(stats -> new DeckWinRateDTO(
                        stats.getDeck(),
                        stats.getTotalMatches(),
                        stats.getTotalWins(),
                        stats.getWinPercentage()
                ))
                .collect(Collectors.toList());
    }

    public long calculateDefeatsByCardCombo(String start, String end, List<String> cardCombo) {
        Date startDate = Date.from(Instant.parse(start));
        Date endDate = Date.from(Instant.parse(end));

        return battleRepository.countDefeatsByCardComboAndTimestampRange(startDate, endDate, cardCombo);
    }

}
