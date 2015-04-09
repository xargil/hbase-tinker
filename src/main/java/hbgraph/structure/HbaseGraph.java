package hbgraph.structure;

import com.tinkerpop.gremlin.process.TraversalStrategies;
import com.tinkerpop.gremlin.process.computer.GraphComputer;
import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Graph;
import com.tinkerpop.gremlin.structure.Transaction;
import com.tinkerpop.gremlin.structure.Vertex;
import hbgraph.hbhandler.HBHandler;
import org.apache.commons.configuration.Configuration;

import java.io.IOException;
import java.util.Iterator;

@Graph.OptIn(Graph.OptIn.SUITE_STRUCTURE_STANDARD)
@Graph.OptIn(Graph.OptIn.SUITE_STRUCTURE_PERFORMANCE)
@Graph.OptIn(Graph.OptIn.SUITE_PROCESS_STANDARD)
public class HbaseGraph implements Graph, Graph.Iterators {
    static {
        try {
            TraversalStrategies.GlobalCache.registerStrategies(HbaseGraph.class, TraversalStrategies.GlobalCache.getStrategies(Graph.class).clone().addStrategies(null));
        } catch (final CloneNotSupportedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    //for testSuite
    public static HbaseGraph open(final Configuration configuration) throws IOException {
        return new HbaseGraph(configuration);
    }

    public final HBHandler hbHandler;

    public HbaseGraph(Configuration configuration) throws IOException {
        hbHandler = new HBHandler(this, configuration);
    }

    @Override
    public Configuration configuration() {
        return null;
    }

    @Override
    public Iterators iterators() {
        return this;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public Features features() {
        return new HbaseFeatures();
    }

    @Override
    public Transaction tx() {
        throw Exceptions.transactionsNotSupported();
    }

    @Override
    public Variables variables() {
        throw Exceptions.variablesNotSupported();
    }

    @Override
    public GraphComputer compute(final Class... graphComputerClass) {
        throw Exceptions.graphComputerNotSupported();
    }

    @Override
    public Iterator<Vertex> vertexIterator(final Object... vertexIds) {
        if (vertexIds == null || vertexIds.length == 0) {
            return null;
        }
        return hbHandler.getVertices(null,vertexIds);
    }

    @Override
    public Iterator<Edge> edgeIterator(final Object... edgeIds) {
        return null;
    }

    @Override
    public Vertex addVertex(final Object... keyValues) {
        throw Exceptions.vertexAdditionsNotSupported();
    }

}
