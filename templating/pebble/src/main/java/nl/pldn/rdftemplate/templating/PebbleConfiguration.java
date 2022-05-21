package nl.pldn.rdftemplate.templating;


import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PebbleConfiguration {

  private final List<Extension> extensions;

  public PebbleConfiguration(List<Extension> extensions) {
    this.extensions = extensions;
  }

  private Loader<?> getTemplateLoader() {
    return new FileLoader();
  }

  @Bean
  public PebbleEngine pebbleEngine() {
    return new PebbleEngine.Builder().extension(extensions.toArray(new Extension[0]))
        .loader(getTemplateLoader())
        .build();
  }
}
