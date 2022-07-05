package nl.pldn.rdftemplate.dataresolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nl.pldn.rdftemplate.config.ConfigResourceLoaders;
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
    Model model = null;
    var dataSourceSource = dataSource.getSource();

    if (dataSource.getSource() != null) {
      model = ConfigResourceLoaders.getResource(dataSourceSource)
          .map(ConfigResourceLoaders::getResourceUriString)
          .map(RDFDataMgr::loadModel)
          .orElseThrow(() -> new DataResolverException(String.format("Could not find source %s", dataSourceSource)));
    }

    var query = ConfigResourceLoaders.getResource(dataSource.getLocation())
        .map(ConfigResourceLoaders::getResourceUriString)
        .map(QueryFactory::read)
        .orElseThrow(
            () -> new DataResolverException(String.format("Could not find query %s", dataSource.getLocation())));

    return executeQuery(model, query);
  }

  private List<Map<String, String>> executeQuery(Model model, Query query) {
    List<Map<String, String>> result = new ArrayList<>();
    try (QueryExecution queryExecution = QueryExecutionFactory.create(query, model)) {
      ResultSet resultSet = queryExecution.execSelect();

      while (resultSet.hasNext()) {
        QuerySolution querySolution = resultSet.nextSolution();
        Iterator<String> varNames = querySolution.varNames();
        Map<String, String> row = new HashMap<>();

        while (varNames.hasNext()) {
          String name = varNames.next();
          row.put(name, querySolution.get(name)
              .toString());
        }

        result.add(row);
      }
    }

    return result;
  }
}
