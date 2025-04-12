package br.com.clashproject.domain.battle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BattleStats {
    private int totalMatches;
    private int totalWins;
    private int totalLosses;
    private double winPercentage;
    private double lossPercentage;
}