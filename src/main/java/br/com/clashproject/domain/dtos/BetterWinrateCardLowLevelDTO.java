package br.com.clashproject.domain.dtos;

public class BetterWinrateCardLowLevelDTO {
    private String carta;
    private double taxaVitoria;

    public BetterWinrateCardLowLevelDTO() {
    }

    public BetterWinrateCardLowLevelDTO(String carta, double taxaVitoria) {
        this.carta = carta;
        this.taxaVitoria = taxaVitoria;
    }

    public String getCarta() {
        return carta;
    }

    public void setCarta(String carta) {
        this.carta = carta;
    }

    public double getTaxaVitoria() {
        return taxaVitoria;
    }

    public void setTaxaVitoria(double taxaVitoria) {
        this.taxaVitoria = taxaVitoria;
    }
}
