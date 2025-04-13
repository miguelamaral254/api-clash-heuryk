package br.com.clashproject.domain.dtos;

import java.util.List;

public record DeckWinRateDTO(
        List<String> deck,
        int totalMatches,
        int totalWins,
        double winPercentage
) {}