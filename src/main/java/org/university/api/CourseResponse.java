package org.university.api;

import io.swagger.v3.oas.annotations.media.Schema;
import org.university.model.Course;

@Schema(description = "Course response payload")
public class CourseResponse {

    private Long id;
    private String code;
    private String name;
    private String professor;

    public CourseResponse() {
    }

    public CourseResponse(Long id, String code, String name, String professor) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.professor = professor;
    }

    public static CourseResponse fromEntity(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getCode(),
                course.getName(),
                course.getProfessor()
        );
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getProfessor() {
        return professor;
    }
}

