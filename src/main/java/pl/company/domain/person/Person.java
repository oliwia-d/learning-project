package pl.company.domain.person;

import lombok.Data;
import pl.company.domain.guineapig.GuineaPig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 8473084507801116089L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    // TODO move javax validation to command
    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "name")
    private String name;

    // TODO move javax validation to command
    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "surname")
    private String surname;

    // TODO move javax validation to command
    @NotNull
    @Min(value = 1)
    @Column(name = "age")
    private int age;

    @OneToMany(mappedBy = "person")
    private List<GuineaPig> piggies;
}
