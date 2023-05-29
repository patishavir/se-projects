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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends Dialog {

	private String userName;
	private String password;
	private ReportApplication application;
	private Shell shell;

	public LoginDialog(ReportApplication application, Shell parent) {
		this(application, parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	public LoginDialog(ReportApplication application, Shell parent, int style) {
		super(parent, style);
		this.application = application;
	}

	public void open() {
		Shell shell = new Shell(getParent(), getStyle());
		this.shell = shell;
		shell.setText("Login to Billing Report System");
		renderContents(shell);
		shell.pack();
		shell.setSize(400, 115);
		shell.open();
	}

	private void renderContents(final Shell shell) {
		GridLayout layout = new GridLayout(2, true);
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		shell.setLayout(layout);

		final Label userNameLabel = new Label(shell, SWT.NONE);
		userNameLabel.setText("User Name:");

		final Text userNameText = new Text(shell, SWT.BORDER);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		userNameText.setLayoutData(layoutData);

		final Label passwordLabel = new Label(shell, SWT.NONE);
		passwordLabel.setText("Password:");

		final Text passwordText = new Text(shell, SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		passwordText.setLayoutData(layoutData);
		passwordText.setEchoChar('*');
		
		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		layoutData.widthHint = 100;
		ok.setLayoutData(layoutData);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				userName = userNameText.getText();
				password = passwordText.getText();
				try {
					application.getCEHelper().login(userName, password);
					shell.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					MessageBox messageBox = new MessageBox(shell, SWT.OK);
					messageBox.setMessage("Login failed, please try again.");
					messageBox.open();
				}
			}
		});

		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		layoutData.widthHint = 100;
		cancel.setLayoutData(layoutData);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				userName = null;
				shell.close();
				getParent().close();
			}
		});

		shell.setDefaultButton(ok);
	}

	public Shell getShell() {
		return shell;
	}
}