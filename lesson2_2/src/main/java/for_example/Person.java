package for_example;

/**
 * Тестовый класс для проверки случая, когда именами вершин являются экземпляры класса, для которых метод equals будет
 * сравнивать ссылки.
 */
public class Person {

    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
