package alexrm84.controllers.rest;

import alexrm84.entities.Product;
import alexrm84.errorHandlers.ProductException;
import alexrm84.services.ProductService;
import alexrm84.utils.ProductsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<Page<Product>> getPage(HttpServletRequest request,
                                                 @RequestParam(value = "currentPage", required = false) Integer currentPage){
        ProductsFilter productsFilter = new ProductsFilter(request);
        if (currentPage == null || currentPage < 1) {
            currentPage = 1;
        }
        Page<Product> page = productService.findAllByPagingAndFiltering(productsFilter.getSpecification(), PageRequest.of(currentPage - 1, 10, Sort.Direction.ASC, "id"));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long id) {
        if (!productService.existsById(id)) {
            throw new ProductException("Product with id: " + id + " not found");
        }
        return new ResponseEntity<>(productService.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        if (product.getId()!=null){
            throw new ProductException("Product must not contain id");
        }
        if (productService.findByTitle(product.getTitle()) != null) {
            throw new ProductException("Product already exist");
        }
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        if (!productService.existsById(product.getId())) {
            throw new ProductException("Product with id: " + product.getId() + " not found");
        }
        return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") Long id) {
        if (!productService.existsById(id)) {
            throw new ProductException("Product with id: " + id + " not found");
        }
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
