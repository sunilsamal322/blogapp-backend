package com.techblog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtAuthEntryPoints jwtAuthEntryPoints;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoints).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/users/signup","/users/signup/admin").permitAll()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                .antMatchers(HttpMethod.GET,"/category/**").permitAll()
                .antMatchers(HttpMethod.GET,"/posts/**").permitAll()
                .antMatchers(HttpMethod.GET,"/comments/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public FilterRegistrationBean coresFilter(){

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration=new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("Authorization");
        corsConfiguration.addAllowedHeader("Content-Type");
        corsConfiguration.addAllowedHeader("Accept");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**",corsConfiguration);

        FilterRegistrationBean bean=new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(-110);
        return bean;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
