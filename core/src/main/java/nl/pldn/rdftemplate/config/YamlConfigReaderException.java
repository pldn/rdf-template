package nl.pldn.rdftemplate.config;

public class YamlConfigReaderException extends RuntimeException {

  public YamlConfigReaderException(String message) {
    super(message);
  }

  public YamlConfigReaderException(String message, Throwable cause) {
    super(message, cause);
  }
}
