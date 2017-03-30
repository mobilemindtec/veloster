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


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Ricardo Bocchi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ColumnDefaultValueListener.class,
    ConnectionTestCase.class,
    DynamicFinderProcessorTestCase.class,
    LazyLoadTestCase.class,
    NativeQueryTestCase.class,
    ORMManagerQueryTestCase.class,
    OrderByTestCase.class,
    QueryToolsListenerTestCase.class,
    ReferenceCascadeTestCase.class,
    StatementPersonGroupTestCase.class,
    TableMetadataListenerTestCase.class,
    ValidationTestCase.class,
    TransactionalOperationTestCase.class,
    StatementCountTestCase.class,
    HasManyTestCase.class
})
public class TestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
