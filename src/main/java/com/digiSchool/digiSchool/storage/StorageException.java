package com.digiSchool.digiSchool.storage;

/**
 * Exception levée lors d'une erreur d'accès au stockage MinIO.
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
