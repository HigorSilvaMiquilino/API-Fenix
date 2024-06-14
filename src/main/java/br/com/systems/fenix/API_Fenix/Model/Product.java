package br.com.systems.fenix.API_Fenix.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "productName", length = 255, nullable = false)
    private String productName;

    @NotEmpty
    @Column(name = "price", length = 5, nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;
}
