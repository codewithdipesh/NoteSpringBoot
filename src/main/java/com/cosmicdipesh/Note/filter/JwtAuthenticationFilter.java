package com.cosmicdipesh.Note.filter;


import com.cosmicdipesh.Note.ExceptionHandler.ValidationException;
import com.cosmicdipesh.Note.entity.InvalidJwtException;
import com.cosmicdipesh.Note.entity.User;
import com.cosmicdipesh.Note.repository.UserRepository;
import com.cosmicdipesh.Note.service.JwtService;
import com.cosmicdipesh.Note.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtservice;

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtservice, UserServiceImpl userService, UserRepository userRepository) {
        this.jwtservice = jwtservice;
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Only apply JWT authentication filter to paths that start with "/user" or "/note"
        if (path.startsWith("/user") || path.startsWith("/note")) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                setErrorResponse(response, "Invalid JWT token", HttpStatus.UNAUTHORIZED.value());
                return;
            }

            String token = authHeader.replace("Bearer ", "");
            String username = jwtservice.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User foundUser = userRepository.findByUsername(username).orElse(null);
                if (foundUser != null) {
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    if (jwtservice.isValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        setErrorResponse(response, "Invalid JWT token", HttpStatus.UNAUTHORIZED.value());
                        return;
                    }
                } else {
                    setErrorResponse(response, "Invalid JWT token or User Not Found", HttpStatus.UNAUTHORIZED.value());
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }


    private void setErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(
                String.format(message)
        );
    }


}

