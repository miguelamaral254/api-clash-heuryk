package br.com.clashproject.domain.dtos;


import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PlayerDTO(

        @NotNull
        String nickname,

        @NotNull
        int trophies,

        @NotNull
        int level,

        @NotNull
        List<String> deck
) {}