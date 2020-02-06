package pl.company.application.person.command;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdatePersonCommand extends PersonCommand {

    @NotNull
    private Long id;

    public UpdatePersonCommand(String name, String surname, Integer age, Long id) {
        super(name, surname, age);
        this.id = id;
    }
}
