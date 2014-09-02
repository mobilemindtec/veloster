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

import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.sql.type.Between;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.sql.type.Eq;
import br.com.mobilemind.veloster.sql.type.Ge;
import br.com.mobilemind.veloster.sql.type.Gt;
import br.com.mobilemind.veloster.sql.type.IsNull;
import br.com.mobilemind.veloster.sql.type.Le;
import br.com.mobilemind.veloster.sql.type.Like;
import br.com.mobilemind.veloster.sql.type.Lt;
import br.com.mobilemind.veloster.sql.type.Match;
import br.com.mobilemind.veloster.sql.type.Ne;
import br.com.mobilemind.veloster.sql.type.NotIsNull;
import br.com.mobilemind.veloster.teste.pack.Person;
import br.com.mobilemind.veloster.teste.pack.PersonGroup;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.security.InvalidParameterException;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Bocchi
 */
public class ORMManagerQueryTestCase extends BaseTestCase {

    @Test
    public void test(){
        
    }
    
//    @Test
//    public void testCreateTablePerson() {
//        String keyWord = VelosterConfig.getConf().getDriver().getAutoincrementKey();
//        String dateTymeData = VelosterConfig.getConf().getDriver().getDataType().getTypeToDate();
//        String query = "Create Table Person(name Varchar(100) Not Null ,birthDay " + dateTymeData + " Not Null ,personType Integer Not Null ,group_id Integer Not Null ,id Integer Primary Key " + keyWord + " Not Null, FOREIGN KEY(group_id) REFERENCES PersonsGroups(id));";
//        Veloster manager = VelosterRepository.getVeloster(Person.class);
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getCreateTableQuery().
//                toUpperCase().trim();
//        Assert.assertTrue(result.equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateCreateTablePersonGroup() {
//
//        String keyWord = VelosterConfig.getConf().getDriver().getAutoincrementKey();
//
//        String query = "Create Table PersonsGroups(name Varchar(100) Not Null ,id Integer Primary Key " + keyWord + " Not Null );";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        Assert.assertTrue("error at create query Create PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getCreateTableQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateDroptPerson() {
//        String query = "Drop Table Person;";
//        Veloster manager = VelosterRepository.getVeloster(Person.class);
//        Assert.assertTrue("error at create drop Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getDropTableQuery().toUpperCase().
//                trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateDroptPersonGroup() {
//        String query = "Drop Table PersonsGroups;";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//
//        Assert.assertTrue("error at create drop PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getDropTableQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateInsertPerson() {
//        String query = "Insert Into Person (name, birthDay, personType, group_id) Values (?, ?, ?, ?)";
//        Veloster manager = VelosterRepository.getVeloster(Person.class);
//        Assert.assertTrue("error at create query Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getInsertQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateInsertPersonGroup() {
//        String query = "Insert Into PersonsGroups (name) Values (?)";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        Assert.assertTrue("error at create query PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getInsertQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateDeleteAllPerson() {
//        String query = "Delete From Person";
//        Veloster manager = VelosterRepository.getVeloster(Person.class);
//        Criteria criteria = manager.createCriteria();
//        Assert.assertTrue("error at create delete Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getDeleteQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateDeleteAllPersonGroup() {
//        String query = "Delete From PersonsGroups";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        Assert.assertTrue("error at create delete PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getDeleteQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateSelectAllPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person";
//        Veloster manager = VelosterRepository.getVeloster(Person.class);
//        Assert.assertTrue("error at create select Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getSelectQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateSelectAllPersonGroup() {
//        String query = "Select name, id From PersonsGroups";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        Assert.assertTrue("error at create select PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getSelectQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateUpdateAllPerson() {
//        String query = "Update Person Set name = ?, birthDay = ?, personType = ?, group_id = ?, id = ?";
//        Veloster manager = VelosterRepository.getVeloster(Person.class);
//        Assert.assertTrue("error at create select Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getUpdateQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateUpdateAllPersonGroup() {
//        String query = "Update PersonsGroups Set name = ?, id = ?";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        Assert.assertTrue("error at create select PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getUpdateQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test(expected = InvalidParameterException.class)
//    public void testSelectWhereWithFieldNotFoundPerson() {
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        Criteria criteria = VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().createCriteria();
//        criteria.add("notexistsfield", new Eq("valor"));
//    }
//
//    @Test(expected = InvalidParameterException.class)
//    public void testSelectWhereWithFieldNotAnnotedPerson() {
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        Criteria criteria = VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().createCriteria();
//        criteria.add("fieldNotAnnoted", new Gt(""));
//    }
//
////    @Test(expected = InvalidParameterException.class)
////    public void testWithEntityNotAnnoted() {
////        //ORMManager manager = new ORMManagerImpl(EntityNotAnnoted.class);
////    }
//    @Test
//    public void testSelectWhereNameLikeAnyWharePerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name Like ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Like("john", Match.ANYWHERE));
//        Assert.assertTrue("error at create select with where name Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().
//                getSelectQuery().toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testSelectWhereNameLikeStartWithPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name Like ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Like("john", Match.STARTWITH));
//        Assert.assertTrue("error at create select with where name Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().
//                getSelectQuery().toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testSelectWhereNameNotLikeStartWithPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name Not Like ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Like("john", Match.STARTWITH, false, true));
//        Assert.assertTrue("error at create select with where name Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().
//                getSelectQuery().toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testSelectWhereNameLikeEndWithPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name Like ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Like("john", Match.ENDWITH));
//        Assert.assertTrue("error at create select with where name Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().
//                getSelectQuery().toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testSelectWhereNameLikeExactPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name Like ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Like("john", Match.EXACT));
//        Assert.assertTrue("error at create select with where name Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().
//                getSelectQuery().toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testSelectWhereIdPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (id = ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("id", new Eq(0));
//        Assert.assertTrue("error at create select with where id Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().
//                getSelectQuery().toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testSelectWhereIdAndNamePerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name = ?) And (id = ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Eq(""));
//        criteria.add("id", new Eq(""));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getSelectQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create select with where id and name Person",
//                result.equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testDeleteWhereNamePerson() {
//        String query = "Delete From Person Where (name = ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Eq(""));
//        Assert.assertTrue("error at create delete with where name Person", VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().
//                getDeleteQuery().toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testDeleteWhereNameAndIdPersonGroup() {
//        String query = "Delete From PersonsGroups Where (id = ?) And (name = ?)";
//        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
//        Criteria<PersonGroup> criteria = manager.createCriteria();
//        criteria.add("id", new Eq(""));
//        criteria.add("name", new Eq(""));
//        String result = VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getDeleteQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create delete with where name and id PersonGroup", result.equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testUpdateWhereIdPerson() {
//        String query = "Update Person Set name = ?, birthDay = ?, personType = ?, group_id = ?, id = ? Where (id = ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("id", new Eq(""));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getUpdateQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testUpdateWhereIdAndNamePerson() {
//        String query = "Update Person Set name = ?, birthDay = ?, personType = ?, group_id = ?, id = ? Where (id = ?) And (name = ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("id", new Eq(""));
//        criteria.add("name", new Eq(""));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getUpdateQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testUpdateWhereIdAndNameOrNamePerson() {
//        String query = "Update Person Set name = ?, birthDay = ?, personType = ?, group_id = ?, id = ? Where (id = ?) And (name = ? Or name = ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("id", new Eq(""));
//        criteria.add("name", new Eq("a").eq("b"));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getUpdateQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testUpdateWhereIdOrIdAndNameOrNamePerson() {
//        String query = "Update Person Set name = ?, birthDay = ?, personType = ?, group_id = ?, id = ? Where (id = ? Or id = ?) And (name = ? Or name = ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("id", new Eq("a").eq("b"));
//        criteria.add("name", new Eq("a").eq("b"));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getUpdateQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testSelectNotEqualsPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name <> ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Ne("a"));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getSelectQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testSelectLessThanPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name < ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Lt("a"));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getSelectQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testSelectGreaterThanPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name > ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Gt("a"));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getSelectQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testSelectLessOrEqualsPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name <= ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Le("a"));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getSelectQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testSelectGreaterOrEqualsPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name >= ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Ge("a"));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getSelectQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testSelectBetweenPerson() {
//        String query = "Select name, birthDay, personType, group_id, id From Person Where (name Between ? And ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("name", new Between("a", "b"));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getSelectQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testJoin() {
//        String query = "Select Person.name, Person.birthDay, Person.personType, Person.group_id, Person.id From Person join PersonsGroups on Person.group_id = PersonsGroups.id Where (PersonsGroups.name Like ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria<Person> criteria = manager.createCriteria();
//        criteria.add("group.name", new Like("teste", Match.ANYWHERE));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getSelectQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create update with where id Person", result.equals(query.toUpperCase().
//                trim()));
//    }
//
//    @Test
//    public void testCount() {
//        String query = "Select Count(id) From Person";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getCountQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create count from Person", result.equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCountByCriteroa() {
//        String query = "Select Count(id) From Person Where (id = ?)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria criteria = manager.createCriteria();
//        criteria.add("id", new Eq(1));
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getCountQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create count from Person", result.equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testIsNullCriteria() {
//        String query = "Select Count(id) From Person Where (id is null)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria criteria = manager.createCriteria();
//        criteria.add("id", new IsNull());
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getCountQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create count from Person", result.equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testNotIsNullCriteria() {
//        String query = "Select Count(id) From Person Where (id is not null)";
//        Veloster<Person> manager = VelosterRepository.getVeloster(Person.class);
//        Criteria criteria = manager.createCriteria();
//        criteria.add("id", new NotIsNull());
//        String result = VelosterRepository.getVelosterControll(Person.class).getQueryBuilder().getCountQuery().toUpperCase().trim();
//        Assert.assertTrue("error at create count from Person", result.equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testListPaged() {
//        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
//        manager.createCriteria().setLimit(10).setOffset(5);
//
//        String result = VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getSelectQuery().toUpperCase().trim();
//
//        Assert.assertEquals("Select name, id From PersonsGroups limit 10 offset 5".toUpperCase(), result);
//    }
//
//    @Test
//    public void testCreateSelectAllPersonGroupOrderByName() {
//        String query = "Select name, id From PersonsGroups Order By name";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        manager.createCriteria().orderBy("name");
//        Assert.assertTrue("error at create select PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getSelectQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateSelectAllPersonGroupOrderByNameAndId() {
//        String query = "Select name, id From PersonsGroups Order By name, id";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        manager.createCriteria().orderBy("name", "id");
//        Assert.assertTrue("error at create select PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getSelectQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateSelectAllPersonGroupOrderByNameAndIdDesc() {
//        String query = "Select name, id From PersonsGroups Order By name desc, id desc";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        manager.createCriteria().orderByDesc("name", "id");
//        Assert.assertTrue("error at create select PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getSelectQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
//
//    @Test
//    public void testCreateSelectAllPersonGroupOrderByNameAndIdPaged() {
//        String query = "Select name, id From PersonsGroups Order By name, id limit 10 offset 5";
//        Veloster manager = VelosterRepository.getVeloster(PersonGroup.class);
//        manager.createCriteria().orderBy("name", "id").setLimit(10).setOffset(5);
//        Assert.assertTrue("error at create select PersonGroup", VelosterRepository.getVelosterControll(PersonGroup.class).getQueryBuilder().getSelectQuery().
//                toUpperCase().trim().equals(query.toUpperCase().trim()));
//    }
}
