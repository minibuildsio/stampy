package co.uk.howardpaget.stampy.entity;



import jakarta.persistence.*;

@Entity
@Table(name = "stampyuser")
public class StampyUserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password_hash;

  public StampyUserEntity() {
  }

  public StampyUserEntity(Long id, String username, String password_hash) {
    this.id = id;
    this.username = username;
    this.password_hash = password_hash;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPasswordHash() {
    return password_hash;
  }
}
