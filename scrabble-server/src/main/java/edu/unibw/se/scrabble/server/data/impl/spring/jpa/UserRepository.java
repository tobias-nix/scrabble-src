package edu.unibw.se.scrabble.server.data.impl.spring.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Bößendörfer
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    //Mögliche zusätzliche Funktionalität


    @Override
    boolean existsById(String username);

    User findUserByUsername(String username);
}
