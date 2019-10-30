package pl.company.domain.guineapig;

import lombok.Data;
import pl.company.domain.person.Gender;
import pl.company.domain.person.Person;

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

@Entity
@Data
@Table(name = "guinea_pig")
public class GuineaPig implements Serializable {

    private static final long serialVersionUID = -3372670564868314867L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "guinea_id")
    private Long id;
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
}
