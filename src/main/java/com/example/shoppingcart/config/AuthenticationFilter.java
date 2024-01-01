package com.example.shoppingcart.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.shoppingcart.util.Constants.USER_UUID;


public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String xAuth = request.getHeader("X-Authorization");
        // generally token embedded user info should be here but for simplicity, i will just take user UUID

        ThreadContext.put(USER_UUID, xAuth);

        // We should do authentication and set the user to spring security context but this is out of scope for this assignment

        filterChain.doFilter(request, response);
    }

}
