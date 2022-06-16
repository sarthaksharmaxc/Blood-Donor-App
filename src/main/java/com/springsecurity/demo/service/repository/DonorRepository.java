package com.springsecurity.demo.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springsecurity.demo.model.Donor;


@Repository("donorRepository")
public interface DonorRepository extends CrudRepository<Donor,String>{
	Donor findByEmailIgnoreCase(String email);

}
