package alexrm84.DTOs.mappers;

import alexrm84.DTOs.OrderDto;
import alexrm84.entities.Order;
import alexrm84.services.UserService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.stream.Collectors;

public class OrderMapper {
    private ModelMapper mapper;
    private UserService userService;
    private OrderItemMapper orderItemMapper;

    @Autowired
    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderItemMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public Order toEntity(OrderDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    public OrderDto toDto(Order entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, OrderDto.class);
    }

    public void setupMapper() {
        mapper.createTypeMap(Order.class, OrderDto.class)
                .addMappings(m -> m.skip(OrderDto::setFirstName))
                .addMappings(m -> m.skip(OrderDto::setStatus))
                .addMappings(m -> m.skip(OrderDto::setItems))
                .addMappings(m -> m.skip(OrderDto::setPaymentType))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(OrderDto.class, Order.class)
                .addMappings(m -> m.skip(Order::setUser))
                .addMappings(m -> m.skip(Order::setStatus))
                .addMappings(m -> m.skip(Order::setItems))
                .setPostConverter(toEntityConverter());
    }

    private Converter<Order, OrderDto> toDtoConverter() {
        return mappingContext -> {
            Order source = mappingContext.getSource();
            OrderDto dest = mappingContext.getDestination();
            mapSpecificFields(source, dest);
            return mappingContext.getDestination();
        };
    }

    private void mapSpecificFields(Order source, OrderDto dest) {
        dest.setFirstName(Objects.isNull(source) ? null : source.getUser().getFirstName());
        dest.setStatus(Objects.isNull(source) ? null : source.getStatus().name());
        dest.setItems(Objects.isNull(source) ? null : source.getItems().stream().map(i -> orderItemMapper.toDto(i)).collect(Collectors.toList()));
    }

    private Converter<OrderDto, Order> toEntityConverter() {
        return mappingContext -> {
            OrderDto source = mappingContext.getSource();
            Order dest = mappingContext.getDestination();
            mapSpecificFields(source, dest);
            return mappingContext.getDestination();
        };
    }

    private void mapSpecificFields(OrderDto source, Order dest) {
        dest.setUser(Objects.isNull(source) ? null : userService.findByPhone(source.getPhone()));
        dest.setStatus(Objects.isNull(source) ? null : Order.Status.valueOf(source.getStatus()));
        dest.setItems(Objects.isNull(source) ? null : source.getItems().stream().map(i -> orderItemMapper.toEntity(i)).collect(Collectors.toList()));
    }
}
