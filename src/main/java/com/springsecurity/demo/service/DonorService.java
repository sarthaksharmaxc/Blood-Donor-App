package com.springsecurity.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.springsecurity.demo.model.Donor;
import com.springsecurity.demo.service.repository.DonorRepository;


public interface DonorService {

	public static final DonorRepository donorRepository = null;
	 public static List<Donor> listAll() {
	        List<Donor> donors = new ArrayList<>();
	        donorRepository.findAll().forEach(donors::add); //fun with Java 8
	        return donors;
	    }

}
