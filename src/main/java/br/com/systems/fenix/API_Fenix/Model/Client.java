package br.com.systems.fenix.API_Fenix.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty
    @Size(min = 3)
    @Column(name = "firstname", length = 255, nullable = false)
    private String firstName;

    @NotEmpty
    @Size(min = 3)
    @Column(name = "lastName", length = 255, nullable = false)
    private String lastName;

    @NotEmpty
    @Column(name = "age", length = 2, nullable = false)
    private Integer age;

    @NotEmpty
    @Column(name = "telephone", length = 11, nullable = false)
    private String telephone;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Size(min = 8, message = "Password has to be at least 8 characters long")
    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Promotion> promotions;

}
