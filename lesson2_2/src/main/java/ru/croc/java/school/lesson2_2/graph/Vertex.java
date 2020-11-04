package ru.croc.java.school.lesson2_2.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Вершина графа.
 */
public class Vertex<T> {

    /** Флаг, показывающий, посещали ли уже данную вершину при обходе */
    private boolean visited = false;

    /** Номер компоненты связности, которой принадлежит вершина. */
    private int component = -1;

    /*
    - Храню в данном поле множество имен вершин, а не ссылки на вершины, тк в этом случае упрощается решение
    для ситуации, когда на вход подается множество с повторяющимися вершинами.
    - Предположим, что у нас есть дублированная вершина 1, связанная с вершиной 2. При этом у вершины 2 во множестве
    соседей есть ссылка только на одну из вершин 1. Тогда возможна ситуация, при которой в граф будет добавлен
    тот экземпляр вершины 1, ссылки на который нет у вершины 2.
    - При текущей реализации все еще можно получить нужного соседа по имени за время O(1) обращением к мапе,
    в которой хранится граф (метод getVertexByName(T name) класса Graph).
     */
    /** Множество имен вершин, которые соединины ребром с текущей. */
    private Set<T> linkedVertexes = new HashSet<>();

    /** Имя вершины. */
    private T name;

    public Vertex(T name) {
        this.name = name;
    }

    /**
     * Добавляем информацию о соседстве.
     * @param vertex - соседняя вершина.
     */
    public void addLinkedVertex(T vertex) {
        linkedVertexes.add(vertex);
    }

    public void addLinkedVertexes(Set<T> vertexes) {
        linkedVertexes.addAll(vertexes);
    }

    public boolean isVisited() {
        return visited;
    }

    public int getComponent() {
        return component;
    }

    public Set<T> getLinkedVertexes() {
        return linkedVertexes;
    }

    public T getName() {
        return name;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setComponent(int component) {
        this.component = component;
    }
}
