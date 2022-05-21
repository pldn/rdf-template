package nl.pldn.rdftemplate.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonDeserialize(using = DataSourcesDeserializer.class)
@Data
@AllArgsConstructor
public class DataSources {
  Set<DataSource> dataSources;
}
