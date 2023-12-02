package nl.pldn.rdftemplate.config;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.pldn.rdftemplate.dataresolver.DataResolverException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Slf4j
public class ConfigResourceLoaders {

  @SneakyThrows
  public static Optional<Resource> getResource(@NonNull String resourceLocation) {

    var relativeUri = resolve(ConfigProperties.getRelativeFileConfigPath(), resourceLocation);
    if (uriExists(relativeUri)) {
      return Optional.of(new UrlResource(relativeUri));
    }

    var uri = resolve(ConfigProperties.getFileConfigPath(), resourceLocation);
    if (uriExists(uri)) {
      return Optional.of(new UrlResource(uri));
    }

    var resource = new ClassPathResource(ConfigProperties.getConfigPath() + resourceLocation);
    if (resource.exists()) {
      return Optional.of(resource);
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

  public static String getResourceUriString(Resource resource) {
    try {
      return resource.getURI()
          .toString();
    } catch (IOException ioException) {
      throw new DataResolverException(String.format("Exception getting URI for resource %s", resource), ioException);
    }
  }
}
