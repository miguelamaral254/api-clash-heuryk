package br.com.clashproject.domain.entities;


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
    private Instant timestamp;
    private Player player1;
    private Player player2;
    private String winner;
}