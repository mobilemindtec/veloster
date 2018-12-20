package br.com.mobilemind.veloster.teste.pack;

/*
 * #%L
 * Mobile Mind - Veloster API
 * %%
 * Copyright (C) 2012 Mobile Mind Empresa de Tecnologia
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */


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

    @Column(length = 100, nullable = true)
    private String tipo;
    @Column(length = 100, nullable = true)
    private String tipoValor;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }    

    public String getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }    

    @Override
    public String toString() {
        return this.getId() + " - " + this.getName();
    }
}
