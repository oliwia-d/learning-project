package pl.company.application.guineapig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.company.application.guineapig.command.AddGuineaPigCommand;
import pl.company.application.guineapig.command.UpdateGuineaPigCommand;
import pl.company.domain.guineapig.GuineaPig;
import pl.company.domain.guineapig.mapper.CommandToGuineaPigMapper;
import pl.company.infrastructure.database.sql.GuineaPigRepository;
import pl.company.infrastructure.error.common.NotFoundException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GuineaPigService {

    private final GuineaPigRepository guineaPigRepository;
    private final CommandToGuineaPigMapper mapper;

    public List<GuineaPig> getPiggies() {
        log.info("Get request for all guinea pigs.");
        return guineaPigRepository.findAll();
    }

    public GuineaPig getPig(Long id) {
        log.info("Get request for guinea pig with id {}.", id);
        return guineaPigRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Guinea pig was not found."));
    }

    public GuineaPig addPig(AddGuineaPigCommand command) {
        log.info("Post request to add guinea pig {}.", command);
        GuineaPig pigToAdd = mapper.map(command);
        return guineaPigRepository.save(pigToAdd);
    }

    public GuineaPig updateGuineaPig(UpdateGuineaPigCommand command) {
        log.info("Put request to update guinea pig using following data: {}.", command);

        if (!guineaPigRepository.existsById(command.getId())) {
            throw new NotFoundException("Guinea pig was not found.");
        }

        GuineaPig updatedPig = mapper.map(command);
        return guineaPigRepository.save(updatedPig);
    }

    public void deletePig(Long id) {
        log.info("Delete request to remove guinea pig with id {}.", id);
        guineaPigRepository.deleteById(id);
    }
}
