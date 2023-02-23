package website.ilib.noproxy.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.ilib.noproxy.teacher.service.TeacherService;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/teacher")
@PreAuthorize("hasRole('TEACHER')")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping("/makeTeacher")
    public ResponseEntity<String> makeTeacher(@RequestBody Map<String, String> request){
        return teacherService.makeTeacher(request);
    }
}
