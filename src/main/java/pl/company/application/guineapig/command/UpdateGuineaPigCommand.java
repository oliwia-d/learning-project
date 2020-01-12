package pl.company.application.guineapig.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.company.domain.person.Gender;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateGuineaPigCommand extends GuineaPigCommand {

    @NotNull
    private Long id;

    @Builder
    public UpdateGuineaPigCommand(String name, int age, Gender gender, Long id) {
        super(name, age, gender);
        this.id = id;
    }
}
