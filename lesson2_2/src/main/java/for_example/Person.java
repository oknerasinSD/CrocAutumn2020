package for_example;

/**
 * Тестовый класс для проверки случая, когда именами вершин являются экземпляры самописного класса.
 */
public class Person {

    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
