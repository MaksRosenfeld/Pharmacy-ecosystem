package ru.budgetapteka.pharmacyecosystem.security;

//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//
//public class SecurityConfig {
//
//    @Bean
//    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests(auth -> auth
//                        .antMatchers("/salary").hasRole(Roles.ADMIN.name())
//                        .anyRequest().authenticated())
//                .formLogin().loginPage("/login").permitAll()
//                .and()
//                .logout()
//                .deleteCookies("check-costs", "order-statement", "costs");
//        return http.build();
//
//
//    }
//
//    @Bean
//    protected JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
////        UserDetails user = User.builder()
////                .username("guest")
////                .password(getPwEncoder().encode("iamtheguest"))
////                .roles(Roles.GUEST.name())
////                .build();
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
////        userDetailsManager.createUser(user);
//        return userDetailsManager;
//    }
//
//    @Bean
//    protected PasswordEncoder getPwEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }
//
//}
