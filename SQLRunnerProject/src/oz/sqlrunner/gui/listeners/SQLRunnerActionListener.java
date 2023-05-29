package oz.sqlrunner.gui.listeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import oz.guigenerator.GuiGeneratorMain;
import oz.guigenerator.GuiGeneratorParamsFileProcessor;
import oz.infra.logging.jul.JulUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.SQLRunnerJTabbedPane;
import oz.sqlrunner.gui.enums.SQLRunnerGuiComponentsEnum;
import oz.sqlrunner.handlers.ConnectionHandler;
import oz.sqlrunner.handlers.ExitHandler;
import oz.sqlrunner.handlers.FilterHandler;
import oz.sqlrunner.handlers.HelpAboutHandler;
import oz.sqlrunner.handlers.RunSQLStatementHandler;
import oz.sqlrunner.handlers.UsageHandler;

public class SQLRunnerActionListener implements ActionListener {

	private Logger logger = JulUtils.getLogger();

	private GuiGeneratorParamsFileProcessor filterGuiGeneratorParamsFileProcessor;
	private FilterHandler sqlRunnerFilterHandler = new FilterHandler();

	public final void actionPerformed(final ActionEvent e) {
		String actionCommandString = e.getActionCommand();
		logger.finest("actionCommandString: " + actionCommandString);
		SQLRunnerConnectionParameters sqlRunnerConnectionParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		try {
			SQLRunnerGuiComponentsEnum guiComponent = SQLRunnerGuiComponentsEnum
					.valueOf(actionCommandString);
			logger.finest(guiComponent.toString());
			switch (guiComponent) {
			case Connect:
				ConnectionHandler.getConnectionHandler().showGui();
				break;
			case DisConnect:
				ConnectionHandler.getConnectionHandler().disConnect();
				break;
			case Filter:
				Component rootComponent = sqlRunnerConnectionParameters.getSqlRunnerJFrame();
				filterGuiGeneratorParamsFileProcessor = GuiGeneratorMain.showGui(
						filterGuiGeneratorParamsFileProcessor,
						SQLRunnerStaticParameters.getFilterGuiXmlFile(), sqlRunnerFilterHandler,
						sqlRunnerFilterHandler, rootComponent);
				break;
			case Run:
				RunSQLStatementHandler.runSQLStatement();
				break;
			case NewSqlStatement:
				SQLRunnerJTabbedPane jTabbedPane = sqlRunnerConnectionParameters
						.getSqlRunnerJTabbedPane();
				jTabbedPane.addSplitPane(null, "New Query", null);
				break;
			case Refresh:
				sqlRunnerConnectionParameters.getSqlRunnerJFrame().validate();
				sqlRunnerConnectionParameters.getSqlRunnerJFrame().repaint();
				logger.info("Refresh processing has completed !");
				break;
			case About:
				HelpAboutHandler.showAboutDialog();
				break;
			case Usage:
				logger.finest("usage processing ...");
				UsageHandler.showUsageDialog();
				break;
			case Exit:
				ExitHandler.doExit("Exiting ...");
				System.exit(0);
				break;
			default:
				logger.warning("Unknown option: " + guiComponent.toString());
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}

}
