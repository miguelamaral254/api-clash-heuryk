package br.com.clashproject.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BattleCreateDTO(
        @NotNull(message = "Player1 é obrigatório")
        @Valid
        PlayerDTO player1,

        @NotNull(message = "Player2 é obrigatório")
        @Valid
        PlayerDTO player2,

        @NotBlank(message = "Winner é obrigatório")
        String winner
) {}