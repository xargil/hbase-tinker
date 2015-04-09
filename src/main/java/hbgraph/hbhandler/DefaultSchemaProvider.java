package hbgraph.hbhandler;

import com.tinkerpop.gremlin.structure.Element;
import hbgraph.structure.HbaseElement;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


public class DefaultSchemaProvider implements SchemaProvider {
    public static String TYPE = "ty";
    String indexName;
    private HBaseAdmin client;

    @Override
    public void init(HBaseAdmin client, Configuration configuration) throws IOException {
        this.client = client;
        indexName = configuration.getString("elasticsearch.index.name", "graph");
        createIndex();
    }

    private void createIndex() throws IOException {

    }

    public void clearAllData() throws IOException {
        client.disableTable(indexName);
        //.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet()
        client.deleteTable(indexName);
        client.enableTable(indexName);
    }

    @Override
    public void close() {

    }

    @Override
    public String toString() {
        return "DefaultSchemaProvider{" +
                "indexName='" + indexName + '\'' +
                '}';
    }

    @Override
    public AddElementResult addElement(String label, Object idValue, HbaseElement.Type type, Object[] keyValues) {
        Object[] all = ArrayUtils.addAll(keyValues, TYPE, type.toString());
        return new AddElementResult() {
            @Override
            public String getIndex() {
                return indexName;
            }

            @Override
            public Object[] getKeyValues() {
                return all;
            }

            @Override
            public String getId() {
                return idValue.toString();
            }
        };
    }

    @Override
    public String getIndex(Element element) {
        return indexName;
    }

    @Override
    public String getIndex(Object id) {
        return indexName;
    }


    @Override
    public SearchResult search(HbaseElement.Type type, String[] labels) {
        return new SearchResult() {
            @Override
            public String[] getIndices() {
                return new String[]{indexName};
            }

            @Override
            public Filter getFilter() {
                //TermFilterBuilder typeFilter = FilterBuilders.termFilter(TYPE, type.toString());
                Filter f = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("gross")));

                return f;
            }
        };

    }


}
