package alexrm84.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CategoryDto implements Serializable {
    private static final long serialVersionUID = 4274183532192908850L;
    private Long id;
    private String title;
}
