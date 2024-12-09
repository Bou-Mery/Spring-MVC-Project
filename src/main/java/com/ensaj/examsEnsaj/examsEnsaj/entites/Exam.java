package com.ensaj.examsEnsaj.examsEnsaj.entites;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Setter
@Getter
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_examen")
    private String dateExamen;

    @Column(name = "heure_examen")
    private String heureExamen;

    @Column(name = "module")
    private String module;

    @Column(name = "option")
    private String option;

    @Column(name = "responsable_module")
    private String responsableModule;

    @Column(name = "nombre_etudiants")
    private int nombreEtudiants;
    @ManyToOne
    @JoinColumn(name = "id_session")
    private Session session;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Local> locals;

    // Méthode pour définir les locaux d'examen
    public void setLocaux(List<Local> locaux) {
        this.locals = locaux;
    }
}