package nl.pldn.rdftemplate.config;

import java.net.URI;

public class ConfigProperties {

  private ConfigProperties() {}

  private static final String CLASSPATH_PREFIX = "classpath:/";

  private static final String FILE_PREFIX = "file:/";

  public static final String CONFIG_PATH = "config/";

  public static final String CONFIG_FILE_NAME = "rdftemplate.yml";

  public static String getConfigPath() {
    return CONFIG_PATH;
  }

  public static URI getFileConfigPath() {
    return URI.create(FILE_PREFIX + CONFIG_PATH);
  }

  public static URI getResourcePath() {
    return URI.create(CLASSPATH_PREFIX + CONFIG_PATH);
  }

}
