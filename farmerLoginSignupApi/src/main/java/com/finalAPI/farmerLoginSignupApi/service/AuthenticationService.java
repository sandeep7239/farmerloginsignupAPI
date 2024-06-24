package com.finalAPI.farmerLoginSignupApi.service;

import com.finalAPI.farmerLoginSignupApi.model.AuthenticationResponse;
import com.finalAPI.farmerLoginSignupApi.model.Token;
import com.finalAPI.farmerLoginSignupApi.model.User;
import com.finalAPI.farmerLoginSignupApi.repository.TokenRepository;
import com.finalAPI.farmerLoginSignupApi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse register(User request){
        User user=new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        user=repository.save(user);
        String jwt=jwtService.generateToken(user);

        //save the generated token
         saveUserToken(jwt, user);


        return new AuthenticationResponse(jwt);
    }



    public  AuthenticationResponse authenticate(User request){
              authenticationManager.authenticate(
                      new UsernamePasswordAuthenticationToken(
                              request.getUsername(),
                              request.getPassword()
                      )
              );
              User user=repository.findByUsername(request.getUsername()).orElseThrow();
              String token=jwtService.generateToken(user);
              revokeAllTokenByUser(user);

               saveUserToken(token,user);
              return new AuthenticationResponse(token);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokenListByUser=tokenRepository.findAllTokenByUser(user.getId());
        if(!validTokenListByUser.isEmpty()){
            validTokenListByUser.forEach(t ->{
                t.setLoggedOut(true);
                    });
        }
        tokenRepository.saveAll(validTokenListByUser);
    }

    private void saveUserToken(String jwt, User user) {
        Token token=new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
