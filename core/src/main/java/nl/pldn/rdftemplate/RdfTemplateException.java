package nl.pldn.rdftemplate;

import lombok.NonNull;

public class RdfTemplateException extends RuntimeException {

  private static final long serialVersionUID = -5995726737972321238L;

  public RdfTemplateException(@NonNull String message) {
    super(message);
  }

  public RdfTemplateException(@NonNull String message, Throwable cause) {
    super(message, cause);
  }
}
