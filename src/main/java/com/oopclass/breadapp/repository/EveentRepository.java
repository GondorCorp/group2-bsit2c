package com.oopclass.breadapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oopclass.breadapp.models.Eveent;

@Repository
public interface EveentRepository extends JpaRepository<Eveent, Long> {

	//User findByEmail(String email);
}
