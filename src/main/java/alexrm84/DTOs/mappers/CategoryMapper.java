package alexrm84.DTOs.mappers;

import alexrm84.DTOs.CategoryDto;
import alexrm84.entities.Category;
import alexrm84.services.ProductService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CategoryMapper {
    private ModelMapper mapper;
    private ProductService productService;

    @Autowired
    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public Category toEntity(CategoryDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Category.class);
    }

    public CategoryDto toDto(Category entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, CategoryDto.class);
    }

    public void setupMapper() {
        mapper.createTypeMap(CategoryDto.class, Category.class)
                .addMappings(m -> m.skip(Category::setProducts))
                .setPostConverter(toEntityConverter());
    }

    private Converter<CategoryDto, Category> toEntityConverter() {
        return mappingContext -> {
            CategoryDto source = mappingContext.getSource();
            Category dest = mappingContext.getDestination();
            mapSpecificFields(source, dest);
            return mappingContext.getDestination();
        };
    }

    private void mapSpecificFields(CategoryDto source, Category dest) {
        dest.setProducts(Objects.isNull(source) ? null : productService.findAllByCategoryId(source.getId()));
    }
}
