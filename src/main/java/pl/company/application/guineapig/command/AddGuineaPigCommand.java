package pl.company.application.guineapig.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.company.domain.person.Gender;

@Getter
@Setter
public class AddGuineaPigCommand extends GuineaPigCommand {

    @Builder
    public AddGuineaPigCommand(String name, Integer age, Gender gender) {
        super(name, age, gender);
    }
}
