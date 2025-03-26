package javafiles.customexceptions;

public class ReadDuplicateKeyException extends RuntimeException {
    public ReadDuplicateKeyException(String message) {
        super(message);
    }
}
