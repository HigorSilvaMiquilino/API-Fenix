package br.com.systems.fenix.API_Fenix.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.systems.fenix.API_Fenix.Model.enuns.ProfileEnum;

@Entity
@Table(name = "Client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 3)
    @Column(name = "firstname", length = 255)
    private String firstName;

    @NotBlank
    @Size(min = 3)
    @Column(name = "lastName", length = 255)
    private String lastName;

    @NotEmpty
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotBlank
    @Column(name = "telephone", length = 20)
    private String telephone;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotBlank
    @Column(name = "email", length = 255, unique = true)
    private String email;

    @Size(min = 8, message = "Password has to be at least 8 characters long")
    @NotBlank
    @Column(name = "password", length = 100, unique = true)
    private String password;

    @Column(nullable = false)
    private boolean isEnabled;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Promotion> promotions;

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @CollectionTable(name = "user_profile")
    @Column(name = "profile", nullable = false)
    @Builder.Default
    private Set<Integer> profiles = new HashSet<>();

    @Column(name = "imageURL")
    private String imageURL;

    public Set<ProfileEnum> getProfile() {
        return this.profiles.stream().map(enumeration -> ProfileEnum.toEnum(enumeration)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }
}
