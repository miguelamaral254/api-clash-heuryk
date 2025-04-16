package br.com.clashproject.core.entities;


import java.util.List;

public class DeckWinRateLowElo extends DeckWinRate{
    private long trophies;

    public DeckWinRateLowElo(List<String> deck, int totalMatches, int totalWins, double winPercentage) {
        super(deck, totalMatches, totalWins, winPercentage);
    }
}
