package ru.croc.java.school.lesson4_2.exeptions;

public class PutMapValueFailException extends RuntimeException {

    public PutMapValueFailException(String message) {
        super(message);
    }
}
