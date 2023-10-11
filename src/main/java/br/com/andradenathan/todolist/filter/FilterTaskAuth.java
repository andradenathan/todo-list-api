package br.com.andradenathan.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.andradenathan.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var authorization = request.getHeader("Authorization");
        var authEncoded = authorization.split(" ")[1].trim();

        byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
        var authString = new String(authDecoded);

        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];

        var user = this.userRepository.findByUsername(username);
        if(user == null) {
            response.sendError(401, "Sem autorização");
        } else {
            var verifiedPassword = BCrypt.verifyer().verify(password.getBytes(), user.getPassword().getBytes());
            if(verifiedPassword.verified) filterChain.doFilter(request, response);
            response.sendError(401);
        }
    }
}