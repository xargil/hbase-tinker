package hbgraph.hbhandler;

import com.tinkerpop.gremlin.structure.Element;
import hbgraph.structure.HbaseElement;
import org.apache.commons.configuration.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.filter.Filter;

import java.io.IOException;


public interface SchemaProvider {
    void init(HBaseAdmin client, Configuration configuration) throws IOException;
    void close();

    AddElementResult addElement(String label, Object idValue, HbaseElement.Type type, Object[] keyValues);
    String getIndex(Element element);
    String getIndex(Object id);
    SearchResult search(/*FilterBuilder? */HbaseElement.Type type, String[] labels);

    public interface AddElementResult{
        public String getIndex() ;
        public Object[] getKeyValues();
        String getId();
    }

    public interface SearchResult{
        public String[] getIndices();
        public Filter getFilter();
    }
}
