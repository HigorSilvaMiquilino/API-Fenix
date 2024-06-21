package br.com.systems.fenix.API_Fenix.exception;

public class ClientIdNotFoundException extends RuntimeException {

  private final Long id;

  public ClientIdNotFoundException(String message, Long ID) {
    super(message);
    this.id = ID;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return super.toString() + "[ Client ID: " + id + "]";
  }
}
