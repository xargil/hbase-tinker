package hbgraph.process.graph.traversal.traversalHolder;

import com.tinkerpop.gremlin.process.Traversal;
import com.tinkerpop.gremlin.process.TraversalStrategies;
import com.tinkerpop.gremlin.process.Traverser;
import com.tinkerpop.gremlin.process.graph.marker.TraversalHolder;
import com.tinkerpop.gremlin.process.graph.step.branch.UnionStep;
import com.tinkerpop.gremlin.process.graph.step.util.ComputerAwareStep;
import com.tinkerpop.gremlin.process.traverser.TraverserRequirement;
import com.tinkerpop.gremlin.process.util.TraversalHelper;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


public class HbaseUnionStep<S,E> extends ComputerAwareStep<S, E> implements TraversalHolder<S, E> {
    public HbaseUnionStep(Traversal traversal, UnionStep originalStep) {
        super(traversal);
    }

    @Override
    protected Iterator<Traverser<E>> standardAlgorithm() throws NoSuchElementException {
        while (true) {
            for (final Traversal<S, E> union : this.getTraversals()) {
                if (union.hasNext()) return TraversalHelper.getEnd(union.asAdmin());
            }
            do {
                Traverser.Admin<S> next = this.starts.next();
                this.getTraversals().forEach(union -> union.asAdmin().addStart(next.split()));
            }
            while (this.starts.hasNext());

        }
    }

    @Override
    protected Iterator<Traverser<E>> computerAlgorithm() throws NoSuchElementException {
        return null;
    }

    @Override
    public List<Traversal<S, E>> getTraversals() {
        return null;
    }

    @Override
    public TraversalStrategies getChildStrategies() {
        return null;
    }


    @Override
    public String toString() {
        return null;
    }

    @Override
    public HbaseUnionStep<S, E> clone() throws CloneNotSupportedException {
        final HbaseUnionStep<S, E> clone = (HbaseUnionStep<S, E>) super.clone();
        return clone;
    }

    @Override
    public void reset() {

    }

    @Override
    public Set<TraverserRequirement> getRequirements() {
        return null;
    }

}
