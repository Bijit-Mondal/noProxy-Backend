package website.ilib.noproxy.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import website.ilib.noproxy.auth.AuthenticationRequest;
import website.ilib.noproxy.auth.AuthenticationResponse;
import website.ilib.noproxy.auth.RegisterRequest;
import website.ilib.noproxy.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationService authenticationService;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			AuthenticationResponse response = authenticationService.register(request);
			return ResponseEntity.ok(response);
		} catch (AuthenticationService.UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(
		@RequestBody AuthenticationRequest request
	){
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}
}
