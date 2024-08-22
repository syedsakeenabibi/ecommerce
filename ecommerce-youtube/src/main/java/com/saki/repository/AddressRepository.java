package com.saki.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saki.model.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {

}
