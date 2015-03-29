package hbgraph.process.graph.traversal.strategy;

import com.tinkerpop.gremlin.process.Step;
import com.tinkerpop.gremlin.process.Traversal;
import com.tinkerpop.gremlin.process.TraversalEngine;
import com.tinkerpop.gremlin.process.graph.marker.HasContainerHolder;
import com.tinkerpop.gremlin.process.graph.step.map.EdgeVertexStep;
import com.tinkerpop.gremlin.process.graph.step.map.VertexStep;
import com.tinkerpop.gremlin.process.graph.step.sideEffect.GraphStep;
import com.tinkerpop.gremlin.process.graph.step.sideEffect.IdentityStep;
import com.tinkerpop.gremlin.process.graph.strategy.AbstractTraversalStrategy;
import com.tinkerpop.gremlin.process.util.EmptyStep;
import com.tinkerpop.gremlin.process.util.TraversalHelper;
import hbgraph.structure.HbaseGraph;

import java.util.ArrayList;
import java.util.List;

public class HbaseGraphStepStrategy extends AbstractTraversalStrategy {
    private static final HbaseGraphStepStrategy INSTANCE = new HbaseGraphStepStrategy();

    public static HbaseGraphStepStrategy instance() {
        return INSTANCE;
    }
    private static ThreadLocal<HbaseGraph> graph = new ThreadLocal<>();

    @Override
    public void apply(final Traversal.Admin<?, ?> traversal, final TraversalEngine engine) {
        if (engine.equals(TraversalEngine.COMPUTER)) return;

        Step<?, ?> startStep = TraversalHelper.getStart(traversal);
        if(startStep instanceof GraphStep || graph != null) {
            if(startStep instanceof GraphStep ) graph.set((HbaseGraph) ((GraphStep) startStep).getGraph(HbaseGraph.class));
            processStep(startStep, traversal);
        }

    }

    private void processStep(Step<?, ?> currentStep, Traversal.Admin<?, ?> traversal) {
        List<String> typeLabels = new ArrayList<>();
        List<Object> onlyIdsAllowed = new ArrayList<>();
        Step<?, ?> nextStep = currentStep.getNextStep();

        while(stepCanConsumePredicates(currentStep) && nextStep instanceof HasContainerHolder) {
            final boolean[] containesLambada = {false};
            ((HasContainerHolder) nextStep).getHasContainers().forEach((has)->{
                if(has.predicate.toString().equals("eq") && has.key.equals("~label"))
                    typeLabels.add(has.value.toString());
                else if(has.predicate.toString().equals("eq") && has.key.equals("~id"))
                    onlyIdsAllowed.add(has.value);
                else if(has.predicate.toString().contains("$$")){
                    containesLambada[0] = true;
                }
            });
            if (nextStep.getLabel().isPresent()) {
                final IdentityStep identityStep = new IdentityStep<>(traversal);
                identityStep.setLabel(nextStep.getLabel().get());
                TraversalHelper.insertAfterStep(identityStep, currentStep, traversal);
            }
            if(!containesLambada[0]) traversal.removeStep(nextStep);
            nextStep  = nextStep.getNextStep();
        }
        String[] typeLabelsArray = typeLabels.toArray(new String[0]);
        Object[] onlyIdsAllowedArray = onlyIdsAllowed.toArray(new Object[onlyIdsAllowed.size()]);

        if(!(currentStep instanceof EmptyStep)) processStep(nextStep, traversal);
    }

    private boolean stepCanConsumePredicates(Step<?, ?> step) {
        return (step instanceof VertexStep)|| (step instanceof EdgeVertexStep ) || (step instanceof GraphStep);
    }

}
