package org.iii.converter.exception;

public class DatasetFileException extends RuntimeException {
    public DatasetFileException(Throwable cause) {
        super("數據集檔案處理發生錯誤", cause);
    }
}
