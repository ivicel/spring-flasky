package info.ivicel.springflasky.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.ivicel.springflasky.core.WebSecurityAuth;
import info.ivicel.springflasky.web.service.PostService;
import info.ivicel.springflasky.web.service.UserService;
import java.util.HashMap;
import java.util.Map;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@Setter(onMethod = @__({@Autowired}))
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userService")
    private UserDetailsService userService;

    @Qualifier("loginFailureHandler")
    private AuthenticationFailureHandler loginFailureHandler;

    @Qualifier("loginSuccessHandler")
    private AuthenticationSuccessHandler loginSuccessHandler;

    @Qualifier("accessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;

    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // login and logout authenticate
        http.authorizeRequests()
                .antMatchers("/auth/login").access("not @webAuth.loginRequired(authentication)")
                .antMatchers("/auth/logout").access("@webAuth.loginRequired(authentication)");

        // 403 handler
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // other urls
        http.authorizeRequests().anyRequest().permitAll();

        // custom form login, logout
        http.formLogin().loginPage("/auth/login")
                .failureHandler(loginFailureHandler).successHandler(loginSuccessHandler)
                .and()
                .logout().logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    Map<String, String> map = new HashMap<>();
                    map.put("url", request.getContextPath() + "/");
                    new ObjectMapper().writeValue(response.getWriter(), map);
                })
                .clearAuthentication(true).invalidateHttpSession(true)
                .and().rememberMe();

        // http.csrf().disable()
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }


    @Bean("webAuth")
    public WebSecurityAuth securityUtilUser(UserService userService, PostService postService) {
        WebSecurityAuth webAuth = new WebSecurityAuth();
        webAuth.setUserService(userService);
        webAuth.setPostService(postService);

        return webAuth;
    }
}
