package alexrm84.repositories;

import alexrm84.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Product findOneByTitle(String s);

    @Query(value = "Select max(p.cost) from products p", nativeQuery = true)
    Integer findMaxCost();

    @Query(value = "Select min(p.cost) from products p", nativeQuery = true)
    Integer findMinCost();

    List<Product> findAllByCategory_id(Long id);
}
