package alexrm84.configs;

import alexrm84.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private AuthEntryPoint authEntryPoint;
    private RequestFilter requestFilter;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public SecurityConfig(AuthEntryPoint authEntryPoint, RequestFilter requestFilter) {
        this.authEntryPoint = authEntryPoint;
        this.requestFilter = requestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/products/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/users/**").hasRole("ADMIN")
                .antMatchers("/shop/order/**").authenticated()
                .antMatchers("/profile/**").authenticated()
                .anyRequest().permitAll()
                .and().formLogin().loginPage("/login").loginProcessingUrl("/authenticateTheUser").permitAll()
                .and().logout().logoutSuccessUrl("/shop").permitAll();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/admin/**", "/admin/products/**", "/admin/users/**").hasRole("ADMIN")
//                .antMatchers("/order/{id}/**", "/profile/**").authenticated()
//                .anyRequest().permitAll()
//                .and().exceptionHandling().authenticationEntryPoint(authEntryPoint)
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
//        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    public void authSocials(String login, String password) throws Exception {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(login, password);
        Authentication auth = authenticationManager().authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
    }
}
