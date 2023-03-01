package ir.maktab.project_final_faz2.config;

import ir.maktab.project_final_faz2.data.model.repository.PersonRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.jwt.JwtConfig;
import ir.maktab.project_final_faz2.jwt.JwtTokenVerifier;
import ir.maktab.project_final_faz2.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenVerifier jwtTokenVerifier;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public SecurityConfig(PersonRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenVerifier jwtTokenVerifier, JwtConfig jwtConfig, SecretKey secretKey) {
        this.personRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenVerifier = jwtTokenVerifier;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtConfig, secretKey))
                .addFilterAfter(jwtTokenVerifier, JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/expert/save_expert").permitAll()
                .requestMatchers(("/expert/verify")).permitAll()
                .requestMatchers("/customer/register").permitAll()
                .requestMatchers("/expert/*").hasAuthority("EXPERT")
                .requestMatchers("/admin/*").hasAuthority("ADMIN")
                .requestMatchers("/customer/*").hasAuthority("CUSTOMER")
                .anyRequest().authenticated().and().httpBasic();
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService((email) -> personRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new NotFoundException(String.format("This %s notFound!", email))))
                .passwordEncoder(passwordEncoder);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
