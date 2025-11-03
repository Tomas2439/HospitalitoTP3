package com.utn.Hospitalito.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Bean encargado de codificar las contraseñas.
     *
     * Se utiliza BCrypt, un algoritmo robusto y recomendado por Spring Security
     * para almacenar contraseñas de manera segura (aplica un "salt" y es resistente a ataques de fuerza bruta).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que define el servicio de autenticación de usuarios.
     *
     * En este caso se usa un usuario "hardcodeado" (en memoria) para simplificar
     * las pruebas y no depender de una base de datos.
     *
     * - Usuario: jorge
     * - Contraseña: 1234 (codificada con BCrypt)
     * - Rol: USER
     */
    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = passwordEncoder();

        UserDetails user = User.withUsername("jorge")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build();

        // InMemoryUserDetailsManager guarda el usuario en memoria (no en BD)
        return new InMemoryUserDetailsManager(user);
    }//userDetailsService()

    /**
     * Bean principal que configura la cadena de filtros de seguridad (Security Filter Chain).
     *
     * Aquí se definen todas las reglas de seguridad:
     * - Qué rutas requieren autenticación
     * - Cómo se maneja el login y el logout
     * - Si se usa o no CSRF (Cross-Site Request Forgery)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //  Desactiva la protección CSRF (solo para desarrollo o pruebas)
                // En producción debería habilitarse correctamente.
                .csrf(AbstractHttpConfigurer::disable)

                // Configura las reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Cualquier otra URL requiere usuario autenticado
                        .anyRequest().authenticated()
                )

                // Configuración del formulario de inicio de sesión (login)
                .formLogin(form -> form
                        // URL a la que redirige al iniciar sesión correctamente
                        .defaultSuccessUrl("/hospitalito", true)
                )

                // Configuración de cierre de sesión (logout)
                .logout(logout -> logout
                        // URL para cerrar sesión
                        .logoutUrl("/logout")
                        // Redirige al login con mensaje de "logout exitoso"
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        // Devuelve la cadena de filtros configurada
        return http.build();
    }//securityFilterChain()
}//class SecurityConfig



