package nl.pldn.rdftemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RdfTemplateApplication implements CommandLineRunner {

  private final RdfTemplateProcessor rdfTemplateProcessor;

  public RdfTemplateApplication(RdfTemplateProcessor rdfTemplateProcessor) {
    this.rdfTemplateProcessor = rdfTemplateProcessor;
  }

  public static void main(String... args) {
    SpringApplication.run(RdfTemplateApplication.class, args)
        .close();
  }

  @Override
  public void run(String... args) {
    rdfTemplateProcessor.process();
  }
}
