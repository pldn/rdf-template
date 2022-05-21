package nl.pldn.rdftemplate.config;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class ConfigResourceLoaders {

  @SneakyThrows
  public static Optional<Resource> getResource(@NonNull String resourceLocation) {
    var uri = resolve(ConfigProperties.getFileConfigPath(), resourceLocation);
    if (uriExists(uri)) {
      return Optional.of(new UrlResource(uri));
    } else {
      var resource = new ClassPathResource(ConfigProperties.getConfigPath() + resourceLocation);
      if (resource.exists()) {
        return Optional.of(resource);
      }
    }
    return Optional.empty();
  }

  private static URI resolve(@NonNull URI basePath, String resourceLocation) {
    if (resourceLocation == null) {
      return basePath;
    }
    return basePath.resolve(resourceLocation);
  }

  static boolean uriExists(URI uri) {
    return Files.exists(Paths.get(uri));
  }
}
