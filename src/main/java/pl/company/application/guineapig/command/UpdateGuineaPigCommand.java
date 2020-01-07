package pl.company.application.guineapig.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.company.domain.person.Gender;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UpdateGuineaPigCommand extends GuineaPigCommand {

    @NotNull
    private Long id;
}
