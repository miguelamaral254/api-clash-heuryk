package br.com.clashproject.domain.dtos;


public record BattleStatsDTO(
        int totalMatches,
        int totalWins,
        int totalLosses,
        double winPercentage,
        double lossPercentage
) {}