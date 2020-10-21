package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Неориентированный граф.
 */
public class Graph<T> {

    /*
    При выборе структуры данных для хранения графа между мапой и множеством остановился на первой,
    тк в общем случае может быть необходимость обратиться к конкретной вершине графа, что будет удобно сделать по ключу.
     */
    /** - Структура данных для хранения графа.
     *  - Ключ может иметь любой тип данных, поддерживаемый Дженериками.
     *  - Ключом конкретной вершины является тот объект, для которого метод equals при сравнении с именем вершины
     *  вернет True.
     * */
    private final Map<T, Vertex<T>> graph = new HashMap<>();

    /** Счетчик компонент связности. */
    private int componentsCounter;

    public void buildByEdges(Set<Edge<T>> edges) {
        for (Edge<T> edge : edges) {
            T name1 = edge.getVertex1Name();
            T name2 = edge.getVertex2Name();
            if (!graph.containsKey(name1)) {
                graph.put(name1, new Vertex<>(name1));
            }
            if (!graph.containsKey(name2)) {
                graph.put(name2, new Vertex<>(name2));
            }
            graph.get(name1).addLinkedVertex(graph.get(name2));
            graph.get(name2).addLinkedVertex(graph.get(name1));
        }
    }

    public void buildByVertexes(Set<Vertex<T>> vertexes) {
        for (Vertex<T> vertex : vertexes) {
            graph.put(vertex.getName(), vertex);
        }
    }

    /**
     * Выделение компонент связности.
     * @return - мапа вида: ключ - номер компоненты связности; значение - множество вершин компоненты.
     */
    Map<Integer, Set<T>> findComponents() {
        componentsCounter = 0;
        iterateOverVertexes();
        return collectComponents();
    }

    /**
     * Перебор вершин для обхода в глубину и выделения компонент связности.
     */
    private void iterateOverVertexes() {
        for (T vertexName : graph.keySet()) {
            if (!graph.get(vertexName).isVisited()) {
                dfs(graph.get(vertexName), -1);
                ++componentsCounter;
            }
        }
    }

    /**
     * Обход в глубину.
     * @param vertex - вершина, с которой начинается обход;
     * @param component - номер текущей компоненты связности.
     */
    private void dfs(Vertex<T> vertex, int component) {
        vertex.setVisited(true);
        vertex.setComponent(component == -1 ? componentsCounter : component);
        for (Vertex<T> linkedVertex : vertex.getLinkedVertexes()) {
            if (!linkedVertex.isVisited()) {
                dfs(linkedVertex, component);
            }
        }
    }

    /**
     * Сбор информации о компонентах связности в одну структуру данных.
     * @return - мапа вида: ключ - номер компоненты связности; значение - множество имен вершин компоненты.
     */
    private Map<Integer, Set<T>> collectComponents() {
        Map<Integer, Set<T>> components = new HashMap<>();
        for (T vertex : graph.keySet()) {
            int currentComponent = graph.get(vertex).getComponent();
            if (components.containsKey(currentComponent)) {
                components.get(currentComponent).add(vertex);
            } else {
                Set<T> newComponent = new HashSet<>();
                newComponent.add(vertex);
                components.put(currentComponent, newComponent);
            }
        }
        return components;
    }

    int getComponentsCounter() {
        return componentsCounter;
    }
}
