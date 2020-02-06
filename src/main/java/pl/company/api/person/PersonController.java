package pl.company.api.person;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.company.application.person.PersonService;
import pl.company.application.person.command.AddPersonCommand;
import pl.company.application.person.command.UpdatePersonCommand;
import pl.company.domain.person.Person;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<Person>> getPeople() {
        return ResponseEntity.ok(personService.getPeople());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personService.getPerson(id));
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody @Valid AddPersonCommand command) {
        return ResponseEntity.ok(personService.addPerson(command));
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody @Valid UpdatePersonCommand command) {
        return ResponseEntity.ok(personService.updatePerson(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
