
package com.example.mentalwellnessbackend.repository;

import com.example.mentalwellnessbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByName(String name);

    boolean existsByName(String name);
}
