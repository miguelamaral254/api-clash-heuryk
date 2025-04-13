package br.com.clashproject.domain;


import br.com.clashproject.domain.dtos.*;
import br.com.clashproject.domain.entities.Battle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;
    private final BattleMapper battleMapper;

    public Page<BattleResponseDTO> getAllBattles(Pageable pageable) {
        Page<Battle> battles = battleRepository.findAll(pageable);
        return battles.map(battleMapper::toResponseDTO);
    }
    public Battle createBattle(BattleCreateDTO battleCreateDTO) {
        Battle battle = battleMapper.toEntityFromCreateDTO(battleCreateDTO);
        battle.setTimestamp(Instant.now());
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

    public Page<DeckWinRateDTO> getDeckWinRates(String start, String end, double minWinRate, Pageable pageable) {
        List<Battle> battles = findBattlesInDateRange(start, end);
        Map<List<String>, DeckStats> deckStatsMap = calculateDeckStatistics(battles);
        List<DeckWinRateDTO> allResults = processAndFilterResults(deckStatsMap, minWinRate);
        return paginateResults(allResults, pageable);
    }

    private List<Battle> findBattlesInDateRange(String start, String end) {
        Date startDate = Date.from(Instant.parse(start));
        Date endDate = Date.from(Instant.parse(end));
        return battleRepository.findBattlesByTimestampRange(startDate, endDate);
    }

    private Map<List<String>, DeckStats> calculateDeckStatistics(List<Battle> battles) {
        Map<List<String>, DeckStats> deckStatsMap = new HashMap<>();

        battles.forEach(battle -> {
            processPlayerDeck(battle.getPlayer1().getDeck(), "player1".equals(battle.getWinner()), deckStatsMap);
            processPlayerDeck(battle.getPlayer2().getDeck(), "player2".equals(battle.getWinner()), deckStatsMap);
        });

        return deckStatsMap;
    }

    private void processPlayerDeck(List<String> deck, boolean won, Map<List<String>, DeckStats> deckStatsMap) {
        List<String> sortedDeck = new ArrayList<>(deck);
        Collections.sort(sortedDeck);

        DeckStats stats = deckStatsMap.computeIfAbsent(
                sortedDeck,
                k -> new DeckStats(sortedDeck, 0, 0, 0.0)
        );

        stats.setTotalMatches(stats.getTotalMatches() + 1);
        if (won) {
            stats.setTotalWins(stats.getTotalWins() + 1);
        }
    }

    private List<DeckWinRateDTO> processAndFilterResults(Map<List<String>, DeckStats> deckStatsMap, double minWinRate) {
        return deckStatsMap.values().stream()
                .peek(this::calculateWinPercentage)
                .filter(stats -> stats.getWinPercentage() >= minWinRate)
                .sorted(Comparator.comparingDouble(DeckStats::getWinPercentage).reversed())
                .map(this::mapToDeckWinRateDTO)
                .collect(Collectors.toList());
    }

    private void calculateWinPercentage(DeckStats stats) {
        double winPercentage = stats.getTotalMatches() > 0
                ? (stats.getTotalWins() * 100.0) / stats.getTotalMatches()
                : 0.0;
        stats.setWinPercentage(Math.round(winPercentage * 100.0) / 100.0);
    }

    private DeckWinRateDTO mapToDeckWinRateDTO(DeckStats stats) {
        return new DeckWinRateDTO(
                stats.getDeck(),
                stats.getTotalMatches(),
                stats.getTotalWins(),
                stats.getWinPercentage()
        );
    }

    private Page<DeckWinRateDTO> paginateResults(List<DeckWinRateDTO> allResults, Pageable pageable) {
        int totalElements = allResults.size();
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min((startIndex + pageable.getPageSize()), totalElements);

        List<DeckWinRateDTO> pageContent = allResults.subList(startIndex, endIndex);

        return new PageImpl<>(pageContent, pageable, totalElements);
    }


    public long calculateDefeatsByCardCombo(String start, String end, List<String> cardCombo) {
        Date startDate = Date.from(Instant.parse(start));
        Date endDate = Date.from(Instant.parse(end));

        return battleRepository.countDefeatsByCardComboAndTimestampRange(startDate, endDate, cardCombo);
    }

    public long calculateVictoriesWithConditions(String start, String end, List<String> cardCombo, int trophyPercentage) {
        Date startDate = Date.from(Instant.parse(start));
        Date endDate = Date.from(Instant.parse(end));

        List<Long> result = battleRepository.countVictoriesWithConditions(startDate, endDate, cardCombo, trophyPercentage);

        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }

        return 0;
    }

    public List<ComboStats> getComboStats(String start, String end, int deckSize, int comboSize, double minWinPercentage) {
        try {
            Date startDate = Date.from(Instant.parse(start));
            Date endDate = Date.from(Instant.parse(end));

            if (comboSize <= 0 || comboSize > 8) {
                throw new IllegalArgumentException("O tamanho do combo deve estar entre 1 e 8");
            }

            if (minWinPercentage < 0 || minWinPercentage > 100) {
                throw new IllegalArgumentException("A porcentagem mínima de vitórias deve estar entre 0 e 100");
            }

            return battleRepository.findComboStats(
                    startDate,
                    endDate,
                    deckSize,
                    comboSize,
                    minWinPercentage
            );
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use o formato ISO-8601 (ex: 2025-04-11T00:00:00Z)");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular estatísticas de combos: " + e.getMessage(), e);
        }
    }


}