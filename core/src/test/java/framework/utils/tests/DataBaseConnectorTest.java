package framework.utils.tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import framework.automation.setuptest.TestBase;
import framework.utils.database.SQLDataBaseConnector;
import framework.utils.database.SQLDatabaseClient;
import framework.utils.log.FrameworkLogger;
import framework.utils.log.LogFactory;
import framework.utils.tests.dto.database.DatabaseTestData;

public class DataBaseConnectorTest extends TestBase {

	private static final FrameworkLogger LOG = LogFactory
			.getLogger(DataBaseConnectorTest.class);

	SQLDatabaseClient sqlClient;

	@BeforeClass(dependsOnMethods = { "setupTestBase" })
	public void setupDataBaseConnectorTests(ITestContext context) {
		LOG.info("Before Suite");
		sqlClient = new SQLDatabaseClient();
		SQLDataBaseConnector.setConnectionProperties();
	}

	@AfterClass(dependsOnMethods = { "tearDownTestBase" })
	public void tearDownDataBaseConnectorTests(ITestContext context) {
		LOG.info("After Suite");
		SQLDataBaseConnector.shutdownConnPool();
	}

	@Test (enabled = false)
	public void getResultSetMapTest() {
		Assert.assertEquals(
				sqlClient.getMapFromResultSet(2, "Select * FROM test_data")
						.get("name"), "Ivan");
	}

	@Test (enabled = false)
	public void getResultSetObjectMappingTest() {
		Assert.assertEquals(
				sqlClient
						.mapTestDataToObjects("Select * FROM test_data",
								DatabaseTestData.class).get(0).getValue(),
				"234");
	}

}
