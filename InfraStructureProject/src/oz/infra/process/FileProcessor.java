package oz.infra.process;

import oz.infra.operaion.Outcome;

public interface FileProcessor {
	Outcome processFile(final String filePath);
}
