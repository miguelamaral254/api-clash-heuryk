package br.com.clashproject.domain.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record BattleResponseDTO(
        @NotNull
        String id,

        @NotNull
        Instant timestamp,

        @NotNull
        PlayerDTO player1,

        @NotNull
        PlayerDTO player2,

        @NotNull
        String winner
) {}