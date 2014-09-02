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

import br.com.mobilemind.veloster.orm.RepositoryModel;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.sql.type.Eq;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.veloster.teste.pack.DynamicFinderEntity;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Bocchi
 */
public class DynamicFinderProcessorTestCase extends BaseTestCase {

    public static interface Finder {

        List<DynamicFinderEntity> findAllByName(String nome);

        List<DynamicFinderEntity> findAllByNameStartsWith(String nome);

        List<DynamicFinderEntity> findAllByNameEndsWith(String nome);

        List<DynamicFinderEntity> findAllByNameAndCampo1AndCampo2AndCampo3(String args1, String args2, String args3, String args4);

        List<DynamicFinderEntity> findAllByNameStartsWithAndCampo1StartsWithAndCampo2StartsWithAndCampo3StartsWith(String args1, String args2, String args3, String args4);

        List<DynamicFinderEntity> findAllByNameEndsWithAndCampo1EndsWithAndCampo2EndsWithAndCampo3EndsWith(String args1, String args2, String args3, String args4);

        List<DynamicFinderEntity> findAllByNameAnyWhere(String ri);

        List<DynamicFinderEntity> findAllByNameAndCampo1AnyWhere(String args1, String args2);

        DynamicFinderEntity findByName(String args);

        DynamicFinderEntity findByNameStartsWith(String args);

        DynamicFinderEntity findByNameEndsWith(String args);

        DynamicFinderEntity findByNameStartsWithAndCampo1EndsWith(String args, String args2);

        DynamicFinderEntity findByNameStartsWithNot(String args);

        List<DynamicFinderEntity> findAllOrderByName();

        List<DynamicFinderEntity> findAllOrderByNameOrderDesc();

        List<DynamicFinderEntity> findAllByNameStartsWithOrderByName(String args1);

        List<DynamicFinderEntity> findAllByNameStartsWithOrderByNameOrderDesc(String args1);

        DynamicFinderEntity findByNameIsNull();

        DynamicFinderEntity findByNameNotIsNull();

        DynamicFinderEntity findByCampo1IsNullNameNotIsNull();

        DynamicFinderEntity findByNameAndCampo1IsNull(String arg);

        List<DynamicFinderEntity> findAllByNameAndCampo1IsNull(String arg);

        int countByName(String arg);

        int countByNameAndId(String arg, Long id);
    }

    interface FinderModel extends RepositoryModel<DynamicFinderEntity> {
    }

    @Test
    public void testFinder() {

        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.assertNotNull(finder.findAllByName("ricardo"));
        Assert.assertTrue(finder.findAllByName("ricardo").size() == 1);
        Assert.assertEquals("ricardo", finder.findAllByName("ricardo").get(0).getName());
    }

    @Test
    public void testFinderStartsWith() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.assertNotNull(finder.findAllByNameStartsWith("ri"));
        Assert.assertTrue(finder.findAllByNameStartsWith("ri").size() == 1);

    }

    @Test
    public void testFinderAnyWhere() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.assertNotNull(finder.findAllByNameAnyWhere("car"));
        Assert.assertTrue(finder.findAllByNameAnyWhere("car").size() == 1);

    }

    @Test
    public void testFinderWhereAndAnyWhere() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("veloster");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.assertNotNull(finder.findAllByNameAndCampo1AnyWhere("ricardo", "los"));
        Assert.assertTrue(finder.findAllByNameAndCampo1AnyWhere("ricardo", "los").size() == 1);

    }

    @Test
    public void testFinderEndsWith() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.assertNotNull(finder.findAllByNameEndsWith("lo"));
        Assert.assertTrue(finder.findAllByNameEndsWith("lo").size() == 1);
    }

    @Test
    public void testFinderVariosAnds() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        List<DynamicFinderEntity> items = finder.findAllByNameAndCampo1AndCampo2AndCampo3("ricardo", "campo1", "campo2", "campo3");

        Assert.assertNotNull(items);
        Assert.assertTrue(items.size() == 1);
    }

    @Test
    public void testFinderVariosStartsWiths() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        List<DynamicFinderEntity> items = finder.findAllByNameStartsWithAndCampo1StartsWithAndCampo2StartsWithAndCampo3StartsWith("rica", "cam", "cam", "ca");

        Assert.assertNotNull(items);
        Assert.assertTrue(items.size() == 1);
    }

    @Test
    public void testFinderVariosEndsWiths() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        List<DynamicFinderEntity> items = finder.findAllByNameEndsWithAndCampo1EndsWithAndCampo2EndsWithAndCampo3EndsWith("ulo", "o4", "mpo5", "po6");

        Assert.assertNotNull(items);
        Assert.assertTrue(items.size() == 1);
    }

    @Test
    public void testFinderLoadByName() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.assertNotNull(finder.findByName("ricardo"));
        Assert.assertNotNull(finder.findByName("joaopaulo"));
    }

    @Test
    public void testFinderLoadByNameStartsWith() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.assertNotNull(finder.findByNameStartsWith("r"));
        Assert.assertNotNull(finder.findByNameStartsWith("joa"));

    }

    @Test
    public void testFinderLoadByNameEndsWith() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.assertNotNull(finder.findByNameEndsWith("rdo"));
        Assert.assertNotNull(finder.findByNameEndsWith("ulo"));

    }

    @Test
    public void testFinderLoadByNameStartsWithAndCampo1EndsWith() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.assertNotNull(finder.findByNameStartsWithAndCampo1EndsWith("ric", "o1"));
        Assert.assertNotNull(finder.findByNameStartsWithAndCampo1EndsWith("joa", "po4"));

    }

    @Test
    public void testFinderLoadByNameNot() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.assertEquals("joaopaulo", finder.findByNameStartsWithNot("ric").getName());
        Assert.assertEquals("ricardo", finder.findByNameStartsWithNot("joa").getName());

    }

    @Test
    public void testFindAllOrderByName() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        List<DynamicFinderEntity> items = finder.findAllOrderByName();

        Assert.assertEquals("joaopaulo", items.get(0).getName());
        Assert.assertEquals("ricardo", items.get(1).getName());
    }

    @Test
    public void testFindAllOrderByNameOrderDesc() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p2);
        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        List<DynamicFinderEntity> items = finder.findAllOrderByNameOrderDesc();

        Assert.assertEquals("ricardo", items.get(0).getName());
        Assert.assertEquals("joaopaulo", items.get(1).getName());
    }

    @Test
    public void testFindAllByNameStartsWithOrderByName() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("ricardao");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p2);
        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        List<DynamicFinderEntity> items = finder.findAllByNameStartsWithOrderByName("rica");

        Assert.assertEquals("ricardao", items.get(0).getName());
        Assert.assertEquals("ricardo", items.get(1).getName());
    }

    @Test
    public void testFindAllByNameStartsWithOrderByNameDesc() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("ricardao");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p2);
        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        List<DynamicFinderEntity> items = finder.findAllByNameStartsWithOrderByNameOrderDesc("rica");

        Assert.assertEquals("ricardo", items.get(0).getName());
        Assert.assertEquals("ricardao", items.get(1).getName());
    }

    @Test
    public void testFindIsNullName() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity(null);
        p.setCampo1("campo1");
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        DynamicFinderEntity p2 = new DynamicFinderEntity("ricardao");
        p2.setCampo1("1campo4");
        p2.setCampo2("2campo5");
        p2.setCampo3("3campo6");

        manager.save(p2);
        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.assertEquals("campo1", finder.findByNameIsNull().getCampo1());

        Assert.assertEquals("ricardao", finder.findByNameNotIsNull().getName());

    }

    @Test
    public void testFindByCampo1IsNullAndNameNotIsNull() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1(null);
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.assertEquals("ricardo", finder.findByCampo1IsNullNameNotIsNull().getName());
    }

    @Test
    public void testFindByNameAndCampo1IsNull() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1(null);
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.assertEquals("ricardo", finder.findByNameAndCampo1IsNull("ricardo").getName());
    }

    @Test
    public void testFindAllByNameAndCampo1IsNull() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1(null);
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.assertEquals("ricardo", finder.findAllByNameAndCampo1IsNull("ricardo").get(0).getName());
    }

    @Test
    public void testCountByName() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("ricardo");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.assertEquals(2, finder.countByName("ricardo"));

        Assert.assertEquals(1, finder.countByNameAndId("ricardo", p.getId()));
    }

    @Test
    public void testRepositoryModel() {

        FinderModel finder = VelosterRepository.getDynamicFinder(FinderModel.class, DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("ricardo");

        finder.persist(p);
        finder.persist(p2);

        Assert.assertEquals(2, finder.list().size());
        Assert.assertNotNull(finder.load(p.getId()));
        Assert.assertEquals(2, finder.count());

        p.setName("frederico");
        finder.merge(p);

        Criteria criteria = finder.createCriteria();
        criteria.add("name", new Eq("frederico"));
        Assert.assertEquals("frederico", finder.loadByCriteria(criteria).getName());

        finder.createCriteria();
        finder.removeByCriteria(criteria);

        Assert.assertTrue(finder.listByCriteria(criteria).isEmpty());
    }
}
