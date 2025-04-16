package br.com.clashproject.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FrequentCard {
    private String card;
    private long count;

}
