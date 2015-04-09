package hbgraph.hbhandler;

import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Element;
import com.tinkerpop.gremlin.structure.Vertex;
import hbgraph.hbhandler.TimingAccessor.Timer;
import hbgraph.structure.HbaseEdge;
import hbgraph.structure.HbaseElement;
import hbgraph.structure.HbaseGraph;
import hbgraph.structure.HbaseVertex;
import org.apache.commons.configuration.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class HBHandler {

    //region members

    public static class ClientType {
    }

    private HbaseGraph graph;
    public SchemaProvider schemaProvider;
    private boolean refresh;
    TimingAccessor timingAccessor = new TimingAccessor();
    //public HBaseAdmin client;
    public HTable client;
    //endregion

    //region initialization

    public HBHandler(HbaseGraph graph, Configuration configuration) throws IOException {
        this.graph = graph;
    }

    public void close() throws IOException {
        client.close();
        schemaProvider.close();
        timingAccessor.print();
    }

    //endregion

    //region queries

    public String addElement(String label, Object idValue, HbaseElement.Type type, Object... keyValues) {
        return null;
    }

    public void deleteElement(Element element) {
    }

    public void deleteElements(Iterator<Element> elements) {
        elements.forEachRemaining(this::deleteElement);
    }

    public <V> void addProperty(Element element, String key, V value) {

    }

    public void removeProperty(Element element, String key) {

    }

    public Iterator<Vertex> getVertices(String type,Object... ids) throws IOException {
        if (ids == null || ids.length == 0) return Collections.emptyIterator();

        //MultiGetResponse responses = get(type,ids);
        List<Get> gets = new ArrayList<>(ids.length);

        Result[] results = client.get(gets);
        ArrayList<Vertex> vertices = new ArrayList<>(ids.length);
        for (Result res : results)
        {

            // todo: get id from row
            // get label from row
            vertices.add(createVertex("asd", "v", null));
        }

        return vertices.iterator();
    }

    public Iterator<Edge> getEdges(String type, Object... ids) throws IOException {
        if (ids == null || ids.length == 0) return Collections.emptyIterator();

        //MultiGetResponse responses = get(type,ids);
        List<Get> gets = new ArrayList<>(ids.length);

        Result[] results = client.get(gets);
        ArrayList<Edge> edges = new ArrayList<>(ids.length);
        for (Result res : results)
        {
            // todo: get id from row
            // get label from row
            edges.add(createEdge("asd", "e", null));
        }

        return edges.iterator();
    }

    public Iterator<Vertex> searchVertices(Object[] ids, String[] labels) throws IOException {
        if(idsOnlyQuery(ids, labels)) return getVertices(getFirstOrDefaultLabel(labels),ids);

        Stream<Result> hits = search(ids, HbaseElement.Type.vertex, labels);
        return hits.map((hit) -> createVertex("asd", "v", null)).iterator();
    }

    private String getFirstOrDefaultLabel(String[] labels) {
        if(labels == null || labels.length == 0) return null;
        return labels[0];
    }

    private boolean idsOnlyQuery(Object[] ids, String[] labels) {
        return (labels == null || labels.length <= 1) && ids != null && ids.length > 0;
    }

    public Iterator<Edge> searchEdges(Object[] ids, String[] labels) throws IOException {
        if(idsOnlyQuery(ids, labels)) return getEdges(getFirstOrDefaultLabel(labels), ids);

        Stream<Result> hits = search(ids, HbaseElement.Type.edge, labels);
        return hits.map((hit) -> createEdge("asd", "e", null)).iterator();
    }

    private Stream<Result> search(Object[] ids_arr, HbaseElement.Type type, String... labels) throws IOException {
        timer("search").start();
        List<Object> ids = Arrays.asList(ids_arr);
        List<Get> gets = new ArrayList<>(ids.size());
        gets.addAll(ids.stream().map(id -> new Get(Bytes.toBytes((String) id))).collect(Collectors.toList()));

        List<Result> resAsList = Arrays.asList(client.get(gets));
        Iterable<Result> hitsIterable = () -> resAsList.iterator();

        Stream<Result> hitStream = StreamSupport.stream(hitsIterable.spliterator(), false);
        timer("search").stop();
        return hitStream;
    }


    private Vertex createVertex(Object id, String label, Map<String, Object> fields) {
        HbaseVertex vertex = new HbaseVertex(id,label, null, this.graph);

        fields.entrySet().forEach((field) -> vertex.addPropertyLocal(field.getKey(), field.getValue()));
        return vertex;
    }

    private Edge createEdge(Object id, String label, Map<String, Object> fields) {
        HbaseEdge edge = new HbaseEdge(id, label, fields.get(HbaseEdge.OutId), fields.get(HbaseEdge.InId), null, graph);
        fields.entrySet().forEach((field) -> edge.addPropertyLocal(field.getKey(), field.getValue()));
        return edge;
    }

    //endregion

    //region meta

    private Timer timer(String name) {
        return timingAccessor.timer(name);
    }

    public void collectData() {
        timingAccessor.print();
    }

    @Override
    public String toString() {
        return "HBHandler{" +
                "schema='" + schemaProvider + '\'' +
                ", client=" + client +
                '}';
    }
    //endregion
}
