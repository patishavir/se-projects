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
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;

/**
 * User Interface to test connection with
 * Content Engine
 */
public class ConnectionPanel extends JPanel 
{

	private static final long serialVersionUID = 1L;
	private JPanel parameterPanel = null;
	private JLabel uriLabel = null;
	private JTextField uriTextField = null;
	private JLabel stanzaLabel = null;
	private JTextField stanzaTextField = null;
	private JLabel userLabel = null;
	private JTextField userTextField = null;
	private JLabel passLabel = null;
	private JTextField passTextField = null;
	private JButton connectButton = null;
	private JLabel domainLabel = null;
	private JTextField domainTextField = null;
	private JLabel osLabel = null;
	private JList osList = null;
	private JLabel statusLabel = null;
	private JButton clearButton = null;
	private Vector osnames = null;
    private CEConnection ce = null;
    
	/**
	 * This is the default constructor.
	 */
	public ConnectionPanel(CEConnection c) 
	{
		super();
		ce = c;
        osnames = new Vector();
		initialize();
	}

	/**
	 * This method initializes this class.
	 * 
	 * @return void
	 */
	private void initialize() 
	{
		statusLabel = new JLabel();
		statusLabel.setText("Test connection to CE");
		this.setLayout(new BorderLayout());
		this.setSize(328, 243);
		this.add(statusLabel, BorderLayout.SOUTH);
		this.add(getParameterPanel(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes parameterPanel.	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getParameterPanel() 
	{
		if (parameterPanel == null) 
		{
			osLabel = new JLabel();
			osLabel.setText("OS List");
			domainLabel = new JLabel();
			domainLabel.setText("Domain");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 6;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.gridheight = 3;
			GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
			gridBagConstraints81.anchor = GridBagConstraints.WEST;
			gridBagConstraints81.gridy = 6;
			gridBagConstraints81.gridx = 0;
			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.fill = GridBagConstraints.BOTH;
			gridBagConstraints71.gridy = 5;
			gridBagConstraints71.weightx = 1.0;
			gridBagConstraints71.gridx = 1;
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.anchor = GridBagConstraints.WEST;
			gridBagConstraints61.gridy = 5;
			gridBagConstraints61.gridx = 0;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 1;
			gridBagConstraints31.anchor = GridBagConstraints.CENTER;
			gridBagConstraints31.gridy = 9;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.anchor = GridBagConstraints.CENTER;
			gridBagConstraints8.gridy = 4;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 3;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 3;
			passLabel = new JLabel();
			passLabel.setText("Password");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 2;
			userLabel = new JLabel();
			userLabel.setText("UserName");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			stanzaLabel = new JLabel();
			stanzaLabel.setText("JAAS Stanza");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			uriLabel = new JLabel();
			uriLabel.setText("CE URI");
			parameterPanel = new JPanel();
			parameterPanel.setLayout(new GridBagLayout());
			parameterPanel.setBorder(BorderFactory.createTitledBorder(null, "Connection Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			parameterPanel.add(uriLabel, gridBagConstraints);
			parameterPanel.add(getUriTextField(), gridBagConstraints1);
			parameterPanel.add(stanzaLabel, gridBagConstraints2);
			parameterPanel.add(getStanzaTextField(), gridBagConstraints3);
			parameterPanel.add(userLabel, gridBagConstraints4);
			parameterPanel.add(getUserTextField(), gridBagConstraints5);
			parameterPanel.add(passLabel, gridBagConstraints6);
			parameterPanel.add(getPassTextField(), gridBagConstraints7);
			parameterPanel.add(getConnectButton(), gridBagConstraints8);
			parameterPanel.add(getClearButton(), gridBagConstraints31);
			parameterPanel.add(domainLabel, gridBagConstraints61);
			parameterPanel.add(getDomainTextField(), gridBagConstraints71);
			parameterPanel.add(osLabel, gridBagConstraints81);
			parameterPanel.add(getOsList(), gridBagConstraints9);
		}
		return parameterPanel;
	}

	/**
	 * This method initializes uriTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUriTextField() 
	{
		if (uriTextField == null) 
		{
			uriTextField = new JTextField();
		}
		return uriTextField;
	}

	/**
	 * This method initializes stanzaTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getStanzaTextField() 
	{
		if (stanzaTextField == null) 
		{
			stanzaTextField = new JTextField();
		}
		return stanzaTextField;
	}

	/**
	 * This method initializes userTextField.
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUserTextField() 
	{
		if (userTextField == null) 
		{
			userTextField = new JTextField();
		}
		return userTextField;
	}

	/**
	 * This method initializes passTextField.
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPassTextField() 
	{
		if (passTextField == null) 
		{
			passTextField = new JTextField();
		}
		return passTextField;
	}

	/**
	 * This method initializes connectButton.	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getConnectButton() 
	{
		if (connectButton == null) 
		{
			connectButton = new JButton();
			connectButton.setText("Test Connection");
			connectButton.addActionListener(new java.awt.event.ActionListener()
	        {
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	                connectButtonActionPerformed(evt);
	            }
	        });
		}
		return connectButton;
	}

	/**
	 * This method initializes domainTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDomainTextField() 
	{
		if (domainTextField == null) 
		{
			domainTextField = new JTextField();
		}
		return domainTextField;
	}

	/**
	 * This method initializes osList.	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getOsList() 
	{
		if (osList == null) 
		{
			osList = new JList();
		}
		return osList;
	}

	/**
	 * This method initializes clearButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getClearButton() 
	{
		if (clearButton == null) 
		{
			clearButton = new JButton();
			clearButton.setText("Clear");
			clearButton.addActionListener(new java.awt.event.ActionListener()
	        {
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	                clearButtonActionPerformed(evt);
	            }
	        });
		}
		return clearButton;
	}

	/*
	 * Action performed on clicking Clear button.
	 * It clears all the textfields.
	 */
	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
		connectButton.setEnabled(true);
		uriTextField.setText("");
        stanzaTextField.setText("");
        userTextField.setText("");
        passTextField.setText("");
        statusLabel.setText("Test connection to CE");
    }
    
    /*
     * Action performed on clicking Connect button.
     * It does the JAAS login.
     */
	private void connectButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        try
        {
            String uri = uriTextField.getText();
            String stanza = stanzaTextField.getText();
            String user = userTextField.getText();
            String pass = passTextField.getText();
            if(checkRequireFields()== true)
            {
            	ce.establishConnection(user,pass,stanza,uri);
            	statusLabel.setText("Connection to CE successful");
            	domainTextField.setText(ce.getDomainName());
            	osnames = ce.getOSNames();
            	osList.setListData(osnames);
            	connectButton.setEnabled(false);
            }
        }
        catch (EngineRuntimeException e)
        {
        	if(e.getExceptionCode() == ExceptionCode.E_NOT_AUTHENTICATED)
        	{
        		statusLabel.setText("Invalid login credentials supplied - please try again");
        	}
        	else if(e.getExceptionCode() == ExceptionCode.API_UNABLE_TO_USE_CONNECTION)
        	{
        		statusLabel.setText("Unable to connect to server.  Please check to see that URL is correct and server is running");
        	}
        	else
        	{
        		statusLabel.setText(e.getMessage());
        	}
            e.printStackTrace();
        }
    }
    
    /*
     * Checks whether all require fields are filled before 
     * performing any action.
     */
    private boolean checkRequireFields()
    {
    	boolean con = true;
    	if(uriTextField.getText().equals(""))
    	{
    		statusLabel.setText("URI field can not be empty");
    		con = false;
    	}
    	else if(stanzaTextField.getText().equals(""))
    	{
    		statusLabel.setText("JAAS Stanza field can not be empty");
    		con = false;
    	}
    	else if(userTextField.getText().equals(""))
    	{
    		statusLabel.setText("UserName field can not be empty");
    		con = false;
    	}
    	else if(passTextField.getText().equals(""))
    	{
    		statusLabel.setText("Password field can not be empty");
    		con = false;
    	}
    	return con;
    }
}
