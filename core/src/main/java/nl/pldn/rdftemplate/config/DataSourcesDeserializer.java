package nl.pldn.rdftemplate.config;

import static nl.pldn.rdftemplate.config.DataSource.DATA_SOURCE_LOCATION_KEY;
import static nl.pldn.rdftemplate.config.DataSource.DATA_SOURCE_RESOLVER_KEY;
import static nl.pldn.rdftemplate.config.DataSource.DATA_SOURCE_SOURCE_KEY;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DataSourcesDeserializer extends StdDeserializer<DataSources> {

  public DataSourcesDeserializer() {
    this(null);
  }

  protected DataSourcesDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public DataSources deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JacksonException {

    JsonNode node = jsonParser.getCodec()
        .readTree(jsonParser);

    var dataSources =
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.fields(), Spliterator.ORDERED), false)
            .map(this::toDataSource)
            .collect(Collectors.toUnmodifiableSet());

    return new DataSources(dataSources);
  }

  private DataSource toDataSource(Map.Entry<String, JsonNode> dataSourceEntry) {
    var name = dataSourceEntry.getKey();

    var node = dataSourceEntry.getValue();
    var resolver = node.get(DATA_SOURCE_RESOLVER_KEY)
        .asText();
    var location = node.get(DATA_SOURCE_LOCATION_KEY)
        .asText();
    var source = node.get(DATA_SOURCE_SOURCE_KEY)
        .asText();

    return new DataSource(name, resolver, location, source);
  }

}
