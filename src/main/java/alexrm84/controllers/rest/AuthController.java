package alexrm84.controllers.rest;

import alexrm84.DTOs.AuthRequestDto;
import alexrm84.DTOs.AuthResponseDto;
import alexrm84.DTOs.AuthUserDto;
import alexrm84.services.UserService;
import alexrm84.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthController {
    private AuthenticationManager authManager;
    private JwtUtil jwtUtil;
    private UserService userService;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserService userService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<String> home() {
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthRequestDto authRequestDto) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = userService.loadUserByUsername(authRequestDto.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        return new  ResponseEntity(new AuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody AuthUserDto authUserDto, BindingResult bindingResult) {
        String phone = authUserDto.getPhone();
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ObjectError o : bindingResult.getAllErrors()) {
                errorMessage.append(o.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }
        if (userService.isUserExist(phone)) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.save(authUserDto), HttpStatus.OK);
    }
}
