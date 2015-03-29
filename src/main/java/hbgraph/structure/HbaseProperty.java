package hbgraph.structure;

import com.tinkerpop.gremlin.structure.Element;
import com.tinkerpop.gremlin.structure.Property;
import com.tinkerpop.gremlin.structure.util.ElementHelper;
import com.tinkerpop.gremlin.structure.util.StringFactory;

public class HbaseProperty<V> implements Property<V> {
    protected final HbaseElement element;
    protected final String key;
    protected V value;
    protected final HbaseGraph graph;

    public HbaseProperty(HbaseElement element, String key, V value) {
        this.element = element;
        this.key = key;
        this.value = value;
        this.graph = this.element.graph;
    }

    @Override
    public Element element() {
        return this.element;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public V value() {
        return this.value;
    }

    @Override
    public boolean isPresent() {
        return null != this.value;
    }

    public String toString() {
        return StringFactory.propertyString(this);
    }

    public boolean equals(final Object object) {
        return ElementHelper.areEqual(this, object);
    }

    public int hashCode() {
        return ElementHelper.hashCode(this);
    }

    @Override
    public void remove() {
        element.removeProperty(this);
    }
}
