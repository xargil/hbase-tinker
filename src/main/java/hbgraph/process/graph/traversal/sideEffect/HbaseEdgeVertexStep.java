package hbgraph.process.graph.traversal.sideEffect;

import com.tinkerpop.gremlin.process.graph.step.map.EdgeVertexStep;
import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Vertex;

public class HbaseEdgeVertexStep extends HbaseFlatMapStep<Edge,Vertex> {

    private Object[] onlyAllowedIds;
    public HbaseEdgeVertexStep(EdgeVertexStep originalStep) {
        super(originalStep.getTraversal(), originalStep.getLabel());
    }


    public String toString() {return "";}
}
