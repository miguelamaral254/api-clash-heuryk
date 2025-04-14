package br.com.clashproject.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeckWinRate {
    private List<String> deck;
    private int totalMatches;
    private int totalWins;
    private double winPercentage;
}
