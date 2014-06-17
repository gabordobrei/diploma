package net.gabordobrei.diploma.simulator.model.network;

import java.util.List;

public abstract class Graph<V,E> {
	List<Vertex<V>> vertexes;
	List<Edge<E>> edges;
}
