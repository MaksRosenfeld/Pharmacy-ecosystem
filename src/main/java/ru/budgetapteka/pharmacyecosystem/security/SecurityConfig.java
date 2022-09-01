package ru.budgetapteka.pharmacyecosystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(auth -> auth
                        .antMatchers("/salary").hasAnyRole(UserRoles.ADMIN.name(), UserRoles.PHARMACY.name())
                        .anyRequest().authenticated())
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID", "check-costs", "order-statement", "costs");

        return http.build();


    }

    @Bean
    protected JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        UserDetails user = User.builder()
                .username("salary")
                .password(getPwEncoder().encode("salaryapteka1"))
                .roles(UserRoles.PHARMACY.name())
                .build();
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//        userDetailsManager.createUser(user);
        return userDetailsManager;
    }

    @Bean
    protected PasswordEncoder getPwEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
