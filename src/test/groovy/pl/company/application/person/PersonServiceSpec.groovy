package pl.company.application.person

import pl.company.application.person.command.AddPersonCommand
import pl.company.application.person.command.UpdatePersonCommand
import pl.company.domain.person.Person
import pl.company.domain.person.mapper.CommandToPersonMapper
import pl.company.infrastructure.database.sql.PersonRepository
import pl.company.infrastructure.error.common.NotFoundException
import spock.lang.Specification
import spock.lang.Subject

class PersonServiceSpec extends Specification {

    def repository = Mock(PersonRepository)
    def mapper = Mock(CommandToPersonMapper)

    @Subject
    def personService = new PersonService(repository, mapper)

    def "it should get all people (GET ALL)"() {
        given:
        def name1 = "Xyz"
        def surname1 = "Zyx"
        def age1 = 19
        def person1 = new Person().builder()
                .name(name1)
                .surname(surname1)
                .age(age1)
                .build()

        and:
        def name2 = "Vux"
        def surname2 = "Xuv"
        def age2 = 22
        def person2 = new Person().builder()
                .name(name2)
                .surname(surname2)
                .age(age2)
                .build()

        and:
        def people = List.of(person1, person2)

        and:
        repository.findAll() >> people

        when:
        def result = personService.getPeople()

        then:
        result.get(0) == person1
        result.get(1) == person2
    }

    def "it should get person given correct id (GET ONE)"() {
        given:
        def id = 1L
        def name = "Xyz"
        def surname = "Zyx"
        def age = 19
        def person = new Person().builder()
                .id(id)
                .name(name)
                .surname(surname)
                .age(age)
                .build()

        and:
        repository.findById(id) >> Optional.of(person)

        when:
        def result = personService.getPerson(id)

        then:
        result.id == id
        result.name == name
        result.surname == surname
        result.age == age
    }

    def "it should throw NotFoundException given not existing id (GET ONE)"() {
        given:
        def id = 9L

        and:
        repository.findById(id) >> Optional.empty()

        when:
        personService.getPerson(id)

        then:
        thrown(NotFoundException)
    }

    def "it should add person given correct data (SAVE)"() {
        given:
        def name = "Xyz"
        def surname = "Zyx"
        def age = 19
        def person = new Person().builder()
                .name(name)
                .surname(surname)
                .age(age)
                .build()

        and:
        def command = new AddPersonCommand(name, surname, age)

        and:
        mapper.map(command) >> person
        repository.save(person) >> person

        when:
        def result = personService.addPerson(command)

        then:
        result.name == name
        result.surname == surname
        result.age == age
    }

    def "it should update person given correct data (UPDATE)"() {
        given:
        def id = 3L
        def name = "Xyz"
        def surname = "Zyx"
        def age = 19
        def person = new Person().builder()
                .id(id)
                .name(name)
                .surname(surname)
                .age(age)
                .build()

        and:
        def command = new UpdatePersonCommand(name, surname, age, id)

        and:
        repository.existsById(command.getId()) >> true
        mapper.map(command) >> person
        repository.save(person) >> person

        when:
        def result = personService.updatePerson(command)

        then:
        result.id == id
        result.name == name
        result.surname == surname
        result.age == age
    }

    def "it should throw NotFoundException given not existing id (UPDATE)"() {
        given:
        def id = 3L
        def name = "Xyz"
        def surname = "Zyx"
        def age = 19

        and:
        def command = new UpdatePersonCommand(name, surname, age, id)

        and:
        repository.existsById(command.getId()) >> false

        when:
        personService.updatePerson(command)

        then:
        thrown(NotFoundException)
    }

    def "it should delete person given correct id (DELETE)"() {
        given:
        def id = 1L

        and:
        repository.existsById(id) >> true

        when:
        personService.deletePerson(id)

        then:
        1 * repository.deleteById(id)
    }

    def "it should throw NotFoundException given not existing id (DELETE)"() {
        given:
        def id = 1L

        and:
        repository.existsById(id) >> false

        when:
        personService.deletePerson(id)

        then:
        thrown(NotFoundException)
        0 * repository.deleteById(id)
    }
}
