package hbgraph.process.graph.traversal.sideEffect;

import com.tinkerpop.gremlin.process.Traversal;
import com.tinkerpop.gremlin.process.Traverser;
import com.tinkerpop.gremlin.process.graph.marker.Reversible;
import com.tinkerpop.gremlin.process.util.AbstractStep;
import com.tinkerpop.gremlin.structure.Element;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class HbaseFlatMapStep<S extends  Element, E extends Element > extends AbstractStep<S,E> implements Reversible {
    public HbaseFlatMapStep(Traversal traversal, Optional<String> label) {
        super(traversal);
    }

    @Override
    protected Traverser<E> processNextStart() {
        return null;
    }

    @Override
    public void reset() {
        super.reset();
    }

    public class HbaseTraverser implements Iterator<Traverser<E>> {

        private HbaseTraverser(Traverser.Admin<S> traverer, AbstractStep<S, E> step) {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Traverser<E> next() { return null; }

        public S getElement() { return null; }
        public List<E> getResults() { return null;}
        public void addResult(E result) {  }
        public void clearResults() { }
    }
}
