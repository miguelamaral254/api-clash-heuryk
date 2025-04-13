package br.com.clashproject.domain.dtos;


import java.util.List;

public record PlayerDTO(
        String nickname,
        int trophies,
        int level,
        List<String> deck
) {}