package com.ensaj.examsEnsaj.examsEnsaj.entites;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_Option;
    private String name;

    // Getters et setters
    public int getId_Option() {
        return id_Option;
    }

    public void setId_Option(int id_Option) {
        this.id_Option = id_Option;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}