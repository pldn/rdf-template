package nl.pldn.rdftemplate;

import static nl.pldn.rdftemplate.config.ConfigProperties.CONFIG_FILE_NAME;

import java.io.IOException;
import nl.pldn.rdftemplate.config.ConfigResourceLoaders;
import nl.pldn.rdftemplate.config.RdfTemplateConfig;
import nl.pldn.rdftemplate.config.YamlConfigReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RdfTemplateConfiguration {

  @Bean
  public RdfTemplateConfig rdfTemplateConfig() {
    return ConfigResourceLoaders.getResource(CONFIG_FILE_NAME)
        .map(config -> {
          try {
            return YamlConfigReader.parseYamlConfig(config.getInputStream(), RdfTemplateConfig.class);
          } catch (IOException ioException) {
            throw new RdfTemplateException(String.format("Exception while parsing %s", CONFIG_FILE_NAME), ioException);
          }
        })
        .orElseThrow(() -> new RdfTemplateException(String.format("Could not find %s", CONFIG_FILE_NAME)))
        .orElseThrow(() -> new RdfTemplateException(String.format("Could not parse %s", CONFIG_FILE_NAME)));
  }
}
