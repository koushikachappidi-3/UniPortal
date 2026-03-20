package org.university.model;

import jakarta.persistence.*;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String professor;

    public Course() {}

    public Course(String code, String name, String professor) {
        this.code = code;
        this.name = name;
        this.professor = professor;
    }

    public Long getId() { return id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }
}


