package pl.company.application.person.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPersonCommand extends PersonCommand {

    @Builder
    public AddPersonCommand(String name, String surname, Integer age) {
        super(name, surname, age);
    }
}
