package com.ensaj.examsEnsaj.examsEnsaj.entites;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Option;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "module")
@Getter
@Setter
@Data
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_module")
    private int id_module;

    @Column(name = "nom", nullable = false)
    private String nom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_option", referencedColumnName = "id_option")
    private Option option;
    // Getters and Setters
    public int getId_module() {
        return id_module;
    }

    public void setId_module(int id_module) {
        this.id_module = id_module;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id_module=" + id_module +
                ", nom='" + nom + '\'' +
                ", option=" + option +
                '}';
    }
}
