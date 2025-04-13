package br.com.clashproject.domain.battle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComboStats {
    private List<String> combo;
    private int totalMatches;
    private int totalWins;
    private int totalLosses;
    private double winPercentage;
}