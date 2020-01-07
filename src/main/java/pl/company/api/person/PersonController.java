//package pl.company.api.person;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import pl.company.model.Person;
//import pl.company.repository.PersonRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/person")
//public class PersonController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
//
//    private final PersonRepository personRepository;
//
//    public PersonController(PersonRepository personRepository) {
//        this.personRepository = personRepository;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Person>> getPeople() {
//        LOGGER.info("Get request for all people");
//        List<Person> people = personRepository.findAll();
//        if (!people.isEmpty()) {
//            LOGGER.info("Found {} people in repository.", people.size());
//            return ResponseEntity.ok(people);
//        } else {
//            LOGGER.info("Did not found any people in repository.");
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Person> getPerson(@PathVariable("id") Long id) {
//        LOGGER.info("Get request for person with id {}.", id);
//        if (id < 0) {
//            LOGGER.debug("Requested id '{}' was negative while performing get one operation.", id);
//            return ResponseEntity.badRequest().header("Reason", "Id cannot be negative number").build();
//        }
//        Optional<Person> optionalPerson = personRepository.findById(id);
//        if (optionalPerson.isPresent()) {
//            LOGGER.info("Person for id {} is {}", id, optionalPerson.get());
//            return ResponseEntity.ok(optionalPerson.get());
//        } else {
//            LOGGER.info("Could not find person with id {}.", id);
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
//        if (person.getId() != null) {
//            return updatePerson(person.getId(), person);
//        }
//        LOGGER.info("Post request to add person {}.", person);
//        Person createdPerson = personRepository.save(person);
//        LOGGER.info("Created person with id {}.", createdPerson.getId());
//        return ResponseEntity.ok(createdPerson);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Person> updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
//        if (id == null) {
//            return addPerson(person);
//        }
//        LOGGER.info("Put request to update person with id {} using following data: {}.", id, person);
//        if (id < 0) {
//            LOGGER.debug("Requested id '{}' was negative while performing update operation.", id);
//            return ResponseEntity.badRequest().header("Reason", "Id cannot be negative number").build();
//        }
//        Person updatedPerson = personRepository.save(person);
//        if (updatedPerson.getId().equals(id)) {
//            LOGGER.info("Updated person with id {}.", id);
//        } else {
//            LOGGER.info("Created person with id {}.", id);
//        }
//        return ResponseEntity.ok(updatedPerson);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePerson(@PathVariable("id") Long id) {
//        LOGGER.info("Delete request to remove person with id {}.", id);
//        if (id < 0) {
//            LOGGER.debug("Requested id '{}' was negative while performing delete operation.", id);
//            return ResponseEntity.badRequest().header("Reason", "Id cannot be negative number").build();
//        }
//        personRepository.deleteById(id);
//        LOGGER.info("Deleted person with id {}.", id);
//        return ResponseEntity.ok().build();
//    }
//}
