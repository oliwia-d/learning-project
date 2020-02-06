package pl.company.application.guineapig.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.company.domain.person.Gender;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public abstract class GuineaPigCommand {

    @NotNull
    @Size(min = 2, max = 40)
    private String name;

    @NotNull
    @Min(1)
    private Integer age;

    @NotNull
    private Gender gender;
}
