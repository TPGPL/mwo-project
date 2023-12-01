package pl.edu.pw.mwoproj.services;

import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.mwoproj.models.Publisher;
import pl.edu.pw.mwoproj.repositories.PublisherRepository;
import pl.edu.pw.mwoproj.utils.ServiceResponse;

@Service
public class PublisherService {
    private final PublisherRepository repository;
    private final Validator validator;

    @Autowired
    public PublisherService(PublisherRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Publisher getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Iterable<Publisher> getAll() {
        return repository.findAll();
    }

    public ServiceResponse<Publisher> create(Publisher publisher) {
        var violations = validator.validate(publisher);

        if (!violations.isEmpty()) {
            return ServiceResponse.createInvalidResponse(violations);
        }

        return ServiceResponse.<Publisher>builder()
                .wasSuccessful(true)
                .data(repository.save(publisher))
                .build();
    }

    public ServiceResponse<Publisher> update(int id, Publisher publisher) {
        var publisherToUpdate = getById(id);

        if (publisherToUpdate == null) {
            return ServiceResponse.<Publisher>builder()
                    .message("The author with given ID was not found.")
                    .build();
        }

        publisherToUpdate.setName(publisher.getName());

        var violations = validator.validate(publisherToUpdate);

        if (!violations.isEmpty()) {
            return ServiceResponse.createInvalidResponse(violations);
        }

        return ServiceResponse.<Publisher>builder()
                .wasSuccessful(true)
                .data(repository.save(publisherToUpdate))
                .build();
    }

    public ServiceResponse<Void> delete(int id) {
        var response = ServiceResponse.<Void>builder().wasSuccessful(true);

        if (!repository.existsById(id)) {
            return response.message("No publisher to delete.").build();
        }

        repository.deleteById(id);

        return response.message("The publisher was successfully deleted.").build();
    }
}
