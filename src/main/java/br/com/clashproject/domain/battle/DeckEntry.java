package br.com.clashproject.domain.battle;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeckEntry {
    private List<String> deck;
    private boolean won;
}