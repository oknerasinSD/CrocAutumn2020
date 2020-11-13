package ru.croc.java.school.lesson4_2.exeptions;

public class PutMapKeyFailException extends RuntimeException {

    public PutMapKeyFailException(String message) {
        super(message);
    }
}
