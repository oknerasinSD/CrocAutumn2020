package ru.croc.java.school.lesson2_2.graph;

/**
 * Ребро.
 */
public class Edge<T> {

    private T vertex1Name;
    private T vertex2Name;

    public Edge(T vertex1Name, T vertex2Name) {
        this.vertex1Name = vertex1Name;
        this.vertex2Name = vertex2Name;
    }

    public T getVertex1Name() {
        return vertex1Name;
    }

    public T getVertex2Name() {
        return vertex2Name;
    }
}
