package com.team12.finalproject.configuration;

import com.team12.finalproject.domain.User;
import com.team12.finalproject.domain.UserRole;
import com.team12.finalproject.exception.AppException;
import com.team12.finalproject.exception.ErrorCode;
import com.team12.finalproject.repository.UserRepository;
import com.team12.finalproject.service.UserService;
import com.team12.finalproject.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
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

    private final UserService userService;
    private final UserRepository userRepository;
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
        try {
            token = authorizationHeader.split(" ")[1];
        } catch (Exception e) {
            filterChain.doFilter(request,response);
            throw new AppException(ErrorCode.INVALID_TOKEN,"토큰추출에 실패하였습니다");
        }

        //token이 expired(만료)되었는지 여부
        if(JwtTokenUtils.isExpired(token,secretKey)) {
            filterChain.doFilter(request,response);
            throw new AppException(ErrorCode.INVALID_TOKEN,"토큰이 만료되었습니다");
        }


        //userName 꺼내기
        String userName = JwtTokenUtils.getUserNAme(token,secretKey);

        //Role확인
        User user = userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND,"유저를 찾을 수 없습니다")
        );
        String userRole = String.format("ROLE_%S",String.valueOf(user.getRole()));

        //인증 완료
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority(userRole))
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
