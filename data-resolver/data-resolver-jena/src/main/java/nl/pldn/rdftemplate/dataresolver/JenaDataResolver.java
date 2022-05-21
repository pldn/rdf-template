package nl.pldn.rdftemplate.dataresolver;

import java.util.List;
import java.util.Map;
import nl.pldn.rdftemplate.config.DataSource;
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

    // TODO: replace this example with actual implementation
    if (dataSourceName.equals("concepts") || dataSourceName.equals("members")) {
      return List.of(Map.of("prefLabel", "Foo", "definition", "this is a foo", "notation", "foo"),
          Map.of("prefLabel", "Bar", "definition", "this is a bar", "notation", "bar"));
    } else if (dataSourceName.equals("collection")) {
      return Map.of("label", "FooBar");
    }

    return Map.of();
  }
}
