package pl.company.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.company.model.GuineaPig;
import pl.company.repository.GuineaPigRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/guineapig")
public class GuineaPigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuineaPig.class);

    private final GuineaPigRepository guineaPigRepository;

    public GuineaPigController(GuineaPigRepository guineaPigRepository) {
        this.guineaPigRepository = guineaPigRepository;
    }

    @GetMapping
    public ResponseEntity<List<GuineaPig>> getPiggies() {
        LOGGER.info("Get request for all guinea pigs");
        List<GuineaPig> piggies = guineaPigRepository.findAll();
        if (!piggies.isEmpty()) {
            LOGGER.info("Found {} guinea pigs in repository.", piggies.size());
            return ResponseEntity.ok(piggies);
        } else {
            LOGGER.info("Did not found any guinea pigs in repository.");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuineaPig> getPig(@PathVariable("id") Long id) {
        LOGGER.info("Get request for guinea pig with id {}.", id);
        if (id < 0) {
            LOGGER.debug("Requested id '{}' was negative while performing get one operation.", id);
            return ResponseEntity.badRequest().header("Reason", "Id cannot be negative number").build();
        }
        Optional<GuineaPig> optionalGuineaPig = guineaPigRepository.findById(id);
        if (optionalGuineaPig.isPresent()) {
            LOGGER.info("Guinea pig for id {} is {}", id, optionalGuineaPig.get());
            return ResponseEntity.ok(optionalGuineaPig.get());
        } else {
            LOGGER.info("Could not find guinea pig with id {}.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GuineaPig> addPig(@RequestBody GuineaPig pig) {
        if (pig.getId() != null) {
            return updateGuineaPig(pig.getId(), pig);
        }
        LOGGER.info("Post request to add guinea pig {}.", pig);
        GuineaPig createdPig = guineaPigRepository.save(pig);
        LOGGER.info("Created guinea pig with id {}.", createdPig.getId());
        return ResponseEntity.ok(createdPig);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuineaPig> updateGuineaPig(@PathVariable("id") Long id, @RequestBody GuineaPig pig) {
        if (id == null) {
            return addPig(pig);
        }
        LOGGER.info("Put request to update guinea pig with id {} using following data: {}.", id, pig);
        if (id < 0) {
            LOGGER.debug("Requested id '{}' was negative while performing update operation.", id);
            return ResponseEntity.badRequest().header("Reason", "Id cannot be negative number").build();
        }
        GuineaPig updatedPig = guineaPigRepository.save(pig);
        if (updatedPig.getId().equals(id)) {
            LOGGER.info("Updated guinea pig with id {}.", id);
        } else {
            LOGGER.info("Created guinea pig with id {}.", id);
        }
        return ResponseEntity.ok(updatedPig);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePig(@PathVariable("id") Long id) {
        LOGGER.info("Delete request to remove guinea pig with id {}.", id);
        if (id < 0) {
            LOGGER.debug("Requested id '{}' was negative while performing delete operation.", id);
            return ResponseEntity.badRequest().header("Reason", "Id cannot be negative number").build();
        }
        guineaPigRepository.deleteById(id);
        LOGGER.info("Deleted guinea pig with id {}.", id);
        return ResponseEntity.ok().build();
    }
}