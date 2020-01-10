package pl.company.domain.person.mapper;

import org.springframework.stereotype.Service;
import pl.company.application.person.command.AddPersonCommand;
import pl.company.application.person.command.PersonCommand;
import pl.company.application.person.command.UpdatePersonCommand;
import pl.company.domain.person.Person;

import java.util.ArrayList;

@Service
public class CommandToPersonMapper {

    public Person map(AddPersonCommand command) {
        return mapCommonFields(command)
                .build();
    }

    public Person map(UpdatePersonCommand command) {
        return mapCommonFields(command)
                .id(command.getId())
                .build();
    }

    private Person.PersonBuilder mapCommonFields(PersonCommand command) {
        return Person.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .age(command.getAge())
                .piggies(new ArrayList<>());
    }
}
