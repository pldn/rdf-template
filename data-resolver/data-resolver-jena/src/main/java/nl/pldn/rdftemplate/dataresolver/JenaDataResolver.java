package nl.pldn.rdftemplate.dataresolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nl.pldn.rdftemplate.config.DataSource;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.stereotype.Component;

@Component
public class JenaDataResolver implements DataSourceResolver {
  @Override
  public String getId() {
    // TODO: make configurable via props
    return "sparql";
  }

  @Override
  public Object resolve(DataSource dataSource) {
    var dataSourceName = dataSource.getName();

    var dataSourceSource = dataSource.getSource();
    Model model = RDFDataMgr.loadModel(dataSourceSource);

    var dataSourceLocation = dataSource.getLocation();
    Query query = QueryFactory.read(dataSourceLocation);

    List<Map> list = new ArrayList<Map>();
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      ResultSet results = qexec.execSelect();
      for (; results.hasNext();) {
        QuerySolution soln = results.nextSolution();
        Iterator<String> varNames = soln.varNames();
        Map<String, String> map = new HashMap<String, String>();
        while (varNames.hasNext()) {
          String name = varNames.next();
          map.put(name, soln.get(name)
              .toString());
        }
        list.add(map);
        // Process
      }
    }
    return list;

    // TODO: replace this example with actual implementation
    /*
     * if (dataSourceName.equals("concepts") || dataSourceName.equals("members")) { return
     * List.of(Map.of("prefLabel", "Foo", "definition", "this is a foo", "notation", "foo"),
     * Map.of("prefLabel", "Bar", "definition", "this is a bar", "notation", "bar")); } else if
     * (dataSourceName.equals("collection")) { return Map.of("label", "FooBar"); }
     *
     * return Map.of();
     */
  }
}
