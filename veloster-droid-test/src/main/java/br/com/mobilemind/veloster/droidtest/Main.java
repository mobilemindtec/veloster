package br.com.mobilemind.veloster.droidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import br.com.mobilemind.api.droidunit.TestResultWebView;
import br.com.mobilemind.api.droidunit.TestSuite;
import br.com.mobilemind.api.droidutil.logs.AppLogger;
import br.com.mobilemind.veloster.droidtest.test.BackupTestCase;
import br.com.mobilemind.veloster.droidtest.test.ConnectionTestCase;
import br.com.mobilemind.veloster.droidtest.test.DynamicFinderProcessorTestCase;
import br.com.mobilemind.veloster.droidtest.test.LazyLoadTestCase;
import br.com.mobilemind.veloster.droidtest.test.NativeQueryTestCase;
import br.com.mobilemind.veloster.droidtest.test.OrderByTestCase;
import br.com.mobilemind.veloster.droidtest.test.QueryToolsListenerTestCase;
import br.com.mobilemind.veloster.droidtest.test.ReferenceCascadeTestCase;
import br.com.mobilemind.veloster.droidtest.test.StatementCountTestCase;
import br.com.mobilemind.veloster.droidtest.test.StatementPersonGroupTestCase;
import br.com.mobilemind.veloster.droidtest.test.TransactionalOperationTestCase;
import br.com.mobilemind.veloster.droidtest.test.ValidationTestCase;
import br.com.mobilemind.veloster.sql.VelosterDroid;

/**
 *
 * @author Ricardo Bocchi
 */
public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppLogger.info(getClass(), "#### load logger");

        //sleep();

        VelosterDroid.buildTestMode(getApplication());

        TestSuite.addTestCase(BackupTestCase.class);
        TestSuite.addTestCase(ConnectionTestCase.class);
        TestSuite.addTestCase(DynamicFinderProcessorTestCase.class);
        TestSuite.addTestCase(LazyLoadTestCase.class);
        TestSuite.addTestCase(NativeQueryTestCase.class);
        TestSuite.addTestCase(OrderByTestCase.class);
        TestSuite.addTestCase(QueryToolsListenerTestCase.class);
        TestSuite.addTestCase(ReferenceCascadeTestCase.class);
        TestSuite.addTestCase(StatementPersonGroupTestCase.class);
        TestSuite.addTestCase(StatementCountTestCase.class);
        TestSuite.addTestCase(TransactionalOperationTestCase.class);
        TestSuite.addTestCase(ValidationTestCase.class);

        Intent it = new Intent(getApplication(), TestResultWebView.class);
        startActivity(it);
    }

    public void sleep() {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
        }
    }
}
