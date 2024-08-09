package com.cosmicdipesh.Note.filter;


import com.cosmicdipesh.Note.service.JwtService;
import com.cosmicdipesh.Note.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
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

    public JwtAuthenticationFilter(JwtService jwtservice, UserServiceImpl userService) {
        this.jwtservice = jwtservice;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException
    {
         String authHeader = request.getHeader("Authorization");
         if(authHeader == null || !authHeader.startsWith("Bearer ")){
             filterChain.doFilter(request, response);
             return;
         }
         String token = authHeader.replace("Bearer ", "");
         String username = jwtservice.extractUsername(token);

         if(username == null && SecurityContextHolder.getContext().getAuthentication() == null){
             UserDetails userDetails = userService.loadUserByUsername(username);

             if(jwtservice.isValid(token, userDetails)){
                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                         userDetails, null, userDetails.getAuthorities()
                 );
                 authToken.setDetails(
                         new WebAuthenticationDetailsSource().buildDetails(request)
                 );
                 SecurityContextHolder.getContext().setAuthentication(authToken);
             }

             filterChain.doFilter(request,response);


         }
    }
}
