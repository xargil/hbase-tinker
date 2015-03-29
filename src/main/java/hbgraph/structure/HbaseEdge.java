package hbgraph.structure;

import com.tinkerpop.gremlin.structure.*;
import com.tinkerpop.gremlin.structure.util.ElementHelper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HbaseEdge extends HbaseElement implements Edge, Edge.Iterators {

    public static String OutId = "o";
    public static String InId = "i";

    private String inId;
    private String outId;

    public HbaseEdge(final Object id, final String label, Object outId, Object inId, Object[] keyValues, final HbaseGraph graph) {
        super(id, label, graph, keyValues);
        this.inId = inId.toString();
        this.outId = outId.toString();
    }

    @Override
    public Property addPropertyLocal(String key, Object value) {
        if (!shouldAddProperty(key)) return Property.empty();
        HbaseProperty vertexProperty = new HbaseProperty(this, key, value);
        properties.put(key, vertexProperty);
        return vertexProperty;
    }

    @Override
    protected boolean shouldAddProperty(String key) {
        return super.shouldAddProperty(key) && key != OutId && key != InId;
    }

    @Override
    public <V> Property<V> property(String key, V value) {
        checkRemoved();
        ElementHelper.validateProperty(key, value);
        HbaseProperty vertexProperty = (HbaseProperty) addPropertyLocal(key, value);
        return vertexProperty;
    }

    @Override
    public void remove() {
        checkRemoved();
        this.removed = true;
    }

    @Override
    public String toString()
    {
        return "E[" + this.id() +"]["+this.outId+"_"+this.label +"->"+ this.inId+"]";
    }

    @Override
    public Edge.Iterators iterators() {
        checkRemoved();
        return this;
    }

    @Override
    public Iterator<Vertex> vertexIterator(final Direction direction) {
        checkRemoved();
        return null;
    }

    public List getVertexId(Direction direction) {
        switch (direction) {
            case OUT:
                return Arrays.asList(outId);
            case IN:
                return Arrays.asList(inId);
            default:
                return Arrays.asList(outId, inId);
        }
    }

    @Override
    public <V> Iterator<Property<V>> propertyIterator(final String... propertyKeys) {
        checkRemoved();
        return innerPropertyIterator(propertyKeys);
    }

    @Override
    protected void checkRemoved() {
        if (this.removed) throw Element.Exceptions.elementAlreadyRemoved(Edge.class, this.id);
    }
}
