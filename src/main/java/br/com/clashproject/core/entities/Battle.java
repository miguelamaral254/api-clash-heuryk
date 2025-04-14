package br.com.clashproject.core.entities;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("battles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Battle {

    @Id
    private String id;

    @NotNull
    private Instant timestamp;

    @NotNull
    private Player player1;

    @NotNull
    private Player player2;

    @NotNull
    private String winner;
}