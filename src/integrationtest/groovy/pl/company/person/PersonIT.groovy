package pl.company.person

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.company.LearningProjectApplication
import pl.company.application.person.command.AddPersonCommand
import pl.company.application.person.command.UpdatePersonCommand
import pl.company.domain.person.Person
import pl.company.infrastructure.database.sql.PersonRepository
import pl.company.util.TestUtil
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@SpringBootTest(classes = LearningProjectApplication.class)
class PersonIT extends Specification {

    @Autowired
    private PersonRepository personRepository

    @Autowired
    private MockMvc mvc

    void setup() {
        personRepository.deleteAll()
    }

    def "it should read all people"() {
        given: "people exist in database"
        def person1 = personRepository.save(new Person(name: "Kurisu", surname: "Makise", age: 18))
        def person2 = personRepository.save(new Person(name: "Okabe", surname: "Rintaro", age: 21))

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "people are read"
        mvc.perform(get("/api/person"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.[0]").value(person1))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.[1]").value(person2))

        and: "size of database has not changed"
        def people = personRepository.findAll()
        people.size() == sizeBefore

        and: "no elements were changed"
        people.containsAll(person1, person2)
    }

    def "it should read wanted person given correct id"() {
        given: "person exists in database"
        def person = personRepository.save(new Person(id: 3L, name: "Kurisu", surname: "Makise", age: 18))

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "person is read"
        mvc.perform(get("/api/person/" + person.id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(person.id))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(person.name))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.age").value(person.age))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.surname").value(person.surname))

        and: "size of database has not changed"
        def people = personRepository.findAll()
        people.size() == sizeBefore

        and: "no elements were changed"
        people.contains(person)
    }

    def "it should not read wanted person given not existing id"() {
        given: "not existing id number"
        def id = 19

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "person with not existing id number is not found"
        mvc.perform(get("/api/person/" + id))
                .andExpect(status().isNotFound())

        and: "size of database has not changed"
        personRepository.findAll().size() == sizeBefore
    }

    @WithMockUser(roles = "USER")
    def "it should add person given correct data"() {
        given: "command of person to add"
        def command = new AddPersonCommand("Kurisu", "Makise", 18)

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "person is sent"
        mvc.perform(post("/api/person")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(command.name))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.age").value(command.age))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.surname").value(command.surname))

        and: "size of database is 1 bigger"
        def people = personRepository.findAll()
        people.size() == sizeBefore + 1

        and: "the exact same person is saved in database"
        people.find {
            it.name == command.name
            it.surname == command.surname
            it.age == command.age
        } != null
    }

    @WithMockUser(roles = "USER")
    def "it should not add person given invalid data"() {
        given: "command of person to add"
        def command = new AddPersonCommand(name, surname, age)

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "person is not sent due to bad request"
        mvc.perform(post("/api/person")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

        and: "size of database has not changed"
        personRepository.findAll().size() == sizeBefore

        where: "values to test"
        name                                                  | surname                                               | age
        "Valid"                                               | "Valid"                                               | -1
        "Valid"                                               | "Valid"                                               | 0
        "Valid"                                               | "Valid"                                               | null
        "Valid"                                               | "Valid"                                               | Integer.MAX_VALUE + 1
        null                                                  | "Valid"                                               | 1
        "Valid"                                               | null                                                  | 1
        "xx"                                                  | "Valid"                                               | 1
        "Valid"                                               | "xx"                                                  | 1
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" | "Valid"                                               | 1
        "Valid"                                               | "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" | 1
    }

    @WithMockUser(roles = "USER")
    def "it should update person given correct data"() {
        given: "person exists in database"
        def person1 = personRepository.save(new Person(name: "Okabe", surname: "Rintaro", age: 21))

        and: "command of person to update"
        def command = new UpdatePersonCommand("Kurisu", "Makise", 18, person1.id)

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "person is sent"
        mvc.perform(put("/api/person")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(command.name))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.age").value(command.age))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.surname").value(command.surname))

        and: "size of database has not changed"
        def people = personRepository.findAll()
        people.size() == sizeBefore

        and: "person which has existed in database was replaced with new person"
        people.find {
            it.name == command.name
            it.surname == command.surname
            it.age == command.age
        } != null
    }

    @WithMockUser(roles = "USER")
    def "it should not update person given invalid data"() {
        given: "person exists in database"
        def person1 = personRepository.save(new Person(name: "Okabe", surname: "Rintaro", age: 21))

        and: "command of person to update"
        def command = new UpdatePersonCommand(name, surname, age, person1.id)

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "person is not sent due to bad request"
        mvc.perform(put("/api/person")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

        and: "size of database has not changed"
        personRepository.findAll().size() == sizeBefore

        where: "values to test"
        name                                                  | surname                                               | age
        "Valid"                                               | "Valid"                                               | -1
        "Valid"                                               | "Valid"                                               | 0
        "Valid"                                               | "Valid"                                               | null
        "Valid"                                               | "Valid"                                               | Integer.MAX_VALUE + 1
        null                                                  | "Valid"                                               | 1
        "Valid"                                               | null                                                  | 1
        "xx"                                                  | "Valid"                                               | 1
        "Valid"                                               | "xx"                                                  | 1
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" | "Valid"                                               | 1
        "Valid"                                               | "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" | 1
    }

    @WithMockUser(roles = "USER")
    def "it should not update person given not existing id"() {
        given: "command of person with not existing id number to update"
        def command = new UpdatePersonCommand("Kurisu", "Makise", 18, 3L)

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "person with not existing id is not found"
        mvc.perform(put("/api/person")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())

        and: "size of database has not changed"
        personRepository.findAll().size() == sizeBefore
    }

    @WithMockUser(roles = "USER")
    def "it should delete person given existing id"() {
        given: "person exists in database"
        def person = personRepository.save(new Person(id: 3L, name: "Kurisu", surname: "Makise", age: 18))

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "person is removed from database"
        mvc.perform(delete("/api/person/" + person.id))
                .andExpect(status().isNoContent())

        and: "size of database is 1 smaller"
        personRepository.findAll().size() == sizeBefore - 1
    }

    @WithMockUser(roles = "USER")
    def "it should throw NotFoundException while making delete request with given not existing id"() {
        given: "non existing id number"
        def nonExistingId = 1L

        and: "size of database before operation"
        def sizeBefore = personRepository.findAll().size()

        expect: "person with non existing id number is not found"
        mvc.perform(delete("/api/person/" + nonExistingId))
                .andExpect(status().isNotFound())

        and: "size of database has not changed"
        personRepository.findAll().size() == sizeBefore
    }

    void cleanup() {
        personRepository.deleteAll()
    }
}
