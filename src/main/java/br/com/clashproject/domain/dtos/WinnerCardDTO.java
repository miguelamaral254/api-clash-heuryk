package br.com.clashproject.domain.dtos;

public class WinnerCardDTO {
    private String carta;
    private long count;       // quantidade de vitórias
    private double mediaNivel; // média de nível

    public WinnerCardDTO() {
    }

    public WinnerCardDTO(String carta, long count, double mediaNivel) {
        this.carta = carta;
        this.count = count;
        this.mediaNivel = mediaNivel;
    }

    public String getCarta() {
        return carta;
    }

    public void setCarta(String carta) {
        this.carta = carta;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getMediaNivel() {
        return mediaNivel;
    }

    public void setMediaNivel(double mediaNivel) {
        this.mediaNivel = mediaNivel;
    }
}
