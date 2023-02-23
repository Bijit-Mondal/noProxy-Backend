package website.ilib.noproxy.user.repository;

import website.ilib.noproxy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public boolean existsByEmail(String email);

    public Optional<User> findByEmail(String email);

//    @Query("select max(s.id) from Student s")
//    public Integer findMaxId();
}
