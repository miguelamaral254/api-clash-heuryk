package br.com.clashproject.domain.dtos;

public class EstatisticaDeckDTO {
    private long count;
    private double averageTrophies;

    public EstatisticaDeckDTO() {
    }

    public EstatisticaDeckDTO(long count, double averageTrophies) {
        this.count = count;
        this.averageTrophies = averageTrophies;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getAverageTrophies() {
        return averageTrophies;
    }

    public void setAverageTrophies(double averageTrophies) {
        this.averageTrophies = averageTrophies;
    }
}