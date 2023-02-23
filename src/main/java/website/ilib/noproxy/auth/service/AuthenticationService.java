package website.ilib.noproxy.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import website.ilib.noproxy.auth.AuthenticationRequest;
import website.ilib.noproxy.auth.AuthenticationResponse;
import website.ilib.noproxy.auth.RegisterRequest;
import website.ilib.noproxy.role.Role;
import website.ilib.noproxy.user.entity.User;
import website.ilib.noproxy.user.repository.UserRepository;
import website.ilib.noproxy.config.service.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	public class UserAlreadyExistsException extends RuntimeException {

		public UserAlreadyExistsException(String message) {
			super(message);
		}

	}

	public AuthenticationResponse register(RegisterRequest request) throws UserAlreadyExistsException{
		// TODO Auto-generated method stub
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists.");
		}
		User user = User.builder()
				.name(request.getName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.STUDENT)
				.build();
		userRepository.save(user);
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder()
				.jwt(jwtToken)
				.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid email/password");
		}

		var user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().jwt(jwtToken).build();
	}
}
