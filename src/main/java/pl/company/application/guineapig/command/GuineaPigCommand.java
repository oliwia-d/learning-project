package pl.company.application.guineapig.command;

import lombok.Getter;
import lombok.Setter;
import pl.company.domain.person.Gender;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public abstract class GuineaPigCommand {

    @NotNull
    @Size(min = 2, max = 40)
    private String name;

    private int age;

    private Gender gender;
}
