package com.example.spring.config;


import com.example.spring.service.UserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class AppSecurityConfig {
    private final UserDetailService userDetailService;


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 요청에 대해 CORS 허용
                        .allowedOrigins("http://localhost:3000") // CORS 허용할 도메인
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                        .allowedHeaders("*") // 허용할 헤더
                        .allowCredentials(true); // 자격 증명 포함 여부
            }
        };
    }

    // 예외처리
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 다음 등록된 내용은 보안 정책이 무시된다
        return webSecurity -> webSecurity.ignoring()
                .requestMatchers(toH2Console()) // ~/h2_console
                .requestMatchers("/static/**") // 정적 데이터 모든 것
                ;
    }

    // 인증 필요한 페이지, 아닌 페이지 구분/  로그인, 로그아웃 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                // 1. 인증이 필요한 페이지와 아닌 페이지
                .authorizeRequests()
                    // 아래 페이지는 인증 필요 x
                    .requestMatchers("/login", "/user/signup","/user/signup1_process", "/user/signup2_process"
                    , "/user/findId").permitAll() //허가
                    .anyRequest().authenticated() // 나머지는 안됨
                .and()
                // 2. 로그인 페이지(커스텀), 로그인 성공 후 포워딩 페이지등 지정
                .formLogin()
                .loginProcessingUrl("/login") // `커스텀` 로그인 페이지 이동
                // 로그인 성공 시 응답 메세지 작성하여 전송 http 200 OK 상태코드 보냄
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Login successful!");
                })
                // 로그인 실패 시 응답 메세지, http 401 Unauthorized 상태 코드 보냄
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Login failed!");
                }) // 로그인 실패 시 커스텀 응답
                .and()
                // 3. 로그아웃 처리, 등 추가 처리
                .logout()
                // 로그아웃 요청 완료되면 200 ok 코드 보냄
                .logoutUrl("/logout") // 로그아웃 요청 URL
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Logout successful! hihii");
                }) // 로그아웃 성공 시 커스텀 응답
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Authentication required");
                    }))
                .and()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserDetailService userDetailService) throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                // 인증 관련하여 디비에 쿼리를 수행하여 존재여부 체크
                // 서비스를 이용해서 내가 입력한 이메일을 통해 유저 정보를 가져온다
                .userDetailsService(this.userDetailService)
                // 입력한 비밀번호를 암호화해서, 유저 엔티티에 저장된 암호와 동일한지 검증한다.
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    // 한 유저 엔티티의 비밀번호는 동일한 해시 함수로 암호화를 해야 하기 때문에
    // 동일한 해시 함수를 사용하기 위해 아래의 함수를 사용하여 동일한 해시함수 입력
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
