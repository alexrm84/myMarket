package alexrm84.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDto implements Serializable {
    private static final long serialVersionUID = -5269633442454974197L;

    private Long id;
    private Long productId;
    private Long orderId;
    private Integer quantity;
    private BigDecimal itemPrice;
    private BigDecimal totalPrice;
}
