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
import br.com.mobilemind.veloster.orm.annotations.Enumerated;
import br.com.mobilemind.veloster.orm.annotations.JoinColumn;
import br.com.mobilemind.veloster.orm.validator.Length;
import br.com.mobilemind.veloster.orm.validator.NotNull;
import br.com.mobilemind.veloster.orm.validator.Regex;
import br.com.mobilemind.veloster.orm.annotations.Table;
import java.util.Date;

/**
 *
 * @author Ricardo Bocchi
 */
@Table
public class Person extends EntityImpl {

    @Column
    @Length
    @Regex(value = "^[a-z]{1,50}$")
    private String name;
    @Column
    @br.com.mobilemind.veloster.orm.validator.DateValidation
    private Date birthDay;
    @Column()
    @Enumerated
    private PersonType personType;
    @NotNull
    @Column(name = "group_id")
    @JoinColumn    
    private PersonGroup group;
    @br.com.mobilemind.veloster.orm.validator.NumberValidation(minInt = 0, maxInt = 100)
    private int age;
    @br.com.mobilemind.veloster.orm.validator.NumberValidation(minDouble = 0.0, maxDouble = 99.0)
    private double doubleValue;
    @br.com.mobilemind.veloster.orm.validator.NumberValidation(minLong = 0L, maxLong = 100L)
    private long longValue;
    private String fieldNotAnnoted;

    public Person() {
        this("john", new Date());
    }

    public Person(String name, Date birthDay) {
        this.name = name;
        this.birthDay = birthDay;
        this.personType = PersonType.PERSON_1;
        this.group = new PersonGroup();
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public PersonGroup getGroup() {
        return group;
    }

    public void setGroup(PersonGroup group) {
        this.group = group;
    }

    public String getFieldNotAnnoted() {
        return fieldNotAnnoted;
    }

    public void setFieldNotAnnoted(String fieldNotAnnoted) {
        this.fieldNotAnnoted = fieldNotAnnoted;
    }

    @Override
    public String toString() {
        return this.getId() + " - " + this.getName();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }
}
