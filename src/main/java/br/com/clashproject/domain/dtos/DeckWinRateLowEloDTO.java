package br.com.clashproject.domain.dtos;

import java.util.List;

public record DeckWinRateLowEloDTO(
        List<String> deck,
        int totalMatches,
        int totalWins,
        double winPercentage,
        long trophies) {}
