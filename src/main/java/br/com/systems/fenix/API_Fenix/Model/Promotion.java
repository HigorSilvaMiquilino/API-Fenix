package br.com.systems.fenix.API_Fenix.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "Promotion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "promotionName", length = 255, nullable = false)
    private String promotionName;

    @NotEmpty
    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "prize", length = 255, nullable = false)
    private Integer prize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}