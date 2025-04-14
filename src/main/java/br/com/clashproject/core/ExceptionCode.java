package br.com.clashproject.core;

public interface ExceptionCode {
    String getCode();
    String getMessage();
    int getHttpStatus();
}
