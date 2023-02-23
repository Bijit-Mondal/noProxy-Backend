package website.ilib.noproxy.teacher.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import website.ilib.noproxy.role.Role;
import website.ilib.noproxy.user.entity.User;
import website.ilib.noproxy.user.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class TeacherService {
    private final UserRepository userRepository;

    public TeacherService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ResponseEntity<String> makeTeacher(Map<String, String> request) {
        String email = request.get("email");
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(Role.TEACHER);
            userRepository.save(user);
            return ResponseEntity.ok(email + " is now a teacher.");
        } else {
            return ResponseEntity.badRequest().body("User with email " + email + " not found.");
        }
    }
}
