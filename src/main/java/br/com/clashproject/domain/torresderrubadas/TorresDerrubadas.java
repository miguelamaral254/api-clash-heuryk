package br.com.clashproject.domain.torresderrubadas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TorresDerrubadas {
    private int player1;
    private int player2;
}