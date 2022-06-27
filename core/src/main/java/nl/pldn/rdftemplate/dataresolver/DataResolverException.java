package nl.pldn.rdftemplate.dataresolver;

import lombok.NonNull;

public class DataResolverException extends RuntimeException {

  private static final long serialVersionUID = 344835896458145273L;

  public DataResolverException(@NonNull String message) {
    super(message);
  }

  public DataResolverException(@NonNull String message, Throwable cause) {
    super(message, cause);
  }
}
