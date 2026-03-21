package org.university.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for creating a course")
public class CourseApiRequest {

    @NotBlank(message = "Course code is required")
    @Schema(example = "CS612")
    private String code;

    @NotBlank(message = "Course name is required")
    @Schema(example = "Web Services")
    private String name;

    @NotBlank(message = "Professor name is required")
    @Schema(example = "Prof. X")
    private String professor;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}

