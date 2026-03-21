package org.university.dto;

import jakarta.validation.constraints.NotBlank;

public class CourseRequest {

    @NotBlank(message = "Course code is required")
    private String code;

    @NotBlank(message = "Course name is required")
    private String name;

    @NotBlank(message = "Professor name is required")
    private String professor;

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getProfessor() { return professor; }

    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name; }
    public void setProfessor(String professor) { this.professor = professor; }
}

