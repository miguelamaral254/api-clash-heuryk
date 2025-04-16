package br.com.clashproject.domain.dtos;

public class WorstWinrateCardDTO {
    private String carta;
    private long derrotas;

    public WorstWinrateCardDTO() {
    }

    public WorstWinrateCardDTO(String carta, long derrotas) {
        this.carta = carta;
        this.derrotas = derrotas;
    }

    public String getCarta() {
        return carta;
    }

    public void setCarta(String carta) {
        this.carta = carta;
    }

    public long getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(long derrotas) {
        this.derrotas = derrotas;
    }
}
