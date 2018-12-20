package br.com.mobilemind.veloster.droidtest.pack;

import br.com.mobilemind.veloster.orm.annotations.Column;
import br.com.mobilemind.veloster.orm.validator.Length;
import br.com.mobilemind.veloster.orm.validator.NotNull;
import br.com.mobilemind.veloster.orm.annotations.Table;

/**
 *
 * @author Ricardo Bocchi
 */
@Table(name = "PersonsGroups")
public class PersonGroup extends EntityImpl {

    @Column(length = 100)
    @NotNull
    @Length(max = 10, min = 5)
    private String name;

    public PersonGroup() {
        this("Group One");
    }

    public PersonGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getId() + " - " + this.getName();
    }
}
