package pl.company.application.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.company.application.person.command.AddPersonCommand;
import pl.company.application.person.command.UpdatePersonCommand;
import pl.company.domain.person.Person;
import pl.company.domain.person.mapper.CommandToPersonMapper;
import pl.company.infrastructure.database.sql.PersonRepository;
import pl.company.infrastructure.error.common.NotFoundException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final CommandToPersonMapper mapper;

    public List<Person> getPeople() {
        log.info("Get request for all people.");
        return personRepository.findAll();
    }

    public Person getPerson(Long id) {
        log.info("Get request for person with id {}.", id);
        return personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Person was not found."));
    }

    public Person addPerson(AddPersonCommand command) {
        log.info("Post request to add person {}.", command);
        Person personToAdd = mapper.map(command);
        return personRepository.save(personToAdd);
    }

    public Person updatePerson(UpdatePersonCommand command) {
        log.info("Put request to update person using following data: {}.", command);

        if (!personRepository.existsById(command.getId())) {
            throw new NotFoundException("Person was not found.");
        }

        Person personToUpdate = mapper.map(command);
        return personRepository.save(personToUpdate);
    }

    public void deletePerson(Long id) {
        log.info("Delete request to remove person with id {}.", id);

        if (!personRepository.existsById(id)) {
            throw new NotFoundException("Person was not found.");
        }

        personRepository.deleteById(id);
    }
}
