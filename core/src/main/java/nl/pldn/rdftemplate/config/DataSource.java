package nl.pldn.rdftemplate.config;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataSource {

  static final String DATA_SOURCE_RESOLVER_KEY = "resolver";

  static final String DATA_SOURCE_LOCATION_KEY = "location";

  static final String DATA_SOURCE_SOURCE_KEY = "source";

  static final String DATA_SOURCE_SQUASH_BY_KEY = "squashByKey";

  private String name;

  private String resolver;

  private String location;

  private String source;

  private Set<String> squashByKey;
}
