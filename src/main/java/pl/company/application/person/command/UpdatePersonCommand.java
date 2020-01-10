package pl.company.application.person.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePersonCommand extends PersonCommand {

    @NotNull
    private long id;
}
