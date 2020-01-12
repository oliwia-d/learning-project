package pl.company.application.guineapig;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.company.application.guineapig.command.AddGuineaPigCommand;
import pl.company.application.guineapig.command.UpdateGuineaPigCommand;
import pl.company.domain.guineapig.GuineaPig;
import pl.company.domain.guineapig.mapper.CommandToGuineaPigMapper;
import pl.company.domain.person.Gender;
import pl.company.domain.person.Person;
import pl.company.infrastructure.database.sql.GuineaPigRepository;
import pl.company.infrastructure.error.common.NotFoundException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GuineaPigServiceTest {

    private GuineaPigService guineaPigService;

    @Mock
    private GuineaPigRepository guineaPigRepository;

    @Mock
    private CommandToGuineaPigMapper mapper;

    @BeforeEach
    void setUp() {
        guineaPigService = new GuineaPigService(guineaPigRepository, mapper);
    }

    @Test
    void itShouldGetAllPiggies() {
        Person owner = Person.builder()
                .id(1L)
                .name("Jan")
                .surname("Kowalski")
                .age(34)
                .build();

        GuineaPig pig1 = GuineaPig.builder()
                .id(1L)
                .name("Akame")
                .age(4)
                .gender(Gender.FEMALE)
                .person(owner)
                .build();

        GuineaPig pig2 = GuineaPig.builder()
                .id(2L)
                .name("Freyia")
                .age(4)
                .gender(Gender.FEMALE)
                .person(owner)
                .build();

        List<GuineaPig> piggies = Lists.list(pig1, pig2);
        Mockito.when(guineaPigRepository.findAll()).thenReturn(piggies);
        Assertions.assertEquals(piggies, guineaPigService.getPiggies());
    }

    @Test
    void itShouldFindGuineaPigGivenCorrectId() {
        long guineaPigId = 1L;

        GuineaPig pig1 = GuineaPig.builder()
                .id(guineaPigId)
                .name("Akame")
                .age(4)
                .gender(Gender.FEMALE)
                .build();

        Mockito.when(guineaPigRepository.findById(guineaPigId)).thenReturn(Optional.of(pig1));
        Assertions.assertEquals(pig1, guineaPigService.getPig(guineaPigId));
    }

    @Test
    void itShouldThrowNotFoundExceptionGivenNonExistingId() {
        long guineaPigId = 1L;

        Mockito.when(guineaPigRepository.findById(guineaPigId)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> guineaPigService.getPig(guineaPigId));
    }

    @Test
    void itShouldAddGuineaPigGivenCorrectData() {
        String name = "Akame";
        int age = 4;
        Gender gender = Gender.FEMALE;

        GuineaPig pig1 = GuineaPig.builder()
                .id(1L)
                .name(name)
                .age(age)
                .gender(gender)
                .build();

        AddGuineaPigCommand command = AddGuineaPigCommand.builder()
                .name(name)
                .age(age)
                .gender(gender)
                .build();

        Mockito.when(guineaPigRepository.save(pig1)).thenReturn(pig1);
        Mockito.when(mapper.map(command)).thenReturn(pig1);
        Assertions.assertEquals(pig1, guineaPigService.addPig(command));
    }


    @Test
    public void itShouldUpdateGuineaPigGivenCorrectIdAndData() {
        Long id = 19L;
        String name = "Akame";
        int age = 4;
        Gender gender = Gender.FEMALE;

        GuineaPig pig1 = GuineaPig.builder()
                .id(id)
                .name(name)
                .age(age)
                .gender(gender)
                .build();

        UpdateGuineaPigCommand command = UpdateGuineaPigCommand.builder()
                .id(id)
                .name(name)
                .age(age)
                .gender(gender)
                .build();

        Mockito.when(guineaPigRepository.existsById(command.getId())).thenReturn(true);
        Mockito.when(mapper.map(command)).thenReturn(pig1);
        Mockito.when(guineaPigRepository.save(pig1)).thenReturn(pig1);
        Assertions.assertEquals(guineaPigService.updateGuineaPig(command), pig1);

    }


}
