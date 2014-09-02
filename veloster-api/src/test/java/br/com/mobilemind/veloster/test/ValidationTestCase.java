package br.com.mobilemind.veloster.test;

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

import br.com.mobilemind.veloster.exceptions.EntityValidatorException;
import br.com.mobilemind.veloster.exceptions.FailProcessExcetion;
import br.com.mobilemind.veloster.orm.EntityValidator;
import br.com.mobilemind.veloster.teste.pack.EntityValidation;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import junit.framework.Assert;
import org.junit.Test;
import br.com.mobilemind.veloster.teste.pack.Person;
import br.com.mobilemind.veloster.teste.pack.PersonGroup;

/**
 *
 * @author Ricardo Bocchi
 */
public class ValidationTestCase extends BaseTestCase {

    @Test
    public void testValidationNameNullPersonGroup() throws EntityValidatorException, FailProcessExcetion {
        String message = null;
        EntityValidator<PersonGroup> validator = VelosterRepository.getVeloster(PersonGroup.class).getValidator();
        PersonGroup group = new PersonGroup();
        group.setName(null);
        try {
            validator.validate(group);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.assertTrue("value cannot be null for field name".equals(message));
    }

    @Test
    public void testValidationNameLengthPersonGroup() throws EntityValidatorException, FailProcessExcetion {
        String message = null;
        EntityValidator<PersonGroup> validator = VelosterRepository.getVeloster(PersonGroup.class).getValidator();
        PersonGroup group = new PersonGroup();
        group.setName("aa");
        try {
            validator.validate(group);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.assertTrue("length range should be between 5 and 10 for field name".equals(message));

        group.setName("aaaaaaaaaaaaaaaaa");
        try {
            validator.validate(group);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.assertTrue("length range should be between 5 and 10 for field name".equals(message));
    }

    @Test
    public void testValidationRegexPerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVeloster(Person.class).getValidator();
        Person person = new Person();
        person.setBirthDay(new SimpleDateFormat("dd/MM/yyyy").parse("27/11/1986"));
        person.setName("123");
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.assertTrue("invalid value. required format ^[a-z]{1,50}$ for field name".equals(message));
    }

    @Test
    public void testValidationDatePerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVeloster(Person.class).getValidator();
        Person person = new Person();
        person.setBirthDay(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2501"));
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.assertTrue("date range should be between 01/01/1900 and 01/01/2500 for field birthDay".equals(message));
    }

    @Test
    public void testValidationGroupNullPerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVeloster(Person.class).getValidator();
        Person person = new Person();
        person.setGroup(null);
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.assertTrue("value cannot be null for field group".equals(message));
    }

    @Test
    public void testValidationNumberIntegerPerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVeloster(Person.class).getValidator();
        Person person = new Person();
        person.setAge(500);
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.assertTrue("number range should be between 0 and 100 for field age".equals(message));
    }

    @Test
    public void testValidationNumberDoublePerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVeloster(Person.class).getValidator();
        Person person = new Person();
        person.setDoubleValue(500.0);
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.assertTrue("number range should be between 0.0 and 99.0 for field doubleValue".equals(message));
    }

    @Test
    public void testValidationNumberLongPerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVeloster(Person.class).getValidator();
        Person person = new Person();
        person.setLongValue(500);
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.assertTrue("number range should be between 0 and 100 for field longValue".equals(message));
    }

    public void testValidationWithCustonMessageKey() {
        EntityValidator<EntityValidation> validator = VelosterRepository.getVeloster(Person.class).getValidator();

        EntityValidation e = new EntityValidation();
        e.setName("aa");
        String message = null;
        try {
            validator.validate(e);
        } catch (br.com.mobilemind.veloster.exceptions.EntityValidatorException ex) {
            message = ex.getMessage();
        }
        
        Assert.assertEquals("valida ke", message);
    }
}
