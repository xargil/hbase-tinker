package hbgraph.structure;

import com.tinkerpop.gremlin.structure.*;
import com.tinkerpop.gremlin.structure.util.ElementHelper;
import com.tinkerpop.gremlin.structure.util.StringFactory;

import java.util.Iterator;

public class HbaseVertex extends HbaseElement implements Vertex, Vertex.Iterators {

    public HbaseVertex(final Object id, final String label, Object[] keyValues, HbaseGraph graph) {
        super(id, label, graph, keyValues);
    }

    @Override
    public Property addPropertyLocal(String key, Object value) {
        checkRemoved();
        if (!shouldAddProperty(key)) return Property.empty();
        HbaseVertexProperty vertexProperty = new HbaseVertexProperty(this, key, value);
        properties.put(key, vertexProperty);
        return vertexProperty;
    }

    @Override
    public <V> VertexProperty<V> property(final String key, final V value, final Object... keyValues) {
        checkRemoved();
        return this.property(key, value);
    }

    @Override
    public <V> VertexProperty<V> property(String key, V value) {
        checkRemoved();
        ElementHelper.validateProperty(key, value);
        HbaseVertexProperty vertexProperty = (HbaseVertexProperty) addPropertyLocal(key, value);
        properties.put(key, vertexProperty);
        return vertexProperty;
    }

    @Override
    public <V> VertexProperty<V> property(final String key) {
        checkRemoved();
        if (this.properties.containsKey(key)) {
            return (VertexProperty<V>) this.properties.get(key);
        } else return VertexProperty.<V>empty();
    }

    @Override
    public Edge addEdge(final String label, final Vertex vertex, final Object... keyValues) {
        return null;
    }

    @Override
    public void remove() {
    }

    @Override
    public String toString() {
        return StringFactory.vertexString(this);
    }

    @Override
    public Vertex.Iterators iterators() {
        return this;
    }

    @Override
    public <V> Iterator<VertexProperty<V>> propertyIterator(final String... propertyKeys) {
        checkRemoved();
        return innerPropertyIterator(propertyKeys);
    }

    @Override
    public Iterator<Edge> edgeIterator(final Direction direction, final String... edgeLabels) {return null;}


    @Override
    public Iterator<Vertex> vertexIterator(final Direction direction, final String... edgeLabels) {
        return null;
    }

    @Override
    protected void checkRemoved() {
        if (this.removed) throw Element.Exceptions.elementAlreadyRemoved(Vertex.class, this.id);
    }
}
