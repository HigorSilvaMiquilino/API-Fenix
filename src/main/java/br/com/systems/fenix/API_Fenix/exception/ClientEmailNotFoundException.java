package br.com.systems.fenix.API_Fenix.exception;

public class ClientEmailNotFoundException extends RuntimeException {

  private String email;

  public ClientEmailNotFoundException(String message, String email) {
    super(message);
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return super.toString() + "[ Client email: " + email + "]";
  }

}
