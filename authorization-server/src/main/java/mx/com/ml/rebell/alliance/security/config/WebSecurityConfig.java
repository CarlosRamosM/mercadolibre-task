package mx.com.ml.rebell.alliance.security.config;

import mx.com.ml.rebell.alliance.security.config.filter.JwtRequestFilter;
import mx.com.ml.rebell.alliance.security.error.CustomAccessDeniedHandler;
import mx.com.ml.rebell.alliance.security.error.CustomAuthenticationEntryPoint;
import mx.com.ml.rebell.alliance.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtRequestFilter filter;

  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  private final AuthService authService;

  @Autowired
  public WebSecurityConfig(final JwtRequestFilter filter,
                           final CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                           final AuthService authService) {
    this.filter = filter;
    this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    this.authService = authService;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(authService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/v2/**").permitAll()
        .antMatchers("/swagger-ui.**").permitAll()
        .antMatchers("/swagger-ui/**").permitAll()
        .antMatchers("/swagger-resources/**").permitAll()
        .antMatchers("/webjars/**").permitAll()
        .antMatchers("/api/v1/auth/**").permitAll()
        .antMatchers("/api/v1/**").authenticated()
        .anyRequest()
        .authenticated()
        .and()
        .cors()
        .and()
        .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
        .accessDeniedHandler(new CustomAccessDeniedHandler());
    httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(List.of("http://localhost"));
    configuration.setAllowedMethods(List.of("POST"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
