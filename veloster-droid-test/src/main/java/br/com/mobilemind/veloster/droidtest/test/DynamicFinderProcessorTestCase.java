package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.droidtest.pack.DynamicFinderEntity;
import br.com.mobilemind.veloster.orm.RepositoryModel;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.sql.type.Eq;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.util.List;

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

        List<DynamicFinderEntity> findAllOrderByDescName();

        List<DynamicFinderEntity> findAllByNameStartsWithOrderByName(String args1);

        List<DynamicFinderEntity> findAllByNameStartsWithOrderByDescName(String args1);

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

    public void testFinder() {

        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.isNotNull(finder.findAllByName("ricardo"));
        Assert.isTrue(finder.findAllByName("ricardo").size() == 1);
        Assert.areEqual("ricardo", finder.findAllByName("ricardo").get(0).getName());
    }

    public void testFinderStartsWith() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.isNotNull(finder.findAllByNameStartsWith("ri"));
        Assert.isTrue(finder.findAllByNameStartsWith("ri").size() == 1);

    }

    public void testFinderAnyWhere() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.isNotNull(finder.findAllByNameAnyWhere("car"));
        Assert.isTrue(finder.findAllByNameAnyWhere("car").size() == 1);

    }

    public void testFinderWhereAndAnyWhere() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1("veloster");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);
        Assert.isNotNull(finder.findAllByNameAndCampo1AnyWhere("ricardo", "los"));
        Assert.isTrue(finder.findAllByNameAndCampo1AnyWhere("ricardo", "los").size() == 1);

    }

    public void testFinderEndsWith() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("joaopaulo");
        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.isNotNull(finder.findAllByNameEndsWith("lo"));
        Assert.isTrue(finder.findAllByNameEndsWith("lo").size() == 1);
    }

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

        Assert.isNotNull(items);
        Assert.isTrue(items.size() == 1);
    }

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

        Assert.isNotNull(items);
        Assert.isTrue(items.size() == 1);
    }

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

        Assert.isNotNull(items);
        Assert.isTrue(items.size() == 1);
    }

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
        Assert.isNotNull(finder.findByName("ricardo"));
        Assert.isNotNull(finder.findByName("joaopaulo"));
    }

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
        Assert.isNotNull(finder.findByNameStartsWith("r"));
        Assert.isNotNull(finder.findByNameStartsWith("joa"));

    }

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
        Assert.isNotNull(finder.findByNameEndsWith("rdo"));
        Assert.isNotNull(finder.findByNameEndsWith("ulo"));

    }

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
        Assert.isNotNull(finder.findByNameStartsWithAndCampo1EndsWith("ric", "o1"));
        Assert.isNotNull(finder.findByNameStartsWithAndCampo1EndsWith("joa", "po4"));

    }

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
        Assert.areEqual("joaopaulo", finder.findByNameStartsWithNot("ric").getName());
        Assert.areEqual("ricardo", finder.findByNameStartsWithNot("joa").getName());

    }

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

        Assert.areEqual("joaopaulo", items.get(0).getName());
        Assert.areEqual("ricardo", items.get(1).getName());
    }

    public void testFindAllOrderByDescName() {
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

        List<DynamicFinderEntity> items = finder.findAllOrderByDescName();

        Assert.areEqual("ricardo", items.get(0).getName());
        Assert.areEqual("joaopaulo", items.get(1).getName());
    }

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

        Assert.areEqual("ricardao", items.get(0).getName());
        Assert.areEqual("ricardo", items.get(1).getName());
    }

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

        List<DynamicFinderEntity> items = finder.findAllByNameStartsWithOrderByDescName("rica");

        Assert.areEqual("ricardo", items.get(0).getName());
        Assert.areEqual("ricardao", items.get(1).getName());
    }

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

        Assert.areEqual("campo1", finder.findByNameIsNull().getCampo1());

        Assert.areEqual("ricardao", finder.findByNameNotIsNull().getName());

    }

    public void testFindByCampo1IsNullAndNameNotIsNull() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1(null);
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.areEqual("ricardo", finder.findByCampo1IsNullNameNotIsNull().getName());
    }

    public void testFindByNameAndCampo1IsNull() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1(null);
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.areEqual("ricardo", finder.findByNameAndCampo1IsNull("ricardo").getName());
    }

    public void testFindAllByNameAndCampo1IsNull() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        p.setCampo1(null);
        p.setCampo2("campo2");
        p.setCampo3("campo3");

        manager.save(p);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.areEqual("ricardo", finder.findAllByNameAndCampo1IsNull("ricardo").get(0).getName());
    }

    public void testCountByName() {
        Veloster<DynamicFinderEntity> manager = VelosterRepository.getVeloster(DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("ricardo");

        manager.save(p);
        manager.save(p2);

        Finder finder = VelosterRepository.getDynamicFinder(Finder.class, DynamicFinderEntity.class);

        Assert.areEqual(2, finder.countByName("ricardo"));

        Assert.areEqual(1, finder.countByNameAndId("ricardo", p.getId()));
    }

    public void testRepositoryModel() {

        FinderModel finder = VelosterRepository.getDynamicFinder(FinderModel.class, DynamicFinderEntity.class);
        DynamicFinderEntity p = new DynamicFinderEntity("ricardo");
        DynamicFinderEntity p2 = new DynamicFinderEntity("ricardo");

        finder.persist(p);
        finder.persist(p2);

        Assert.areEqual(2, finder.list().size());
        Assert.isNotNull(finder.load(p.getId()));
        Assert.areEqual(2, finder.count());

        p.setName("frederico");
        finder.merge(p);

        Criteria criteria = finder.createCriteria();
        criteria.add("name", new Eq("frederico"));
        Assert.areEqual("frederico", finder.loadByCriteria().getName());

        finder.createCriteria();
        finder.removeByCriteria();

        Assert.isTrue(finder.list().isEmpty());
    }
}
