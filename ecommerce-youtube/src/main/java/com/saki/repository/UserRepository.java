package com.saki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.saki.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
	 Optional<User> findByEmail(String email); // Return Optional<User>
	  //
	
}
