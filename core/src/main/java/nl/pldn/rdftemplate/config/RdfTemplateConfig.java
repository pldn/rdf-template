package nl.pldn.rdftemplate.config;

import java.util.List;
import lombok.Data;

@Data
public class RdfTemplateConfig {

  private DataSources dataSources;

  private List<Template> templates;
}
