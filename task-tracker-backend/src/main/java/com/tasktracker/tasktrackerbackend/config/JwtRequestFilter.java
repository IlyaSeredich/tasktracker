package com.tasktracker.tasktrackerbackend.config;

import com.tasktracker.tasktrackerbackend.exception.JwtTokenExpiredException;
import com.tasktracker.tasktrackerbackend.exception.WrongJwtTokenSignException;
import com.tasktracker.tasktrackerbackend.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = getJwtTokenFromRequest(request);
        String username = getUsernameFromJwtToken(jwtToken);
        createAuthentication(username, jwtToken);
        filterChain.doFilter(request, response);
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private String getUsernameFromJwtToken(String jwtToken) {
        if(jwtToken != null) {
            try {
                return jwtTokenUtils.getUsernameFromToken(jwtToken);
            }
            catch (ExpiredJwtException ex) {
                throw new JwtTokenExpiredException();
            } catch (SignatureException ex) {
                throw new WrongJwtTokenSignException();
            }

        }
        return null;
    }

    private void createAuthentication(String username, String jwtToken) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = createAuthToken(username, jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private UsernamePasswordAuthenticationToken createAuthToken(String username, String jwtToken) {
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                jwtTokenUtils.getRolesFromToken(jwtToken).stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }
}
