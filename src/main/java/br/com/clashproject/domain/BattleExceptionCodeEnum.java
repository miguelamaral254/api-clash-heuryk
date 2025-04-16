package br.com.clashproject.domain;

import br.com.clashproject.core.ExceptionCode;

public enum BattleExceptionCodeEnum implements ExceptionCode {

    BATTLE_NOT_FOUND("Battle not found", "BATTLEEXCEP-404", 404),
    BATTLE_IS_EMPTY("There is any Battle yet", "BATTLEEXCEP-404", 404),
    INVALID_WIN_PERCENTAGE("Invalid win percentage, it must be between 0 and 100", "BATTLEEXCEP-003", 400),
    INVALID_COMBO("Invalid combo size", "BATTLEEXCEP-002", 400),
    WIN_RATE_CALCULATION_ERROR("Error calculating win rate", "BATTLEEXCEP-003", 500);
    //CARD_NOT_FOUND("Card not found", "BATTLEEXCEP-404", 404);

    private final String message;
    private final String code;
    private final int httpStatus;

    BattleExceptionCodeEnum(String message, String code, int httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }
}