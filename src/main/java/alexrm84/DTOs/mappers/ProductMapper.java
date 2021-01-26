package alexrm84.DTOs.mappers;

import alexrm84.DTOs.ProductDto;
import alexrm84.entities.Product;
import alexrm84.services.CategoryService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProductMapper {
    private ModelMapper mapper;
    private ProductImageMapper productImageMapper;
    private CategoryService categoryService;

    @Autowired
    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setProductImageMapper(ProductImageMapper productImageMapper) {
        this.productImageMapper = productImageMapper;
    }

    public Product toEntity(ProductDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Product.class);
    }

    public ProductDto toDto(Product entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, ProductDto.class);
    }

    public void setupMapper() {
        mapper.createTypeMap(Product.class, ProductDto.class)
                .addMappings(m -> m.skip(ProductDto::setCategoryId))
                .addMappings(m -> m.skip(ProductDto::setProductImageDto))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(ProductDto.class, Product.class)
                .addMappings(m -> m.skip(Product::setCategory))
                .addMappings(m -> m.skip(Product::setImage))
                .setPostConverter(toEntityConverter());
    }

    private Converter<Product, ProductDto> toDtoConverter() {
        return mappingContext -> {
            Product source = mappingContext.getSource();
            ProductDto dest = mappingContext.getDestination();
            mapSpecificFields(source, dest);
            return mappingContext.getDestination();
        };
    }

    private void mapSpecificFields(Product source, ProductDto dest) {
        dest.setCategoryId(Objects.isNull(source) ? null : source.getCategory().getId());
        dest.setProductImageDto(Objects.isNull(source) ? null : productImageMapper.toDto(source.getImage()));
    }

    private Converter<ProductDto, Product> toEntityConverter() {
        return mappingContext -> {
            ProductDto source = mappingContext.getSource();
            Product dest = mappingContext.getDestination();
            mapSpecificFields(source, dest);
            return mappingContext.getDestination();
        };
    }

    private void mapSpecificFields(ProductDto source, Product dest) {
        dest.setCategory(Objects.isNull(source) ? null : categoryService.findById(source.getCategoryId()).get());
        dest.setImage(Objects.isNull(source) ? null : productImageMapper.toEntity(source.getProductImageDto()));
    }
}
