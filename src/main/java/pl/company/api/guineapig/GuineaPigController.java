package pl.company.api.guineapig;

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
import pl.company.application.guineapig.GuineaPigService;
import pl.company.application.guineapig.command.AddGuineaPigCommand;
import pl.company.application.guineapig.command.UpdateGuineaPigCommand;
import pl.company.domain.guineapig.GuineaPig;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/guineapig")
public class GuineaPigController {

    private final GuineaPigService guineaPigService;

    @GetMapping
    public ResponseEntity<List<GuineaPig>> getPiggies() {
        return ResponseEntity.ok(guineaPigService.getPiggies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuineaPig> getPig(@PathVariable("id") Long id) {
        return ResponseEntity.ok(guineaPigService.getPig(id));
    }

    @PostMapping
    public ResponseEntity<GuineaPig> addPig(@RequestBody @Valid AddGuineaPigCommand command) {
        return ResponseEntity.ok(guineaPigService.addPig(command));
    }

    @PutMapping
    public ResponseEntity<GuineaPig> updateGuineaPig(@RequestBody @Valid UpdateGuineaPigCommand command) {
        return ResponseEntity.ok(guineaPigService.updateGuineaPig(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePig(@PathVariable("id") Long id) {
        guineaPigService.deletePig(id);
        return ResponseEntity.ok().build();
    }
}