package com.ensaj.examsEnsaj.examsEnsaj.respository;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {

}
