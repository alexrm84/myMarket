package alexrm84.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = -7303616358855139206L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private BigDecimal price;

//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private List<ProductImage> images;

    @OneToOne
    @JoinColumn(name = "image_id")
    private ProductImage image;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;
}
