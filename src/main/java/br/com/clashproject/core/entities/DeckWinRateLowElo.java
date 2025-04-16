package br.com.clashproject.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeckWinRateLowElo{

    private List<String> deck;
    private int totalMatches;
    private int totalWins;
    private double winPercentage;
    private long trophies;
}