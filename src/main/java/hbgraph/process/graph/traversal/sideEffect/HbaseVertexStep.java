package hbgraph.process.graph.traversal.sideEffect;

import com.tinkerpop.gremlin.process.graph.step.map.VertexStep;
import com.tinkerpop.gremlin.structure.Element;
import com.tinkerpop.gremlin.structure.Vertex;

import java.util.List;

public class HbaseVertexStep<E extends Element> extends HbaseFlatMapStep<Vertex,E> {

    private final Class returnClass;
    private final String[] typeLabels;
    private String[] edgeLabels;
    private Object[] onlyAllowedIds;
    public HbaseVertexStep(VertexStep originalStep, String[] typeLabels, Object[] onlyAllowedIds) {
        super(originalStep.getTraversal(), originalStep.getLabel());
        this.typeLabels = typeLabels;
        this.edgeLabels = originalStep.getEdgeLabels();
        this.onlyAllowedIds = onlyAllowedIds;
        returnClass = originalStep.getReturnClass();
    }


    private void loadEdges(List<HbaseTraverser> traversers) {
    }

    private void loadVertices(List<HbaseTraverser> traversers) {
    }

    @Override
    public String toString() {
        return "";
    }
}
