package pl.company.model;

import pl.company.enumeration.Gender;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "guinea_pig")
public class GuineaPig implements Serializable {

    static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "guinea_id")
    private long id;
    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @Column(name = "gender")
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person person;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuineaPig guineaPig = (GuineaPig) o;
        return id == guineaPig.id &&
                age == guineaPig.age &&
                Objects.equals(name, guineaPig.name) &&
                Objects.equals(gender, guineaPig.gender) &&
                Objects.equals(person, guineaPig.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, gender, person);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
