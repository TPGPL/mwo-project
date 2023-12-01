package pl.edu.pw.mwoproj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.mwoproj.models.Publisher;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher,Integer> {
}
