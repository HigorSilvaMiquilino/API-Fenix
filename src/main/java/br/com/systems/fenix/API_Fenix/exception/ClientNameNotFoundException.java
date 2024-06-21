package br.com.systems.fenix.API_Fenix.exception;

public class ClientNameNotFoundException extends RuntimeException {

  private String name;

  public ClientNameNotFoundException(String message, String name) {
    super(message);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return super.toString() + "[ Client name: " + name + "]";
  }
}
