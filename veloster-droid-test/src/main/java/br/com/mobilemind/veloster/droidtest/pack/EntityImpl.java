package br.com.mobilemind.veloster.droidtest.pack;

import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.orm.annotations.Column;
import br.com.mobilemind.veloster.orm.annotations.Id;
import br.com.mobilemind.veloster.orm.model.EntityLazy;

/**
 *
 * @author Ricardo Bocchi
 */
public class EntityImpl extends EntityLazy {

    @Id()
    @Column()
    private Long id;
    private boolean loaded;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EntityImpl && this.id != null) {
            return this.id.equals(((Entity) o).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " {" + this.id + "}";
    }
}
