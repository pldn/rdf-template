package nl.pldn.rdftemplate.templating;

import java.io.File;
import java.io.Writer;
import java.util.Map;

public interface Templater {

  void evaluateAndWrite(File templateFile, Writer writer, Map<String, Object> dataContext);
}
