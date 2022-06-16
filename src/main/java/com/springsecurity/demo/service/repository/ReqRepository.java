package com.springsecurity.demo.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springsecurity.demo.model.Donor;
import com.springsecurity.demo.model.Requester;


@Repository("reqRepo")
public interface ReqRepository extends CrudRepository<Requester,String>{
	//Donor findByEmailIgnoreCase(String email);

}
