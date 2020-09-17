package alexrm84.services;

import alexrm84.entities.ProductImage;
import alexrm84.repositories.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductImageService {
    private ProductImageRepository productImageRepository;

    @Autowired
    public void setProductImageRepository(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public ProductImage findByProductId(Long id) {
        return productImageRepository.findByProduct_id(id);
    }
}
