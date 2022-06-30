package mx.com.ml.rebell.alliance.security.dm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "user", schema = "public")
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Basic(optional = false)
  @Column(name = "name")
  private String name;

  @Basic(optional = false)
  @Column(name = "family_name")
  private String familyName;

  @Basic(optional = false)
  @Column(name = "birthday")
  private LocalDate birthDate;

  @Basic(optional = false)
  @Column(name = "email")
  private String username;

  @Basic(optional = false)
  @Column(name = "password")
  private String password;
}
