package nl.pldn.rdftemplate.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataSource {

  static final String DATA_SOURCE_RESOLVER_KEY = "resolver";

  static final String DATA_SOURCE_LOCATION_KEY = "location";

  private String name;

  private String resolver;

  private String location;
}
