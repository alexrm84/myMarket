package alexrm84.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto implements Serializable {
    private static final long serialVersionUID = 2323515576334898067L;

    private Long id;
    private BigDecimal price;
    private String phone;
    private String firstName;
    private String address;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDto> items;
    private String paymentType;
}
