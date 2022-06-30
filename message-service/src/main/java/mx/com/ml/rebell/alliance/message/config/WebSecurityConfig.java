package mx.com.ml.rebell.alliance.message.config;

import mx.com.ml.rebell.alliance.message.config.filter.JwtRequestFilter;
import mx.com.ml.rebell.alliance.message.error.CustomAccessDeniedHandler;
import mx.com.ml.rebell.alliance.message.error.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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

  public WebSecurityConfig(final JwtRequestFilter filter,
                           final CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
    this.filter = filter;
    this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests(authorize ->
            authorize
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/swagger-ui.**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/**").authenticated()
                .anyRequest()
                .authenticated())
        .cors()
        .and()
        .addFilterBefore(filter, BasicAuthenticationFilter.class)
        .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
        .accessDeniedHandler(new CustomAccessDeniedHandler());
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
