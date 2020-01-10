package pl.company.domain.guineapig.mapper;

import org.springframework.stereotype.Service;
import pl.company.application.guineapig.command.AddGuineaPigCommand;
import pl.company.application.guineapig.command.GuineaPigCommand;
import pl.company.application.guineapig.command.UpdateGuineaPigCommand;
import pl.company.domain.guineapig.GuineaPig;

@Service
public class CommandToGuineaPigMapper {

    public GuineaPig map(AddGuineaPigCommand command) {
        return mapCommonFields(command)
                .build();
    }

    public GuineaPig map(UpdateGuineaPigCommand command) {
        return mapCommonFields(command)
                .id(command.getId())
                .build();
    }

    private GuineaPig.GuineaPigBuilder mapCommonFields(GuineaPigCommand command) {
        return GuineaPig.builder()
                .gender(command.getGender())
                .age(command.getAge())
                .name(command.getName());
    }
}
