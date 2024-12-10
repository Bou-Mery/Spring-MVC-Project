package com.ensaj.examsEnsaj.examsEnsaj.respository;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    boolean existsByName(String name);
}