package ru.croc.java.school.lesson6_1;

import java.io.File;
import java.util.Objects;

/**
 * Поток очищающий установленную директорию с определенным временным интервалом.
 */
class CleanDirectoryThread implements Runnable {

    /** Временной интервал, с которым происходит очистка директории  */
    private long time;

    /** Очищаемая директория */
    private final String directory;

    /**
     * Конструктор класса-потока-демона, который очищает определенную директорию с установленным временным интервалом.
     * @param time - время в милисекундах.
     * @param directory - путь к очищаемой директории.
     */
    public CleanDirectoryThread(long time, String directory) {
        this.time = time;
        this.directory = directory;
    }

    @Override
    /**
     * Удаление всех файлов из директории с установленным временным интервалом.
     */
    public void run() {
        File directoryToClean = new File(directory);
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (directory) {
                for (File file : Objects.requireNonNull(directoryToClean.listFiles())) {
                    file.delete();
                }
            }
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
