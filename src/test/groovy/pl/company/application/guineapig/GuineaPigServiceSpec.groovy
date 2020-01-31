package pl.company.application.guineapig

import pl.company.application.guineapig.command.AddGuineaPigCommand
import pl.company.application.guineapig.command.UpdateGuineaPigCommand
import pl.company.domain.guineapig.GuineaPig
import pl.company.domain.guineapig.mapper.CommandToGuineaPigMapper
import pl.company.domain.person.Gender
import pl.company.infrastructure.database.sql.GuineaPigRepository
import pl.company.infrastructure.error.common.NotFoundException
import spock.lang.Specification
import spock.lang.Subject

class GuineaPigServiceSpec extends Specification {

    def repository = Mock(GuineaPigRepository)
    def mapper = Mock(CommandToGuineaPigMapper)

    @Subject
    def guineaPigService = new GuineaPigService(repository, mapper)

    def "it should get all guinea pigs (GET ALL)"() {
        given:
        def name1 = "Ayaka"
        def age1 = 1
        def gender1 = Gender.FEMALE
        def pig1 = new GuineaPig().builder()
                .name(name1)
                .age(age1)
                .gender(gender1)
                .build()

        and:
        def name2 = "Akame"
        def age2 = 1
        def gender2 = Gender.FEMALE
        def pig2 = new GuineaPig().builder()
                .name(name2)
                .age(age2)
                .gender(gender2)
                .build()

        and:
        def piggies = List.of(pig1, pig2)

        and:
        repository.findAll() >> piggies

        when:
        def result = guineaPigService.getPiggies()

        then:
        result.get(0) == pig1
        result.get(1) == pig2
    }

    def "it should get guinea pig given correct id (GET ONE)"() {
        given:
        def id = 1L
        def name = "Ayaka"
        def age = 1
        def gender = Gender.FEMALE
        def pig = new GuineaPig().builder()
                .id(id)
                .name(name)
                .age(age)
                .gender(gender)
                .build()

        and:
        repository.findById(id) >> Optional.of(pig)

        when:
        def result = guineaPigService.getPig(id)

        then:
        result.id == id
        result.name == name
        result.age == age
        result.gender == gender
    }

    def "it should throw NotFoundException given not existing id (GET ONE)"() {
        given:
        def id = 5L

        and:
        repository.findById(id) >> Optional.empty()

        when:
        guineaPigService.getPig(id)

        then:
        thrown(NotFoundException)
    }

    def "it should add guinea pig given correct data (SAVE)"() {
        given:
        def name = "Ayaka"
        def age = 1
        def gender = Gender.FEMALE

        and:
        def command = AddGuineaPigCommand.builder()
                .name(name)
                .age(age)
                .gender(gender)
                .build()
        def guineaPig = new GuineaPig(name: name, age: age, gender: gender)

        and:
        mapper.map(command) >> guineaPig
        repository.save(guineaPig) >> guineaPig

        when:
        def result = guineaPigService.addPig(command)

        then:
        result.name == name
        result.age == age
        result.gender == gender
    }

    def "it should update guinea pig given correct data (UPDATE)"() {
        given:
        def name = "Ayaka"
        def age = 1
        def gender = Gender.FEMALE
        def id = 1L

        and:
        def command = UpdateGuineaPigCommand.builder()
                .name(name)
                .age(age)
                .gender(gender)
                .id(id)
                .build()
        def guineaPig = new GuineaPig(id: id, name: name, age: age, gender: gender)

        and:
        repository.existsById(id) >> true
        mapper.map(command) >> guineaPig
        repository.save(guineaPig) >> guineaPig

        when:
        def result = guineaPigService.updateGuineaPig(command)

        then:
        result.name == name
        result.age == age
        result.gender == gender
        result.id == id
    }

    def "it should throw NotFoundException given not existing id (UPDATE)"() {
        given:
        def id = 1L

        and:
        def command = UpdateGuineaPigCommand.builder()
                .id(id)
                .build()

        and:
        repository.existsById(id) >> false

        when:
        guineaPigService.updateGuineaPig(command)

        then:
        thrown(NotFoundException)
    }

    def "it should delete guinea pig given correct id (DELETE)"() {
        given:
        def id = 1L

        when:
        guineaPigService.deletePig(id)

        then:
        1 * repository.deleteById(id)
    }
}
