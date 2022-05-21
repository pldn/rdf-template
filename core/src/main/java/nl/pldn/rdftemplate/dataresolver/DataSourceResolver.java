package nl.pldn.rdftemplate.dataresolver;

import nl.pldn.rdftemplate.config.DataSource;

public interface DataSourceResolver {

  String getId();

  Object resolve(DataSource dataSource);
}
