package graph;

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

    /** Множество вершин, которые соединины ребром с текущей. */
    private Set<Vertex<T>> linkedVertexes = new HashSet<>();

    /** Имя вершины. */
    private T name;

    public Vertex(T name) {
        this.name = name;
    }

    /**
     * Добавляем информацию о соседстве.
     * @param vertex - соседняя вершина.
     */
    public void addLinkedVertex(Vertex<T> vertex) {
        linkedVertexes.add(vertex);
    }

    public void addLinkedVertexes(Set<Vertex<T>> vertexes) {
        linkedVertexes.addAll(vertexes);
    }

    public boolean isVisited() {
        return visited;
    }

    public int getComponent() {
        return component;
    }

    public Set<Vertex<T>> getLinkedVertexes() {
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
