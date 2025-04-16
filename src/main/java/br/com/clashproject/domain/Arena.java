package br.com.clashproject.domain;
public enum Arena {
    ARENA_1(0, 300),
    ARENA_2(300, 600),
    ARENA_3(600, 1000),
    ARENA_4(1000, 1300),
    ARENA_5(1300, 1600),
    ARENA_6(1600, 2000),
    ARENA_7(2000, 2300),
    ARENA_8(2300, 2600),
    ARENA_9(2600, 3000),
    ARENA_10(3000, 3400),
    ARENA_11(3400, 3800),
    ARENA_12(3800, 4200),
    ARENA_13(4200, 4600),
    ARENA_14(4600, 5000),
    ARENA_15(5000, 5500),
    ARENA_16(5500, 6000),
    ARENA_17(6000, 6500),
    ARENA_18(6500, 7000),
    ARENA_19(7000, 7500),
    ARENA_20(7500, 8000),
    ARENA_21(8000, 8500),
    ARENA_22(8500, 9000),
    ARENA_23(9000, Integer.MAX_VALUE);

    private final int minTrophies;
    private final int maxTrophies;

    Arena(int minTrophies, int maxTrophies) {
        this.minTrophies = minTrophies;
        this.maxTrophies = maxTrophies;
    }

    public int getMinTrophies() {
        return minTrophies;
    }

    public int getMaxTrophies() {
        return maxTrophies;
    }
}