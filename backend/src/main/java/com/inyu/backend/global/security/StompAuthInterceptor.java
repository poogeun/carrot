package com.inyu.backend.global.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompAuthInterceptor implements ChannelInterceptor {

  private final JwtProvider jwtProvider;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String header = accessor.getFirstNativeHeader("Authorization");
      String token = (header != null && header.startsWith("Bearer ")) ?
          header.substring(7) : null;

      if (token == null || !jwtProvider.validateToken(token)) {
        throw new BadCredentialsException("유효하지 않은 토큰입니다.");
      }

      String email = jwtProvider.getEmail(token);
      String role = jwtProvider.getRole(token);
      List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
      Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);

      accessor.setUser(authentication);
    }

    return message;
  }
}
