package br.com.clashproject.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeckEntry {
    private List<String> deck;
    private boolean won;
}