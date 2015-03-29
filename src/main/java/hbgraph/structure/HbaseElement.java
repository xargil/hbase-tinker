package hbgraph.structure;

import com.tinkerpop.gremlin.structure.Element;
import com.tinkerpop.gremlin.structure.Graph;
import com.tinkerpop.gremlin.structure.Property;
import com.tinkerpop.gremlin.structure.util.ElementHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public abstract class HbaseElement implements Element, Element.Iterators {
    protected HashMap<String, Property> properties = new HashMap();
    protected final Object id;
    protected final String label;
    protected final HbaseGraph graph;
    protected boolean removed = false;

    public HbaseElement(final Object id, final String label, HbaseGraph graph, Object[] keyValues) {
        this.graph = graph;
        this.id = id;
        this.label = label;
        if (keyValues != null) addPropertiesLocal(keyValues);
    }

    @Override
    public Object id() {
        return this.id;
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public Graph graph() {
        return this.graph;
    }

    @Override
    public Set<String> keys() {
        return this.properties.keySet();
    }

    @Override
    public <V> Property<V> property(final String key) {
        checkRemoved();
        return this.properties.containsKey(key) ? this.properties.get(key) : Property.<V>empty();
    }

    @Override
    public int hashCode() {
        return ElementHelper.hashCode(this);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object object) {
        return ElementHelper.areEqual(this, object);
    }

    protected Iterator innerPropertyIterator(String[] propertyKeys) {
        HashMap<String, Property> properties = (HashMap<String, Property>) this.properties.clone();

        if (propertyKeys.length > 0)
            return properties.entrySet().stream().filter(entry -> ElementHelper.keyExists(entry.getKey(), propertyKeys)).map(x -> x.getValue()).iterator();

        return (Iterator) properties.values().iterator();
    }


    public void removeProperty(Property property) {
        properties.remove(property.key());
    }

    private void addPropertiesLocal(Object[] keyValues) {
        for (int i = 0; i < keyValues.length; i = i + 2) {
            String key = keyValues[i].toString();
            Object value = keyValues[i + 1];
            this.addPropertyLocal(key, value);
        }
    }

    public abstract Property addPropertyLocal(String key, Object value);

    protected boolean shouldAddProperty(String key) {
        return false;
    }

    protected abstract void checkRemoved();

    public enum Type {
        vertex,
        edge
    }
}
