package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.droidtest.pack.Person;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.exceptions.EntityValidatorException;
import br.com.mobilemind.veloster.exceptions.FailProcessExcetion;
import br.com.mobilemind.veloster.orm.EntityValidator;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ricardo Bocchi
 */
public class ValidationTestCase extends BaseTestCase {

    public void testValidationNameNullPersonGroup() throws EntityValidatorException, FailProcessExcetion {
        String message = null;
        EntityValidator<PersonGroup> validator = VelosterRepository.getVelosterControll(PersonGroup.class).getEntityValidator();
        PersonGroup group = new PersonGroup();
        group.setName(null);
        try {
            validator.validate(group);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.isTrue("length range should be between 5 and 10 for field name".equals(message.trim()));
    }

    public void testValidationNameLengthPersonGroup() throws EntityValidatorException, FailProcessExcetion {
        String message = null;
        EntityValidator<PersonGroup> validator = VelosterRepository.getVelosterControll(PersonGroup.class).getEntityValidator();
        PersonGroup group = new PersonGroup();
        group.setName("aa");
        try {
            validator.validate(group);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.isTrue("length range should be between 5 and 10 for field name".equals(message));

        group.setName("aaaaaaaaaaaaaaaaa");
        try {
            validator.validate(group);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.isTrue("length range should be between 5 and 10 for field name".equals(message));
    }

    public void testValidationRegexPerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVelosterControll(Person.class).getEntityValidator();
        Person person = new Person();
        person.setBirthDay(new SimpleDateFormat("dd/MM/yyyy").parse("27/11/1986"));
        person.setName("123");
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.isTrue("invalid value. required format ^[a-z]{1,50}$ for field name".equals(message));
    }

    public void testValidationDatePerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVelosterControll(Person.class).getEntityValidator();
        Person person = new Person();
        person.setBirthDay(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2501"));
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.isTrue("date range should be between 01/01/1900 and 01/01/2500 for field birthDay".equals(message));
    }

    public void testValidationGroupNullPerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVelosterControll(Person.class).getEntityValidator();
        Person person = new Person();
        person.setGroup(null);
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.isTrue("value cannot be null for field group".equals(message));
    }

    public void testValidationNumberIntegerPerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVelosterControll(Person.class).getEntityValidator();
        Person person = new Person();
        person.setAge(500);
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.isTrue("number range should be between 0 and 100 for field age".equals(message));
    }

    public void testValidationNumberDoublePerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVelosterControll(Person.class).getEntityValidator();
        Person person = new Person();
        person.setDoubleValue(500.0);
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.isTrue("number range should be between 0.0 and 99.0 for field doubleValue".equals(message));
    }

    public void testValidationNumberLongPerson() throws EntityValidatorException, FailProcessExcetion, ParseException {
        String message = null;
        EntityValidator<Person> validator = VelosterRepository.getVelosterControll(Person.class).getEntityValidator();
        Person person = new Person();
        person.setLongValue(500);
        try {
            validator.validate(person);
        } catch (EntityValidatorException e) {
            message = e.getMessage();
            say(message);
        }
        Assert.isTrue("number range should be between 0 and 100 for field longValue".equals(message));
    }
}
