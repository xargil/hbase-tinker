package hbgraph.process.graph.traversal.sideEffect;

import com.tinkerpop.gremlin.process.TraverserGenerator;
import com.tinkerpop.gremlin.process.graph.step.sideEffect.GraphStep;
import com.tinkerpop.gremlin.process.traverser.B_O_P_PA_S_SE_SL_TraverserGenerator;
import com.tinkerpop.gremlin.process.util.TraversalMetrics;
import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Element;
import com.tinkerpop.gremlin.structure.Vertex;
import hbgraph.structure.HbaseGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class HbaseGraphStep<E extends Element> extends GraphStep<E> {
    public HbaseGraphStep(GraphStep originalStep, String[] typeLabels, Object[] onlyAllowedIds) {
        super(originalStep.getTraversal(), originalStep.getGraph(HbaseGraph.class), originalStep.getReturnClass(), originalStep.getIds());
    }


    private Iterator<? extends Vertex> vertices() {
         return null;
    }

    private Iterator<? extends Edge> edges() {
         return null;
    }

    @Override
    public Object[] getIds(){
        return null;
    }

    @Override
    public void generateTraversers(final TraverserGenerator traverserGenerator) {
        if (PROFILING_ENABLED) TraversalMetrics.start(this);
        try {
            this.start = this.iteratorSupplier.get();
            if (null != this.start) {

                if (this.start instanceof Iterator) {
                    List<E> newListForIterator = new ArrayList<>();
                    Iterator<E> iter = (Iterator<E>) this.start;
                    while(iter.hasNext()){
                        E next = iter.next();

                        //B_O_PA_S_SE_SL_NC_Traverser<E> eb_o_pa_s_se_sl_nc_traverser = new B_O_PA_S_SE_SL_NC_Traverser<>(next, this);
                        this.starts.add(B_O_P_PA_S_SE_SL_TraverserGenerator.instance().generate(next,this,1l));
                        newListForIterator.add(next);
                    }
                    this.start = newListForIterator.iterator();
                    //this.starts.add(traverserGenerator.generateIterator((Iterator<E>) this.start, this, 1l));
                } else {
                    this.starts.add(traverserGenerator.generate((E) this.start, this, 1l));
                }
            }
        }catch (NoSuchElementException ex){
            throw ex;
        }
        catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            if (PROFILING_ENABLED) TraversalMetrics.stop(this);
        }
    }
}
