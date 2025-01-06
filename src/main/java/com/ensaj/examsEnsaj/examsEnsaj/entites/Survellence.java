package com.ensaj.examsEnsaj.examsEnsaj.entites;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Survellence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_survellence")
    private int idSurvellence;

    @Column(name = "date_examen", nullable = false)
    private String dateExamen;

    @Column(name = "heure_examen", nullable = false)
    private String heureExamen;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "local_id")
    private Local local;


    private String  enseignant;

    // Getters et Setters
    public int getIdSurvellence() {
        return idSurvellence;
    }

    public void setIdSurvellence(int idSurvellence) {
        this.idSurvellence = idSurvellence;
    }

    public String getDateExamen() {
        return dateExamen;
    }

    public void setDateExamen(String dateExamen) {
        this.dateExamen = dateExamen;
    }

    public String getHeureExamen() {
        return heureExamen;
    }

    public void setHeureExamen(String heureExamen) {
        this.heureExamen = heureExamen;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public String  getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(String enseignant) {
        this.enseignant = enseignant;
    }

    // Equals & HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survellence that = (Survellence) o;
        return idSurvellence == that.idSurvellence &&
                Objects.equals(dateExamen, that.dateExamen) &&
                Objects.equals(heureExamen, that.heureExamen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSurvellence, dateExamen, heureExamen);
    }
}
