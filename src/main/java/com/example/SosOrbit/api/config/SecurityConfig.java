package com.example.SosOrbit.api.config;

import com.example.SosOrbit.api.security.JwtAuthenticationFilter;
import com.example.SosOrbit.api.security.RestAccessDeniedHandler;
import com.example.SosOrbit.api.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/regioes/**").hasAnyRole("OPERADOR", "DEFESA_CIVIL", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/regioes/**").hasAnyRole("DEFESA_CIVIL", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/regioes/**").hasAnyRole("DEFESA_CIVIL", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/regioes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/dados-ambientais/**", "/api/alertas/**").hasAnyRole("OPERADOR", "DEFESA_CIVIL", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/dados-ambientais/**").hasAnyRole("DEFESA_CIVIL", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/dados-ambientais/**", "/api/alertas/**").hasAnyRole("DEFESA_CIVIL", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/dados-ambientais/**", "/api/alertas/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/abrigos/**", "/api/recursos/**").hasAnyRole("OPERADOR", "DEFESA_CIVIL", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/abrigos/**", "/api/recursos/**").hasAnyRole("DEFESA_CIVIL", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/abrigos/**", "/api/recursos/**").hasAnyRole("DEFESA_CIVIL", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/abrigos/**", "/api/recursos/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    UserDetailsService users(PasswordEncoder encoder) {
        UserDetails operador = User.withUsername("operador")
                .password(encoder.encode("123456"))
                .roles("OPERADOR")
                .build();

        UserDetails defesaCivil = User.withUsername("defesa")
                .password(encoder.encode("defesa123"))
                .roles("DEFESA_CIVIL")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(operador, defesaCivil, admin);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
