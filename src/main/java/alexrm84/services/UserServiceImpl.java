package alexrm84.services;

import alexrm84.DTOs.OrderDto;
import alexrm84.configs.SecurityConfig;
import alexrm84.entities.Role;
import alexrm84.entities.User;
import alexrm84.repositories.RoleRepository;
import alexrm84.repositories.UserRepository;
import alexrm84.utils.SystemUser;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private SecurityConfig securityConfig;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setSecurityConfig(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Override
    @Transactional
    public User findByPhone(String phone) {
        return userRepository.findOneByPhone(phone);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findOneByPhone(phone);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getPhone(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        return null;
    }

    @Override
    @Transactional
    public User save(SystemUser systemUser) {
        if (findByPhone(systemUser.getPhone())!=null){
            throw new RuntimeException("User with phone " + systemUser.getPhone() + " is already exist");
        }
        User user = new User();
        user.setPhone(systemUser.getPhone());
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        user.setEmail(systemUser.getEmail());
        user.setFirstName(systemUser.getFirstName());
        user.setLastName(systemUser.getLastName());
        user.setRoles(Arrays.asList(roleRepository.findOneByName("ROLE_CUSTOMER")));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User save(Map<String, String> params) {
        User user = findByPhone(params.get("phone"));
        if (user == null){
            user = new  User();
            user.setPhone(params.get("phone"));
            user.setFirstName(params.get("firstName"));
            user.setRoles(Arrays.asList(roleRepository.findOneByName("ROLE_CUSTOMER")));
            return userRepository.save(user);
        }
        return user;
    }

    @Override
    @Transactional
    public User save(JsonObject jsonUserInfo) throws Exception {
        User user = findByPhone(jsonUserInfo.get("id").toString());
        String password = passGenerator(jsonUserInfo);
        if (user==null){
            user = new  User();
            user.setPhone(jsonUserInfo.get("id").getAsString());
            user.setPassword(passwordEncoder.encode(password));
            String fName = jsonUserInfo.get("first_name").getAsString();
            fName = fName.substring(1, fName.length()-1);
            user.setFirstName(fName);
            String lName = jsonUserInfo.get("last_name").getAsString();
            lName = lName.substring(1, lName.length()-1);
            user.setLastName(lName);
            user.setRoles(Arrays.asList(roleRepository.findOneByName("ROLE_CUSTOMER")));
            user = userRepository.save(user);
            securityConfig.authSocials(jsonUserInfo.get("id").toString(), password);
            return user;
        }
        if (!passwordEncoder.matches(passGenerator(jsonUserInfo), user.getPassword())){
            user.setPassword(passwordEncoder.encode(password));
        }
        securityConfig.authSocials(jsonUserInfo.get("id").toString(), password);
        return user;
    }

    @Override
    @Transactional
    public User save(OrderDto orderDto) {
        User user = findByPhone(orderDto.getPhone());
        if (user == null){
            user = new  User();
            user.setPhone(orderDto.getPhone());
            user.setFirstName(orderDto.getFirstName());
            user.setRoles(Arrays.asList(roleRepository.findOneByName("ROLE_CUSTOMER")));
            return userRepository.save(user);
        }
        return user;
    }

    private String passGenerator(JsonObject jsonUserInfo) {
        String str = jsonUserInfo.get("id").toString().substring(0,4) +
                jsonUserInfo.get("first_name").toString().substring(0,3) +
                jsonUserInfo.get("last_name").toString().substring(0,3);
        return str;
    }



//    private String passGenerator(int charLength) {
//        return String.valueOf(charLength < 1 ? 0 : new Random()
//                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
//                + (int) Math.pow(10, charLength - 1));
//    }

    @Override
    public boolean isUserExist(String phone) {
        return userRepository.existsByPhone(phone);
    }
}
