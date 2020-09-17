package alexrm84.services;

import alexrm84.DTOs.OrderDto;
import alexrm84.entities.User;
import alexrm84.utils.SystemUser;
import com.google.gson.JsonObject;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UserService extends UserDetailsService {
    User findByPhone(String phone);
    User save(User user);
    User update(User user);
    User save(SystemUser systemUser);
    User save(Map<String, String> params);
    User save(JsonObject jsonUserInfo) throws Exception;
    boolean isUserExist(String phone);
    User save(OrderDto orderDto);
}
