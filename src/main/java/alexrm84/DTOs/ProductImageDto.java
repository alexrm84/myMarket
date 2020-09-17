package alexrm84.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ProductImageDto implements Serializable {
    private static final long serialVersionUID = -7229710218072123897L;

    private Long id;
    private String path;
}
