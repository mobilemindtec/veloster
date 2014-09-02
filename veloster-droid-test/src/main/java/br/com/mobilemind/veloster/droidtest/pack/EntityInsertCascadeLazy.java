package br.com.mobilemind.veloster.droidtest.pack;

import br.com.mobilemind.veloster.orm.annotations.Column;
import br.com.mobilemind.veloster.orm.annotations.JoinColumn;
import br.com.mobilemind.veloster.orm.annotations.Table;

/**
 *
 * @author Ricardo Bocchi
 */
@Table
public class EntityInsertCascadeLazy extends EntityImpl {

    @Column
    private String name = "teste";
    @Column
    @JoinColumn( cascadeOnInsert = true)
    private PersonGroup grupo;

    public PersonGroup getGrupo() {
        lazy("grupo");
        return grupo;
    }

    public void setGrupo(PersonGroup grupo) {
        this.grupo = grupo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
