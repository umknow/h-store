package edu.brown.graphs;

import java.util.List;

import org.voltdb.catalog.*;

import edu.brown.utils.JSONSerializable;
import edu.uci.ics.jung.graph.Graph;

/**
 * 
 * @author pavlo
 *
 */
public interface IGraph<V extends AbstractVertex, E extends AbstractEdge> extends Graph<V, E>, Cloneable, JSONSerializable {
    public V getVertex(CatalogType catalog_item);
    public V getVertex(String catalog_key);
    public V getVertex(Long element_id);
    public void pruneIsolatedVertices();
    public String getName();
    public void setName(String name);
    public List<E> getPath(V source, V target);
    public List<E> getPath(List<V> path);
    public String debug();
}
