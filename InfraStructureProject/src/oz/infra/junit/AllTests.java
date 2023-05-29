package oz.infra.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ oz.infra.array.test.JUnitTestArrayUtils.class, oz.infra.base64.test.JUnitTestBase64Utils.class,
		oz.infra.io.test.JUnitTestPathUtils.class, oz.infra.list.test.JUnitTestListUtils.class,
		oz.infra.net.ip.test.JUnitTestIpUtils.class })
// ,oz.infra.fibi.test.JUnitTestFibiUtils.class })
public class AllTests {

}