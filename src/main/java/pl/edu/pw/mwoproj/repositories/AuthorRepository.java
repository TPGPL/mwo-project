package pl.edu.pw.mwoproj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.mwoproj.models.Author;


@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
    boolean existsByEmailAndIdNot(String email, int id);
    boolean existsByEmail(String email);
}
