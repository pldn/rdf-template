package nl.pldn.rdftemplate;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.pldn.rdftemplate.config.ConfigResourceLoaders;
import nl.pldn.rdftemplate.config.DataSource;
import nl.pldn.rdftemplate.config.RdfTemplateConfig;
import nl.pldn.rdftemplate.config.Template;
import nl.pldn.rdftemplate.dataresolver.DataSourceResolver;
import nl.pldn.rdftemplate.templating.Templater;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class RdfTemplateProcessor {
  private final RdfTemplateConfig rdfTemplateConfig;

  private final Map<String, DataSourceResolver> dataSourceResolverRegistry;

  private final Templater templater;

  public RdfTemplateProcessor(RdfTemplateConfig rdfTemplateConfig, Set<DataSourceResolver> dataSourceResolvers,
      Templater templater) {
    this.rdfTemplateConfig = rdfTemplateConfig;
    this.dataSourceResolverRegistry = getDataSourceResolverRegistry(dataSourceResolvers);
    this.templater = templater;
  }

  private Map<String, DataSourceResolver> getDataSourceResolverRegistry(Set<DataSourceResolver> dataSourceResolvers) {
    return dataSourceResolvers.stream()
        .collect(groupingBy(DataSourceResolver::getId, collectingAndThen(toList(), this::ensureSingleResolver)));
  }

  private DataSourceResolver ensureSingleResolver(List<DataSourceResolver> dataSourceResolvers) {
    if (dataSourceResolvers.size() > 1) {
      throw new RdfTemplateException(String
          .format("Expecting one, but found multiple data source resolvers for id %s: %s", dataSourceResolvers.get(0)
              .getId(), dataSourceResolvers));
    }

    return dataSourceResolvers.get(0);
  }

  public void process() {
    Map<String, Object> dataContext = rdfTemplateConfig.getDataSources()
        .getDataSources()
        .stream()
        .map(this::resolveForContext)
        .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

    rdfTemplateConfig.getTemplates()
        .forEach(template -> processTemplate(template, dataContext));
  }

  private Map.Entry<String, Object> resolveForContext(DataSource dataSource) {
    return Map.entry(dataSource.getName(), dataSourceResolverRegistry.get(dataSource.getResolver())
        .resolve(dataSource));
  }

  private void processTemplate(Template template, Map<String, Object> dataContext) {
    ConfigResourceLoaders.getResource(String.format("%s/%s", Template.TEMPLATE_DIR, template.getTemplate()))
        .ifPresentOrElse(templateResource -> evaluateAndWriteTemplate(template, templateResource, dataContext), () -> {
          throw new RdfTemplateException(String.format("Could not locate template for template config %s", template));
        });
  }

  private void evaluateAndWriteTemplate(Template template, Resource templateResource, Map<String, Object> dataContext) {
    try {
      Files.createDirectories(template.getOutputLocation());
    } catch (IOException ioException) {
      throw new RdfTemplateException(String.format("Error creating output directory %s", template.getOutputLocation()),
          ioException);
    }

    var outputPath = template.getOutputLocation()
        .resolve(template.getTemplate());

    try (var writer = Files.newBufferedWriter(outputPath, StandardOpenOption.CREATE)) {
      templater.evaluateAndWrite(templateResource.getFile(), writer, dataContext);
    } catch (IOException ioException) {
      throw new RdfTemplateException(String.format("An exception occurred while evaluating template %s", template),
          ioException);
    }
  }
}
