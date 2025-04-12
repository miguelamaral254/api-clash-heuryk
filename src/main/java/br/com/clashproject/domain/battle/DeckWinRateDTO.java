package br.com.clashproject.domain.battle;

import java.util.List;

public record DeckWinRateDTO(
        List<String> deck,
        int totalMatches,
        int totalWins,
        double winPercentage
) {}