package br.com.clashproject.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private String nickname;
    private int trophies;
    private int level;
    private int towersDestroyed;
    private List<String> deck;
}