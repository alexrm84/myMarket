package alexrm84.repositories;

import alexrm84.entities.Order;
import alexrm84.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>, JpaSpecificationExecutor<Order> {

    ProductImage findByProduct_id(Long id);
}
