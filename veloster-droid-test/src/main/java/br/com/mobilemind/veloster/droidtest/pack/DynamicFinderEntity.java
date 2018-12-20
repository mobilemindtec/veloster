package br.com.mobilemind.veloster.droidtest.pack;

import br.com.mobilemind.veloster.orm.annotations.Column;
import br.com.mobilemind.veloster.orm.annotations.Table;

/**
 *
 * @author Ricardo Bocchi
 */
@Table(name = "DynamicFinderEntity")
public class DynamicFinderEntity extends EntityImpl {

    @Column(length = 100, nullable = true)
    private String name;
    @Column(length = 100, nullable = true)
    private String campo1;
    @Column(length = 100, nullable = true)
    private String campo2;
    @Column(length = 100, nullable = true)
    private String campo3;

    public DynamicFinderEntity() {
        this("Group One");
    }

    public DynamicFinderEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    public String getCampo3() {
        return campo3;
    }

    public void setCampo3(String campo3) {
        this.campo3 = campo3;
    }

    @Override
    public String toString() {
        return this.getId() + " - " + this.getName();
    }
}
