package oz.utils.autodeploy.product;

import oz.utils.autodeploy.common.AutoDeployUtils;

public class TestPortalDeploymentProcessor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testZip();
		// testFolder();
		// testGet1TargetServerFoldersProcessorScript();
		testCopyFile2RemoteServers();
	}

	private static void testZip() {
		ProductDeploymentParameters.setProductVersionFolder("c:\\temp");
		ProductDeploymentProcessor.setupDeploymentZipAndFolder("C:\\oj\\data\\portal\\portalTest1\\oz\\oztestTest.zip");
	}

	private static void testFolder() {
		ProductDeploymentParameters.setProductVersionFolder("c:\\temp");
		ProductDeploymentProcessor.setupDeploymentZipAndFolder("C:\\oj\\data\\portal\\portalTest1\\oztestTest");
	}

	private static void testGet1TargetServerFoldersProcessorScript() {
		ProductDeploymentProcessor.get1TargetServerFoldersProcessorScript("Resources");
	}

	private static void testCopyFile2RemoteServers() {
		String[] targetServersArray = { "suswastest1", "suswastest2" };
		AutoDeployUtils.copyFile2RemoteServers("c:\\temp\\1122.txt", "/tmp/1122.txt", targetServersArray);
	}
}
