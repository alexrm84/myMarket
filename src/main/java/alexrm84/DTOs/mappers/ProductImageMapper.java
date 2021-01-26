package alexrm84.DTOs.mappers;

import alexrm84.DTOs.ProductImageDto;
import alexrm84.entities.ProductImage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProductImageMapper {
    private ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ProductImage toEntity(ProductImageDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, ProductImage.class);
    }

    public ProductImageDto toDto(ProductImage entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, ProductImageDto.class);
    }
}
