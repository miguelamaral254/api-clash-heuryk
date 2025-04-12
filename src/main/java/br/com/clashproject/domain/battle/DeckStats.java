package br.com.clashproject.domain.battle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeckStats {
    private List<String> deck;
    private int totalMatches;
    private int totalWins;
    private double winPercentage;
}