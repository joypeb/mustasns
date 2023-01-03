package com.team12.finalproject.configuration;

import com.team12.finalproject.domain.entity.User;
import com.team12.finalproject.service.VerificationService;
import com.team12.finalproject.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 토큰 꺼내기
        String servletPath = request.getServletPath();
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        //토큰이 없는 경우 리턴
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        //token 꺼내기 Bearer를 제거해준다
        String token = "";
        Claims claims = null;
        try {
            token = authorizationHeader.split(" ")[1];
            claims = JwtTokenUtils.extractClaims(token,secretKey);
        } catch (JwtException e) {
            filterChain.doFilter(request,response);
        }

        //token이 expired(만료)되었는지 여부
        if(JwtTokenUtils.isExpired(claims,secretKey)) {
            filterChain.doFilter(request,response);
        }

        //userName 꺼내기
        String userName = claims.get("userName").toString();

        //Role확인
        String userRole = String.format("ROLE_%s",claims.get("role").toString());

        //인증 완료
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(new User(claims), null, List.of(new SimpleGrantedAuthority(userRole))
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
