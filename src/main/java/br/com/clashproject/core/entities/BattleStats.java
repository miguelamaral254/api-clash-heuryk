package br.com.clashproject.core.entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BattleStats {

    @NotNull
    private int totalMatches;

    @NotNull
    private int totalWins;

    @NotNull
    private int totalLosses;

    @NotNull
    private double winPercentage;

    @NotNull
    private double lossPercentage;
}