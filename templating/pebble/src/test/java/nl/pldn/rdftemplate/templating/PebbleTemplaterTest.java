package nl.pldn.rdftemplate.templating;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import nl.pldn.rdftemplate.config.ConfigResourceLoaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PebbleTemplaterTest {

  private PebbleTemplater pebbleTemplater;

  @BeforeEach
  void setup() {
    var pebbleEngine = new PebbleConfiguration(List.of()).pebbleEngine();
    pebbleTemplater = new PebbleTemplater(pebbleEngine);
  }

  @Test
  void evaluateAndWriteTest() {
    var templateFile = getTemplateFile();
    var writer = new StringWriter();
    Map<String, Object> dataContext = Map.of("foo", "Foo", "bar", "Bar");

    pebbleTemplater.evaluateAndWrite(templateFile, writer, dataContext);

    assertThat(writer.toString(), is("Foo Bar"));
  }

  private File getTemplateFile() {
    return ConfigResourceLoaders.getResource("template.md")
        .map(resource -> {
          try {
            return resource.getFile();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .orElse(null);
  }

}
