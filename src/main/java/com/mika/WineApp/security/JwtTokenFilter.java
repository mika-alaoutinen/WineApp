package com.mika.WineApp.security;

import com.mika.WineApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtProvider tokenProvider;
    private final UserService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getJwt(request);
        if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
            String username = tokenProvider.getUserNameFromToken(jwt);
            UserDetails userDetails = service.loadUserByUsername(username);

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(getAuthToken(userDetails, request));
        }

        filterChain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        return authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.replace("Bearer ", "")
                : null;
    }

    private UsernamePasswordAuthenticationToken getAuthToken(UserDetails userDetails, HttpServletRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }
}
