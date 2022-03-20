package co.uk.howardpaget.stampy.controller.dto;

public class StampRequest {
  private String name;

  public StampRequest() {
  }

  public StampRequest(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
