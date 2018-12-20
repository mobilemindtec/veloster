package br.com.mobilemind.veloster.droidtest.pack;

import br.com.mobilemind.veloster.orm.annotations.Column;
import br.com.mobilemind.veloster.orm.annotations.Table;

/**
 *
 * @author Ricardo Bocchi
 */
@Table()
public class NullebleEntity extends EntityImpl {

    @Column(nullable = true)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
