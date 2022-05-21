package nl.pldn.rdftemplate.templating;


import com.mitchellbosecke.pebble.PebbleEngine;
import java.io.File;
import java.io.Writer;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class PebbleTemplater implements Templater {

  private final PebbleEngine pebbleEngine;

  public PebbleTemplater(PebbleEngine pebbleEngine) {
    this.pebbleEngine = pebbleEngine;
  }

  @SneakyThrows
  @Override
  public void evaluateAndWrite(File templateFile, Writer writer, Map<String, Object> dataContext) {
    var pebbleTemplate = pebbleEngine.getTemplate(templateFile.getAbsolutePath());
    pebbleTemplate.evaluate(writer, dataContext);
  }
}
