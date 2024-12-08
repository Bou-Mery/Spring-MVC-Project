package com.ensaj.examsEnsaj.examsEnsaj.entites;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Data
@Table(name = "departements")
@Getter
@Setter
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departement")
    private int idDepartement;

    @Column(name = "nom_departement", nullable = false)
    private String nomDepartement;
    // Add this to establish the one-to-many relationship
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    private List<Ensiegnent> enseignants;

}