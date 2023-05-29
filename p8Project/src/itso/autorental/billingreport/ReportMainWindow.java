/*
 * DISCLAIMER OF WARRANTIES.  The following [enclosed] code is
 * sample code created by IBM Corporation.  This sample code is
 * not part of any standard or IBM product and is provided to you
 * solely for the purpose of assisting you in the development of
 * your applications.  The code is provided "AS IS", without
 * warranty of any kind.  IBM shall not be liable for any damages
 * arising out of your use of the sample code, even if they have
 * been advised of the possibility of such damages.
 */

package itso.autorental.billingreport;

import java.io.IOException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class ReportMainWindow {
	ReportApplication reportApplication;
	private Shell shell;

	public ReportMainWindow(ReportApplication reportApplication) {
		this.reportApplication = reportApplication;
	}

	public void renderContents(final Shell shell) {
		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns = 3;
		shell.setLayout(mainLayout);
		shell.setText("Vehicle Hours Report");
		GridLayout searchToolbarLayout = new GridLayout();
		searchToolbarLayout.numColumns = 6;
		searchToolbarLayout.horizontalSpacing = 5;
		
		Composite searchToolbar = new Composite(shell, SWT.NONE);
		searchToolbar.setLayout(searchToolbarLayout);
		
		final Label fromText = new Label(searchToolbar, SWT.NONE);
		final DateTime fromDate = new DateTime (searchToolbar, SWT.DATE);
		final Label toText = new Label(searchToolbar, SWT.NONE);
		final DateTime toDate = new DateTime (searchToolbar, SWT.DATE);
		final Button searchButton = new Button(searchToolbar, SWT.PUSH);
		final Button printButton = new Button(shell, SWT.PUSH);
		final Browser browser;
		
		GridData data = new GridData();
		data.horizontalAlignment  = SWT.END;
		printButton.setLayoutData(data);
		
		fromText.setText("Start Date:");
		toText.setText("End Date:");
		searchButton.setText("Search");
		printButton.setText("Print Report");
		
		data = new GridData();
		data.horizontalSpan = 2;
		searchToolbar.setLayoutData(data);
		

		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			return;
		}
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		browser.setLayoutData(data);

		final Label status = new Label(shell, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		status.setLayoutData(data);

		final ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		progressBar.setLayoutData(data);
		
		Listener listener = new Listener() {
		   public void handleEvent(Event event) {
			   Date startDate = new Date();
			   int year = fromDate.getYear() - 1900;
			   int month = fromDate.getMonth();
			   int date = fromDate.getDay();
			   startDate.setYear(year);
			   startDate.setMonth(month);
			   startDate.setDate(date);
			   Date endDate = new Date();
			   year = toDate.getYear() - 1900;
			   month = toDate.getMonth();
			   date = toDate.getDay();
			   endDate.setYear(year);
			   endDate.setMonth(month);
			   endDate.setDate(date);
			   
			   try {
					reportApplication.generateReport(startDate, endDate);
				} catch (IOException e) {
					e.printStackTrace();
				}
			   browser.setUrl(System.getProperty("user.dir") + "/VehicleHoursReport.html");
		   }
		};
		searchButton.addListener(SWT.Selection, listener);
	}
	
	public void open() {
		Display display = new Display();
		this.shell = new Shell(display);
		renderContents(shell);
		shell.open();
		
		LoginDialog loginDialog = new LoginDialog(this.reportApplication, shell);
		loginDialog.open();
		
		Rectangle parentSize = shell.getBounds();
		Rectangle dialogSize = loginDialog.getShell().getBounds();

		int locationX, locationY;
		locationX = (parentSize.width - dialogSize.width)/2+parentSize.x;
		locationY = (parentSize.height - dialogSize.height)/2+parentSize.y;

		loginDialog.getShell().setLocation(new Point(locationX, locationY));
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public Shell getShell() {
		return shell;
	}
}
