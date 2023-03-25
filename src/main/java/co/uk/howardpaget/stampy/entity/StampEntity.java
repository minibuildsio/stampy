package co.uk.howardpaget.stampy.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "stamp")
public class StampEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  public StampEntity() {
  }

  public StampEntity(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}
