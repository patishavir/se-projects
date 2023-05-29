/**
	IBM grants you a nonexclusive copyright license to use all programming code 
	examples from which you can generate similar function tailored to your own 
	specific needs.

	All sample code is provided by IBM for illustrative purposes only.
	These examples have not been thoroughly tested under all conditions.  IBM, 
	therefore cannot guarantee or imply reliability, serviceability, or function of 
	these programs.

	All Programs or code component contained herein are provided to you “AS IS “ 
	without any warranties of any kind.
	The implied warranties of non-infringement, merchantability and fitness for a 
	particular purpose are expressly disclaimed.

	© Copyright IBM Corporation 2007, ALL RIGHTS RESERVED.
 */

package cesample;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * This JFrame hosts the other User Interface components
 * such as panels for creating and retrieving Document, CustomObject,
 * CompoundDocument, and Folder; a panel for testing the connection; and a panel
 * for making SQL queries to Content Engine in form of tabs.
 */
public class MainFrame extends JFrame 
{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTabbedPane mainTabbedPane = null;
	private JButton closeButton = null;
	private JPanel buttonPanel = null;
	private ConnectionPanel connectionPanel = null;
	private CreateDocPanel createDocPanel = null;
	private GetDocPanel getDocPanel = null;
	private CreateFolderPanel createFolderPanel = null;
	private CreateCustomObjectPanel createCustomObjectPanel = null;
	private GetCustomObjectPanel getCustomObjectPanel = null;
	private CreateCompoundDocPanel compoundDocPanel = null;
       private GetCompoundDocPanel getCompoundDocPanel = null;
       private AdHocQueryPanel adHocQueryPanel = null;
	private CEConnection ce = null;
	
	/**
	 * This is the default constructor.
	 */
	public MainFrame() 
	{
		super();
		ce = new CEConnection();
		initialize();
	}
	
	/**
	 * This method initializes this class.
	 * 
	 * @return void
	 */
	private void initialize() 
	{
		createDocPanel = new CreateDocPanel(ce);
		connectionPanel = new ConnectionPanel(ce);
		getDocPanel = new GetDocPanel(ce);
		createFolderPanel = new CreateFolderPanel(ce);
		createCustomObjectPanel = new CreateCustomObjectPanel(ce);
              getCustomObjectPanel = new GetCustomObjectPanel(ce);
              compoundDocPanel = new CreateCompoundDocPanel(ce);
              getCompoundDocPanel = new GetCompoundDocPanel(ce);
              adHocQueryPanel = new AdHocQueryPanel(ce);
		this.setSize(500, 400);
		this.setContentPane(getJContentPane());
		this.setTitle("Content Engine Java API Demo");
	}

	/**
	 * This method initializes jContentPane.
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() 
	{
		if (jContentPane == null) 
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
			jContentPane.add(getMainTabbedPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes mainTabbedPane.
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getMainTabbedPane() 
	{
		if (mainTabbedPane == null) 
		{
			mainTabbedPane = new JTabbedPane();
			mainTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
	        mainTabbedPane.setTabPlacement(javax.swing.JTabbedPane.TOP);
	        mainTabbedPane.addTab("Server Connection", connectionPanel);
	        mainTabbedPane.addTab("Create Doc", createDocPanel);
	        mainTabbedPane.addTab("Get Doc", getDocPanel);
	        mainTabbedPane.addTab("Create Folder", createFolderPanel);
	        mainTabbedPane.addTab("Create CustomObject", createCustomObjectPanel);
	        mainTabbedPane.addTab("Get CustomObject", getCustomObjectPanel);
	        mainTabbedPane.addTab("Create CompoundDoc", compoundDocPanel);
	        mainTabbedPane.addTab("Get Compound Doc", getCompoundDocPanel);
	        mainTabbedPane.addTab("Adhoc Query", adHocQueryPanel);
		}
		return mainTabbedPane;
	}
	
	/**
	 * This method initializes buttonPanel.	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() 
	{
		if (buttonPanel == null) 
		{
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.weightx = 2.0;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getCloseButton(), gridBagConstraints);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes closeButton.	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCloseButton() 
	{
		if (closeButton == null) 
		{
			closeButton = new JButton();
			closeButton.setText("Close");
			closeButton.addActionListener(new java.awt.event.ActionListener()
	        {
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	                closeButtonActionPerformed(evt);
	            }
	        });
		}
		return closeButton;
	}
	
	/*
	 * Closes this frame when user clicks Close button.
	 */
	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)                                            
    {                                                
        MainFrame.this.dispose();
    } 

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				MainFrame thisClass = new MainFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}
}
