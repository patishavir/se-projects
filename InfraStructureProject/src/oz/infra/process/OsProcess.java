package oz.infra.process;

public class OsProcess {
	private String pid;
	private String command;
	private String commandFullPath;

	public OsProcess(final String pid, final String command, final String commandFullPath) {
		this.pid = pid;
		this.command = command;
		this.commandFullPath = commandFullPath;
	}

	public String getPid() {
		return pid;
	}

	public String getCommand() {
		return command;
	}

	public String getCommandFullPath() {
		return commandFullPath;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("pid: ");
		sb.append(pid);
		sb.append(" command: ");
		sb.append(command);
		sb.append(" args: ");
		sb.append(commandFullPath);
		return sb.toString();
	}
}
