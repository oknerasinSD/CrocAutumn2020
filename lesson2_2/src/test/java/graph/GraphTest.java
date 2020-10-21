package graph;

import for_example.Person;
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
     * Вершины для примера 3.
     * Рисунок графа: https://ibb.co/Xj3rWm5
     */
    private Vertex<Person> petr = new Vertex<>(new Person("Petr"));
    private Vertex<Person> maria = new Vertex<>(new Person("Maria"));
    private Vertex<Person> yuri = new Vertex<>(new Person("Yuri"));
    private Vertex<Person> anna = new Vertex<>(new Person("Anna"));
    private Vertex<Person> andrey = new Vertex<>(new Person("Andrey"));
    private Vertex<Person> dmitri = new Vertex<>(new Person("Dmitri"));
    private Vertex<Person> nadezhda = new Vertex<>(new Person("Nadezhda"));
    private Vertex<Person> boris = new Vertex<>(new Person("Boris"));
    private Vertex<Person> irina = new Vertex<>(new Person("Irina"));
    private Vertex<Person> kirill = new Vertex<>(new Person("Kirill"));
    private Vertex<Person> ekaterina = new Vertex<>(new Person("Ekaterina"));
    private Set<Vertex<Person>> vertexes = new HashSet<>(
            Arrays.asList(petr, maria, yuri, anna, andrey, dmitri, nadezhda, boris, irina, kirill, ekaterina)
    );

    /**
     * Метод установки связей между вершинами для примера 3.
     */
    private void linkVertexes() {

        petr.addLinkedVertexes(new HashSet<>(Arrays.asList(maria, yuri, anna, andrey)));
        maria.addLinkedVertexes(new HashSet<>(Arrays.asList(petr, yuri, anna, andrey)));
        yuri.addLinkedVertexes(new HashSet<>(Arrays.asList(petr, maria)));
        anna.addLinkedVertexes(yuri.getLinkedVertexes());
        andrey.addLinkedVertexes(yuri.getLinkedVertexes());

        dmitri.addLinkedVertexes(new HashSet<>(Arrays.asList(nadezhda, boris)));
        nadezhda.addLinkedVertexes(new HashSet<>(Arrays.asList(dmitri, boris)));
        boris.addLinkedVertexes(new HashSet<>(Arrays.asList(dmitri, nadezhda, irina)));
        irina.addLinkedVertexes(new HashSet<>(Arrays.asList(boris, kirill)));
        kirill.addLinkedVertexes(new HashSet<>(Arrays.asList(boris, irina, ekaterina)));
        ekaterina.addLinkedVertex(kirill);
    }

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
        Поскольку мое разбиение возвращает множество имен вершин, а не множество самих вершин, в случае использования
        в качестве имени вершины экземпляров самописного класса получается вот такая многоэтажная проверка,
        т.к. объекты petr, maria и т.д. в данном примере являются экземплярами класса Vertex,
        а множества в мапе содержат экземпляры класса Person.
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
        Проверяем для случая, когда на вход подаются пустые множества ребер или вершин.
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
