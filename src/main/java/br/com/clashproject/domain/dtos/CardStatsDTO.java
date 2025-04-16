package br.com.clashproject.domain.dtos;

public class CardStatsDTO {
    private String id; // Nome da carta
    private int empateCount;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getEmpateCount() { return empateCount; }
    public void setEmpateCount(int empateCount) { this.empateCount = empateCount; }
}
