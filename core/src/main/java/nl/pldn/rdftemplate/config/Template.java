package nl.pldn.rdftemplate.config;

import java.nio.file.Path;
import lombok.Data;

@Data
public class Template {

  public static final String TEMPLATE_DIR = "templates";

  private String template;

  private Path outputLocation;

}
