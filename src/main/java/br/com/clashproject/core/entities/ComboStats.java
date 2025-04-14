package br.com.clashproject.core.entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComboStats {

    @NotNull
    private List<String> combo;

    @NotNull
    private int totalMatches;

    @NotNull
    private int totalWins;

    @NotNull
    private int totalLosses;

    @NotNull
    private double winPercentage;
}