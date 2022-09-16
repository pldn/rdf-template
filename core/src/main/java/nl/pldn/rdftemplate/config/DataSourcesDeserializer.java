package nl.pldn.rdftemplate.config;

import static nl.pldn.rdftemplate.config.DataSource.DATA_SOURCE_LOCATION_KEY;
import static nl.pldn.rdftemplate.config.DataSource.DATA_SOURCE_RESOLVER_KEY;
import static nl.pldn.rdftemplate.config.DataSource.DATA_SOURCE_SOURCE_KEY;
import static nl.pldn.rdftemplate.config.DataSource.DATA_SOURCE_SQUASH_BY_KEY;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import nl.pldn.rdftemplate.dataresolver.DataResolverException;

public class DataSourcesDeserializer extends StdDeserializer<DataSources> {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
    var squashByKeyNode = node.get(DATA_SOURCE_SQUASH_BY_KEY);
    Set<String> squashByKey = Set.of();
    if (squashByKeyNode != null) {
      try {
        squashByKey = OBJECT_MAPPER.readerFor(new TypeReference<Set<String>>() {})
            .readValue(squashByKeyNode);
      } catch (IOException e) {
        throw new DataResolverException(String.format("Could not resolve value for %s", DATA_SOURCE_SQUASH_BY_KEY));
      }
    }

    return new DataSource(name, resolver, location, source, squashByKey);
  }

}
