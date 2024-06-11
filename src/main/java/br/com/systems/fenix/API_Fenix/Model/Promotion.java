package br.com.systems.fenix.API_Fenix.Model;


import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String promotionName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer prize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}
