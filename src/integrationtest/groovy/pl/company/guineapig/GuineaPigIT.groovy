package pl.company.guineapig

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.company.LearningProjectApplication
import pl.company.application.guineapig.command.AddGuineaPigCommand
import pl.company.application.guineapig.command.UpdateGuineaPigCommand
import pl.company.domain.guineapig.GuineaPig
import pl.company.domain.person.Gender
import pl.company.infrastructure.database.sql.GuineaPigRepository
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
class GuineaPigIT extends Specification {

    @Autowired
    private GuineaPigRepository guineaPigRepository

    @Autowired
    private MockMvc mvc

    void setup() {
        guineaPigRepository.deleteAll()
    }

    def "it should read all guinea pigs"() {
        given: "guinea pigs exist in database"
        def guineapig1 = new GuineaPig(name: "Ayaka", age: 12, gender: Gender.FEMALE)
        def guineapig2 = new GuineaPig(name: "Akame", age: 3, gender: Gender.FEMALE)
        def savedGuineaPig1 = guineaPigRepository.save(guineapig1)
        def savedGuineaPig2 = guineaPigRepository.save(guineapig2)

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pigs are received"
        mvc.perform(get("/api/guinea-pig"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.[0]").value(savedGuineaPig1))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.[1]").value(savedGuineaPig2))

        and: "no elements were added"
        def pigs = guineaPigRepository.findAll()
        pigs.size() == sizeBefore

        and: "no elements were changed"
        pigs.containsAll(savedGuineaPig1, savedGuineaPig2)
    }

    def "it should read wanted guinea pig given correct id"() {
        given: "guinea pig exists in database"
        def guineapig = guineaPigRepository.save(new GuineaPig(name: "Ayaka", age: 12, gender: Gender.FEMALE))

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pig is received"
        mvc.perform(get("/api/guinea-pig/" + guineapig.id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(guineapig.id))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(guineapig.name))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.age").value(guineapig.age))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.gender").value(guineapig.gender.toString()))

        and: "no guinea pigs were added"
        def pigs = guineaPigRepository.findAll()
        pigs.size() == sizeBefore

        and: "guinea pig was not changed"
        pigs.contains(guineapig)
    }

    def "it should not read wanted guinea pig given not existing id"() {
        given: "id number not existing in database"
        def id = 19

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pig with not existing id was not found in database"
        mvc.perform(get("/api/guinea-pig/" + id))
                .andExpect(status().isNotFound())

        and: "size of database has not changed"
        guineaPigRepository.findAll().size() == sizeBefore
    }

    @WithMockUser(roles = "USER")
    def "it should add guinea pig given correct data"() {
        given: "command of guinea pig to add"
        def command = new AddGuineaPigCommand("Ayaka", 12, Gender.FEMALE)

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pig is sent"
        mvc.perform(post("/api/guinea-pig")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(command.name))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.age").value(command.age))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.gender").value(command.gender.toString()))

        and: "size of database is 1 bigger"
        def pigs = guineaPigRepository.findAll()
        pigs.size() == sizeBefore + 1

        and: "the exact same guinea pig is saved in database"
        pigs.find {
            it.name == command.name
            it.age == command.age
            it.gender == command.gender
        } != null
    }

    @WithMockUser(roles = "USER")
    def "it should not add guinea pig given invalid data"() {
        given: "command of guinea pig to add"
        def command = new AddGuineaPigCommand(name, age, gender)

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pig without sent name is not sent due to bad request"
        mvc.perform(post("/api/guinea-pig")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

        and: "size of database has not changed"
        guineaPigRepository.findAll().size() == sizeBefore

        where: "values to test"
        name                                         | age                   | gender
        "A"                                          | 1                     | Gender.FEMALE
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" | 1                     | Gender.FEMALE
        null                                         | 1                     | Gender.FEMALE
        ""                                           | 1                     | Gender.FEMALE
        "Valid"                                      | -1                    | Gender.FEMALE
        "Valid"                                      | 0                     | Gender.FEMALE
        "Valid"                                      | null                  | Gender.FEMALE
        "Valid"                                      | Integer.MAX_VALUE + 1 | Gender.FEMALE
        "Valid"                                      | 1                     | null
    }

    @WithMockUser(roles = "USER")
    def "it should update guinea pig given existing id"() {
        given: "guinea pig exists in database"
        def guineapig = guineaPigRepository.save(new GuineaPig(name: "Ayaka", age: 12, gender: Gender.FEMALE))

        and: "command of guinea pig to update"
        def command = new UpdateGuineaPigCommand("Akame", 2, Gender.MALE, guineapig.id)

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pig is sent"
        mvc.perform(put("/api/guinea-pig")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(command.name))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.age").value(command.age))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.gender").value(command.gender.toString()))

        and: "size of database has not changed"
        def pigs = guineaPigRepository.findAll()
        pigs.size() == sizeBefore

        and: "guinea pig which has existed in database is replaced with new guinea pig"
        pigs.find {
            it.name == command.name
            it.gender == command.gender
            it.age == command.age
        } != null
    }

    @WithMockUser(roles = "USER")
    def "it should not update guinea pig given not existing id"() {
        given: "command of guinea pig with non existing id number to update"
        def nonExistingId = 4L
        def command = new UpdateGuineaPigCommand("Akame", 2, Gender.MALE, nonExistingId)

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pig to replace is not found"
        mvc.perform(put("/api/guinea-pig")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())

        and: "size of database has not changed"
        guineaPigRepository.findAll().size() == sizeBefore
    }

    @WithMockUser(roles = "USER")
    def "it should not update guinea pig given wrong data"() {
        given: "guinea pig exists in database"
        def guineapig = guineaPigRepository.save(new GuineaPig(name: "Ayaka", age: 12, gender: Gender.FEMALE))

        and: "command of guinea pig to update"
        def command = new UpdateGuineaPigCommand(name, age, gender, guineapig.id)

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pig is not sent due to bad request"
        mvc.perform(put("/api/guinea-pig")
                .content(TestUtil.asJsonString(command))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

        and: "size of database has not changed"
        guineaPigRepository.findAll().size() == sizeBefore

        where: "values to test"
        name                                         | age                   | gender
        "A"                                          | 1                     | Gender.FEMALE
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" | 1                     | Gender.FEMALE
        null                                         | 1                     | Gender.FEMALE
        ""                                           | 1                     | Gender.FEMALE
        "Valid"                                      | -1                    | Gender.FEMALE
        "Valid"                                      | 0                     | Gender.FEMALE
        "Valid"                                      | null                  | Gender.FEMALE
        "Valid"                                      | Integer.MAX_VALUE + 1 | Gender.FEMALE
        "Valid"                                      | 1                     | null
    }

    @WithMockUser(roles = "USER")
    def "it should delete guinea pig given existing id"() {
        given: "guinea pig exists in database"
        def guineapig = guineaPigRepository.save(new GuineaPig(id: 10L, name: "Ayaka", age: 12, gender: Gender.FEMALE))

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pig is removed from database"
        mvc.perform(delete("/api/guinea-pig/" + guineapig.id))
                .andExpect(status().isNoContent())

        and: "size of database is 1 smaller"
        guineaPigRepository.findAll().size() == sizeBefore - 1
    }

    @WithMockUser(roles = "USER")
    def "it should throw NotFoundException while making delete request with given not existing id"() {
        given: "non existing id number"
        def nonExistingId = 1L

        and: "size of database before operation"
        def sizeBefore = guineaPigRepository.findAll().size()

        expect: "guinea pig with non existing id number is not found"
        mvc.perform(delete("/api/guinea-pig/" + nonExistingId))
                .andExpect(status().isNotFound())

        and: "size of database has not changed"
        guineaPigRepository.findAll().size() == sizeBefore
    }

    void cleanup() {
        guineaPigRepository.deleteAll()
    }
}
