package br.com.clashproject.domain.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ComboStatsDTO(

        @NotNull
        List<String> combo,

        @NotNull
        int totalMatches,

        @NotNull
        int totalWins,

        @NotNull
        int totalLosses,

        @NotNull
        double winPercentage
) {}