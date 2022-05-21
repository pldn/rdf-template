package nl.pldn.rdftemplate.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Slf4j
public final class YamlConfigReader {
  private static final Pattern p = Pattern.compile("\\$\\{((?>[A-Z]|_)*)}");

  @Autowired
  static Environment env;

  private YamlConfigReader() {}

  public static <T> Optional<T> parseYamlConfig(Path filePath, Class<T> clazz) {
    if (Files.exists(filePath)) {
      try (InputStream yamlFile = Files.newInputStream(filePath)) {
        return parseYamlConfig(yamlFile, clazz);
      } catch (IOException ex) {
        throw new YamlConfigReaderException(String.format("Couldn't parse sources file from path '%s'", filePath), ex);
      }
    }
    return Optional.empty();
  }

  public static <T> Optional<T> parseYamlConfig(InputStream inputStream, Class<T> clazz) {
    if (inputStream == null) {
      return Optional.empty();
    }
    String content;
    try {
      content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new YamlConfigReaderException(String.format("Could not read %s yaml config", clazz.getName()), e);
    }
    if (content.length() == 0) {
      return Optional.empty();
    }
    content = replaceEnvironmentVariables(content);
    try {
      return Optional.of(createObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
          .readValue(content, clazz));
    } catch (JsonProcessingException ex) {
      throw new YamlConfigReaderException(String.format("Couldn't parse sources file to '%s' object", clazz.getName()),
          ex);
    }
  }

  private static String replaceEnvironmentVariables(String content) {
    Matcher matcher = p.matcher(content);
    while (matcher.find()) {
      String key = matcher.group(1);
      String value = env.getProperty(key);
      content = content.replace(matcher.group(), value != null ? value : matcher.group());
    }
    return content;
  }

  private static ObjectMapper createObjectMapper() {
    return new ObjectMapper(new YAMLFactory());
  }
}
