package pl.company.application.guineapig.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateGuineaPigCommand extends GuineaPigCommand {

    @NotNull
    private Long id;
}
