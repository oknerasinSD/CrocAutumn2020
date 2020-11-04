package ru.croc.java.school.lesson2_2.graph;

import ru.croc.java.school.lesson2_2.for_example.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class GraphTest {

    /**
     *  Связи для примера 1.
     *  Рисунок графа: https://ibb.co/1X0W8V2
     * */
    private Integer[][] links1 = {
            {1, 2},
            {1, 3},
            {2, 3},
            {2, 4},
            {3, 4},
            {6, 8},
            {10, 9},
            {10, 7},
            {7, 9},
            {3, 5},
            {4, 5}
    };

    /**
     * Связи для примера 2.
     * Рисунок графа: https://ibb.co/6PXGmNy
     */
    private String[][] links2 = {
            {"Краснодар", "Ростов"},
            {"Ростов", "Москва"},
            {"Москва", "Санкт-Петербург"},
            {"Санкт-Петербург", "Минск"},
            {"Минск", "Киев"},
            {"Киев", "Краснодар"},
            {"Вашингтон", "Нью-Йорк"},
            {"Нью-Йорк", "Бостон"},
            {"Бостон", "Вашингтон"},
            {"Вашингтон", "Москва"}
    };

    /**
     * Вершины для примера 3 - имена вершин являются экземплярами класса, для которых метод equals будет сравнивать
     * ссылки, а во входном множестве вершин имеются имеются дубли в виде разных экземпляров класса Vertex,
     * имеющих ссылку на одинаковый объект в поле name и совпадающие поэлементно множества в поле linkedVertex.
     * Рисунок графа: https://ibb.co/Xj3rWm5
     */
    private Vertex<Person> petr2 = new Vertex<>(new Person("Petr"));
    private Vertex<Person> petr = new Vertex<>(petr2.getName());
    private Vertex<Person> maria = new Vertex<>(new Person("Maria"));
    private Vertex<Person> yuri = new Vertex<>(new Person("Yuri"));
    private Vertex<Person> anna = new Vertex<>(new Person("Anna"));
    private Vertex<Person> andrey = new Vertex<>(new Person("Andrey"));
    private Vertex<Person> dmitri = new Vertex<>(new Person("Dmitri"));
    private Vertex<Person> nadezhda = new Vertex<>(new Person("Nadezhda"));
    private Vertex<Person> boris3 = new Vertex<>(new Person("Boris"));
    private Vertex<Person> boris = new Vertex<>(boris3.getName());
    private Vertex<Person> boris2 = new Vertex<>(boris3.getName());
    private Vertex<Person> irina = new Vertex<>(new Person("Irina"));
    private Vertex<Person> kirill = new Vertex<>(new Person("Kirill"));
    private Vertex<Person> ekaterina = new Vertex<>(new Person("Ekaterina"));
    private Set<Vertex<Person>> vertexes = new HashSet<>(
            Arrays.asList(petr, petr2, maria, yuri, anna, andrey, dmitri, nadezhda,
                    boris, boris2, boris3, irina, kirill, ekaterina)
    );

    /**
     * Установка связей между вершинами для примера 3.
     */
    private void linkVertexes() {

        petr.addLinkedVertexes(new HashSet<>(
                Arrays.asList(maria.getName(), yuri.getName(), anna.getName(), andrey.getName()))
        );
        petr2.addLinkedVertexes(new HashSet<>(
                Arrays.asList(maria.getName(), yuri.getName(), anna.getName(), andrey.getName()))
        );
        maria.addLinkedVertexes(new HashSet<>(
                Arrays.asList(petr.getName(), yuri.getName(), anna.getName(), andrey.getName()))
        );
        yuri.addLinkedVertexes(new HashSet<>(Arrays.asList(petr.getName(), maria.getName())));
        anna.addLinkedVertexes(yuri.getLinkedVertexes());
        andrey.addLinkedVertexes(yuri.getLinkedVertexes());

        dmitri.addLinkedVertexes(new HashSet<>(Arrays.asList(nadezhda.getName(), boris.getName())));
        nadezhda.addLinkedVertexes(new HashSet<>(Arrays.asList(dmitri.getName(), boris.getName())));
        boris.addLinkedVertexes(new HashSet<>(Arrays.asList(dmitri.getName(), nadezhda.getName(), irina.getName())));
        boris2.addLinkedVertexes(new HashSet<>(Arrays.asList(dmitri.getName(), nadezhda.getName(), irina.getName())));
        boris3.addLinkedVertexes(new HashSet<>(Arrays.asList(dmitri.getName(), nadezhda.getName(), irina.getName())));
        irina.addLinkedVertexes(new HashSet<>(Arrays.asList(boris.getName(), kirill.getName())));
        kirill.addLinkedVertexes(new HashSet<>(Arrays.asList(boris.getName(), irina.getName(), ekaterina.getName())));
        ekaterina.addLinkedVertex(kirill.getName());
    }

    /*
    Пример 4 - проверка корректности работы программы при подаче на вход пустых множеств вершин или ребер.
     */
    private Set<Vertex<Integer>> emptyVertexSet = Collections.emptySet();
    private Set<Edge<Integer>> emptyEdgesSet = Collections.emptySet();

    /**
     * Построение множества ребер по заданным связям для примеров 1 и 2.
     * @param links - связи.
     * @param <T> - тип имен вершин.
     * @return - множество ребер графа.
     */
    private <T> Set<Edge<T>> buildEdgesSet(T[][] links) {
        Set<Edge<T>> edges = new HashSet<>();
        for (T[] link : links) {
            edges.add(new Edge<>(link[0], link[1]));
        }
        return edges;
    }

    @Test
    public void findComponentsTest() {

        Graph<Integer> graph1 = new Graph<>();
        graph1.buildByEdges(buildEdgesSet(links1));
        Map<Integer, Set<Integer>> components1 = graph1.findComponents();
        /*
        Проверяем количество компонент связности.
         */
        Assertions.assertEquals(3, graph1.getComponentsCounter());
        /*
        Проверяем правильность разбиения.
         */
        for (Integer component : components1.keySet()) {
            switch (components1.get(component).size()) {
                case 2:
                    Assertions.assertTrue(components1.get(component).contains(6));
                    Assertions.assertTrue(components1.get(component).contains(8));
                    break;
                case 3:
                    Assertions.assertTrue(components1.get(component).contains(7));
                    Assertions.assertTrue(components1.get(component).contains(9));
                    Assertions.assertTrue(components1.get(component).contains(10));
                    break;
            }
        }




        Graph<String> graph2 = new Graph<>();
        graph2.buildByEdges(buildEdgesSet(links2));
        Map<Integer, Set<String>> components2 = graph2.findComponents();
        /*
        Проверяем, что в графе всего одна компонента связности, в которую входят все 9 вершин.
         */
        Assertions.assertEquals(1, components2.size());
        Assertions.assertEquals(9, components2.get(0).size());




        Graph<Person> graph3 = new Graph<>();
        linkVertexes();
        graph3.buildByVertexes(vertexes);
        Map<Integer, Set<Person>> components3 = graph3.findComponents();

        Assertions.assertEquals(2, components3.size());

/*
        Этот тест упадет при хранении в поле linkedVertexes класса Vertex не имен соседей, а ссылок на них.

        Vertex<Person> checkPetr = new Vertex<>(new Person(""));
        for (Vertex<Person> linkedVertex : anna.getLinkedVertexes()) {
            if ("Petr".equals(linkedVertex.getName().getName())) {
                checkPetr = linkedVertex;
            }
        }
        Assertions.assertTrue(graph3.hasVertex(checkPetr));
*/

        /*
        Проверяем содержимое компонент связности.
         */
        for (Integer component : components3.keySet()) {
            Set<Person> currentComponent = components3.get(component);
            switch (components3.get(component).size()) {
                case 5:
                    Assertions.assertTrue(
                            currentComponent.containsAll(new HashSet<>(Arrays.asList(
                                    petr.getName(),
                                    maria.getName(),
                                    yuri.getName(),
                                    anna.getName(),
                                    andrey.getName())
                                    )
                            )
                    );
                    break;
                case 6:
                    Assertions.assertTrue(
                            currentComponent.containsAll(new HashSet<>(Arrays.asList(
                                    dmitri.getName(),
                                    nadezhda.getName(),
                                    boris.getName(),
                                    irina.getName(),
                                    kirill.getName(),
                                    ekaterina.getName())
                                    )
                            )
                    );
                    break;
            }
        }



        /*
        Проверяем случай, когда на вход подаются пустые множества ребер или вершин.
         */
        Graph<Integer> graph4 = new Graph<>();
        graph4.buildByEdges(emptyEdgesSet);
        Map<Integer, Set<Integer>> components4 = graph4.findComponents();
        Assertions.assertEquals(0, components4.size());
        graph4.buildByVertexes(emptyVertexSet);
        components4 = graph4.findComponents();
        Assertions.assertEquals(0, components4.size());
    }
}
