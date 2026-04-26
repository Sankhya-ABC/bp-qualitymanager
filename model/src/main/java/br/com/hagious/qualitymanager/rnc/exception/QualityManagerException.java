package br.com.hagious.qualitymanager.rnc.exception;

public class QualityManagerException extends Exception {

    public QualityManagerException(String message) {
        super(message);
    }

    public QualityManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
