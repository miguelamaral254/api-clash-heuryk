package br.com.clashproject.domain;

import br.com.clashproject.core.BusinessException;
import br.com.clashproject.core.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;

    @Transactional(readOnly = true)
    public Page<Battle> getAllBattles(Pageable pageable) {
        try {
            return battleRepository.findAll(pageable);
        } catch (Exception e) {
            throw new BusinessException(BattleExceptionCodeEnum.BATTLE_IS_EMPTY);
        }
    }
    @Transactional
    public Battle createBattle(Battle battle) {
        try {
            battle.setTimestamp(Instant.now());
            return battleRepository.save(battle);
        } catch (Exception e) {
            throw new BusinessException(BattleExceptionCodeEnum.BATTLE_NOT_FOUND);
        }
    }


    @Transactional(readOnly = true)
    public BattleStats calculateWinLossPercentage(String start, String end, String cardName) {
        try {
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
        } catch (Exception e) {
            throw new BusinessException(BattleExceptionCodeEnum.WIN_RATE_CALCULATION_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Page<DeckWinRate> getDeckWinRates(String start, String end, double minWinRate, Pageable pageable) {
        try {
            List<Battle> battles = findBattlesInDateRange(start, end);
            Map<List<String>, DeckWinRate> deckWinRateMap = calculateDeckWinRateStatistics(battles);
            List<DeckWinRate> filteredResults = processAndFilterDeckWinRateResults(deckWinRateMap, minWinRate);

            return paginateResults(filteredResults, pageable);
        } catch (Exception e) {
            throw new BusinessException(BattleExceptionCodeEnum.WIN_RATE_CALCULATION_ERROR);
        }
    }

    private List<Battle> findBattlesInDateRange(String start, String end) {
        Date startDate = Date.from(Instant.parse(start));
        Date endDate = Date.from(Instant.parse(end));

        return battleRepository.findBattlesByTimestampRange(startDate, endDate);
    }

    private Map<List<String>, DeckWinRate> calculateDeckWinRateStatistics(List<Battle> battles) {
        Map<List<String>, DeckWinRate> deckWinRateMap = new HashMap<>();

        battles.forEach(battle -> {
            processPlayerDeck(battle.getPlayer1().getDeck(), "player1".equals(battle.getWinner()), deckWinRateMap);
            processPlayerDeck(battle.getPlayer2().getDeck(), "player2".equals(battle.getWinner()), deckWinRateMap);
        });

        return deckWinRateMap;
    }

    private void processPlayerDeck(List<String> deck, boolean won, Map<List<String>, DeckWinRate> deckWinRateMap) {
        List<String> sortedDeck = new ArrayList<>(deck);
        Collections.sort(sortedDeck);

        DeckWinRate stats = deckWinRateMap.computeIfAbsent(
                sortedDeck,
                k -> new DeckWinRate(sortedDeck, 0, 0, 0.0)
        );

        stats.setTotalMatches(stats.getTotalMatches() + 1);
        if (won) {
            stats.setTotalWins(stats.getTotalWins() + 1);
        }
    }

    private List<DeckWinRate> processAndFilterDeckWinRateResults(Map<List<String>, DeckWinRate> deckWinRateMap, double minWinRate) {
        return deckWinRateMap.values().stream()
                .peek(this::calculateWinPercentage)
                .filter(stats -> stats.getWinPercentage() >= minWinRate)
                .sorted(Comparator.comparingDouble(DeckWinRate::getWinPercentage).reversed())
                .collect(Collectors.toList());
    }

    private void calculateWinPercentage(DeckWinRate stats) {
        double winPercentage = stats.getTotalMatches() > 0
                ? (stats.getTotalWins() * 100.0) / stats.getTotalMatches()
                : 0.0;
        stats.setWinPercentage(Math.round(winPercentage * 100.0) / 100.0);
    }

    private Page<DeckWinRate> paginateResults(List<DeckWinRate> allResults, Pageable pageable) {
        int totalElements = allResults.size();
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min((startIndex + pageable.getPageSize()), totalElements);

        List<DeckWinRate> pageContent = allResults.subList(startIndex, endIndex);

        return new PageImpl<>(pageContent, pageable, totalElements);
    }

    @Transactional(readOnly = true)
    public long calculateDefeatsByCardCombo(String start, String end, List<String> cardCombo) {
        try {
            Date startDate = Date.from(Instant.parse(start));
            Date endDate = Date.from(Instant.parse(end));

            return battleRepository.countDefeatsByCardComboAndTimestampRange(startDate, endDate, cardCombo);
        } catch (Exception e) {
            throw new BusinessException(BattleExceptionCodeEnum.WIN_RATE_CALCULATION_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public long calculateVictoriesWithConditions(String start, String end, List<String> cardCombo, int trophyPercentage) {
        try {
            Date startDate = Date.from(Instant.parse(start));
            Date endDate = Date.from(Instant.parse(end));

            List<Long> result = battleRepository.countVictoriesWithConditions(startDate, endDate, cardCombo, trophyPercentage);

            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }

            return 0;
        } catch (Exception e) {
            throw new BusinessException(BattleExceptionCodeEnum.WIN_RATE_CALCULATION_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public Page<ComboStats> getComboStats(String start, String end, int deckSize, int comboSize, double minWinPercentage, Pageable pageable) {
        try {
            Date startDate = Date.from(Instant.parse(start));
            Date endDate = Date.from(Instant.parse(end));

            if (comboSize <= 0 || comboSize > 8) {
                throw new BusinessException(BattleExceptionCodeEnum.INVALID_COMBO);
            }

            if (minWinPercentage < 0 || minWinPercentage > 100) {
                throw new BusinessException(BattleExceptionCodeEnum.INVALID_WIN_PERCENTAGE);
            }

            List<ComboStats> comboStatsList = battleRepository.findComboStats(
                    startDate,
                    endDate,
                    deckSize,
                    comboSize,
                    minWinPercentage
            );

            int totalElements = comboStatsList.size();
            int startIndex = (int) pageable.getOffset();
            int endIndex = Math.min((startIndex + pageable.getPageSize()), totalElements);

            List<ComboStats> pageContent = comboStatsList.subList(startIndex, endIndex);

            return new PageImpl<>(pageContent, pageable, totalElements);
        } catch (Exception e) {
            throw new BusinessException(BattleExceptionCodeEnum.WIN_RATE_CALCULATION_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Page<FrequentCard> getMostFrequentCardsInLostDecks(String start, String end, Pageable pageable) {
        try {
            Date startDate = Date.from(Instant.parse(start));
            Date endDate = Date.from(Instant.parse(end));

            List<FrequentCard> frequentCardsList = battleRepository.getMostFrequentCardsInLostDecks(startDate, endDate);

            int startIndex = (int) pageable.getOffset();
            int endIndex = Math.min(startIndex + pageable.getPageSize(), frequentCardsList.size());

            List<FrequentCard> pagedResults = frequentCardsList.subList(startIndex, endIndex);

            return new PageImpl<>(pagedResults, pageable, frequentCardsList.size());
        } catch (Exception e) {
            throw new BusinessException(BattleExceptionCodeEnum.CARDS_RETRIEVAL_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public Page<DeckWinRate> getDeckWinRatesPerArena(String start, String end, double minWinRate, String arena, Pageable pageable) {
        try {
            Arena selectedArena = Arena.valueOf(arena.toUpperCase());
            int minTrophies = selectedArena.getMinTrophies();
            int maxTrophies = selectedArena.getMaxTrophies();

            List<Battle> battles = findBattlesInDateRange(start, end);

            Map<List<String>, DeckWinRate> deckWinRateMap = calculateDeckWinRateStatistics(battles, minTrophies, maxTrophies);

            List<DeckWinRate> filteredResults = processAndFilterDeckWinRateResults(deckWinRateMap, minWinRate);

            return paginateResults(filteredResults, pageable);
        } catch (Exception e) {
            throw new BusinessException(BattleExceptionCodeEnum.WIN_RATE_CALCULATION_ERROR);
        }
    }

    private Map<List<String>, DeckWinRate> calculateDeckWinRateStatistics(List<Battle> battles, int minTrophies, int maxTrophies) {
        Map<List<String>, DeckWinRate> deckWinRateMap = new HashMap<>();

        battles.forEach(battle -> {
            if (isPlayerInTrophyRange(battle.getPlayer1().getTrophies(), minTrophies, maxTrophies)) {
                processPlayerDeck(battle.getPlayer1().getDeck(), "player1".equals(battle.getWinner()), deckWinRateMap);
            }

            if (isPlayerInTrophyRange(battle.getPlayer2().getTrophies(), minTrophies, maxTrophies)) {
                processPlayerDeck(battle.getPlayer2().getDeck(), "player2".equals(battle.getWinner()), deckWinRateMap);
            }
        });

        return deckWinRateMap;
    }

    private boolean isPlayerInTrophyRange(int trophies, int minTrophies, int maxTrophies) {
        return trophies >= minTrophies && trophies <= maxTrophies;
    }
}