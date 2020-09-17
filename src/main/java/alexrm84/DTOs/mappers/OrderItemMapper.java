package alexrm84.DTOs.mappers;

import alexrm84.DTOs.OrderItemDto;
import alexrm84.entities.OrderItem;
import alexrm84.services.OrderService;
import alexrm84.services.ProductService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class OrderItemMapper {
    private ModelMapper mapper;
    private ProductService productService;
    private OrderService orderService;

    @Autowired
    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public OrderItem toEntity(OrderItemDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, OrderItem.class);
    }

    public OrderItemDto toDto(OrderItem entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, OrderItemDto.class);
    }

    public void setupMapper() {
        mapper.createTypeMap(OrderItem.class, OrderItemDto.class)
                .addMappings(m -> m.skip(OrderItemDto::setOrderId))
                .addMappings(m -> m.skip(OrderItemDto::setProductId))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(OrderItemDto.class, OrderItem.class)
                .addMappings(m -> m.skip(OrderItem::setOrder))
                .addMappings(m -> m.skip(OrderItem::setProduct))
                .setPostConverter(toEntityConverter());
    }

    private Converter<OrderItem, OrderItemDto> toDtoConverter() {
        return mappingContext -> {
            OrderItem source = mappingContext.getSource();
            OrderItemDto dest = mappingContext.getDestination();
            mapSpecificFields(source, dest);
            return mappingContext.getDestination();
        };
    }

    private void mapSpecificFields(OrderItem source, OrderItemDto dest) {
        dest.setOrderId(Objects.isNull(source) ? null : source.getOrder().getId());
        dest.setProductId(Objects.isNull(source) ? null : source.getProduct().getId());
    }

    private Converter<OrderItemDto, OrderItem> toEntityConverter() {
        return mappingContext -> {
            OrderItemDto source = mappingContext.getSource();
            OrderItem dest = mappingContext.getDestination();
            mapSpecificFields(source, dest);
            return mappingContext.getDestination();
        };
    }

    private void mapSpecificFields(OrderItemDto source, OrderItem dest) {
        dest.setOrder(Objects.isNull(source) ? null : orderService.findOneById(source.getOrderId()));
        dest.setProduct(Objects.isNull(source) ? null : productService.findById(source.getProductId()).get());
    }
}
