package com.sena.henosis.security;

import com.sena.henosis.security.Service.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

	private final MongoUserDetailsService userDetailsService;

	@Autowired
	public SecurityConfiguration(MongoUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
		return new AnonymousAuthenticationFilter("INVITADO", "INVITADO",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_INVITADO")));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
		accessDeniedHandler.setErrorPage("/acceso-denegado"); // Ruta de la página de error personalizada
		return accessDeniedHandler;
	}

	@Bean
	public ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy() {
		ConcurrentSessionControlAuthenticationStrategy strategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
		strategy.setMaximumSessions(1);
		return strategy;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler() {
		return new HttpStatusReturningLogoutSuccessHandler();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authConfig -> {
			authConfig.requestMatchers(HttpMethod.GET, "/", "/newAprendiz","/saveAprendiz", "/newUsuario", "/saveUsuario","/acceso-denegado", "home","index", "/login", "/recuperar-contrasena", "/resetear-contrasena/{codigoRecuperacion}").permitAll();
			authConfig.requestMatchers(HttpMethod.POST, "/","/newAprendiz","/saveAprendiz","/newUsuario", "/saveUsuario", "/acceso-denegado", "home","index","/recuperar-contrasena", "/resetear-contrasena/{codigoRecuperacion}").permitAll();
			authConfig.requestMatchers(HttpMethod.GET, "/Admin").hasRole("Admin");
			authConfig.requestMatchers(HttpMethod.GET, "/Apoyo").hasRole("Apoyo");
					authConfig.requestMatchers(HttpMethod.GET, "/Instructor").hasRole("Instructor");
					authConfig.requestMatchers(HttpMethod.GET, "/Aprendiz").hasRole("Aprendiz");
			authConfig.requestMatchers(HttpMethod.GET, "/css/**", "/js/**", "/img/**", "/vendor/**").permitAll(); // Permitir acceso público a recursos estáticos
			authConfig.anyRequest().authenticated();
		}).formLogin(formLoginConfig -> {
			formLoginConfig.loginPage("/login").permitAll();
		}).httpBasic(Customizer.withDefaults());

		http.headers().cacheControl().disable(); // Desactivar la caché

		// Configurar el manejador de acceso denegado personalizado dentro de http.exceptionHandling()
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

		// Configurar la gestión de sesiones
		http.sessionManagement()
				.maximumSessions(1) // Solo permitir una sesión activa por usuario
				.maxSessionsPreventsLogin(true) // Evitar que el usuario inicie sesión desde una segunda ubicación
				.sessionRegistry(sessionRegistry())
				.expiredUrl("/login") // Redirigir al usuario a la página de inicio de sesión al expirar la sesión
				.and()
				.invalidSessionUrl("/login"); // Redirigir a la página de inicio de sesión cuando la sesión ha expirado o es inválida


		// Configurar el logout y su manejador
		http.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessHandler(logoutSuccessHandler())
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/");

		return http.build();
	}
}

