package ru.croc.java.school.lesson6_1;

/**
 * Вспомогательный класс, управляющий потоком-демоном, который очищает определенную директорию с установленным временным
 * интервалом.
 */
public class CleanDirectory {

    /** Поток-демон, очищающий директорию с определенным временным интервалом */
    private Thread thread;

    /**
     * Конструктор вспомогательного класса, управляющего потоком-демоном, который очищает определенную директорию с
     * установленным временным интервалом.
     * @param time - время в милисекундах.
     * @param directory - путь до директории.
     */
    public CleanDirectory(long time, String directory) {
        thread = new Thread(new CleanDirectoryThread(time, directory));
        thread.setDaemon(true);
    }

    /**
     * Запуск потока.
     */
    public void startThread() {
        thread.start();
    }

    /**
     * Остановка потока.
     * @throws InterruptedException - выбрасывается, если поток, завершения которого мы ожидаем в методе join(),
     * был прерван, и этот случай не был обработан.
     */
    public void stopThread() throws InterruptedException {
        thread.interrupt();
        thread.join();
    }

    public Thread getThread() {
        return thread;
    }
}
