package com.hoangvn.home24h.configurations.sercurity;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hoangvn.home24h.configurations.UserPrincipal;
import com.hoangvn.home24h.models.token.Token;
import com.hoangvn.home24h.services.ITokenService;

public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ITokenService verificationTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        final String authorizationHeader = request.getHeader("Authorization");
        UserPrincipal user = null;
        Token token = null;

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Token ")) {
            String jwtToken = authorizationHeader.substring(6);
            user = jwtUtil.getUserFromToken(jwtToken);
            token = verificationTokenService.findByToken(jwtToken);
        }

        if (null != user && null != token && token.getExpireDate().after(new Date())) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            user.getAuthorities().forEach(role -> authorities.add(new SimpleGrantedAuthority((String) role)));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
                    null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
