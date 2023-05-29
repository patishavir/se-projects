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

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.filenet.api.core.ComponentRelationship;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Vector;

/**
 * User Interface for retrieving a CompoundDocument.
 */
public class GetCompoundDocPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JPanel parameterPanel = null;
	private JRadioButton osRadioButton = null;
	private JComboBox osComboBox = null;
	private JTextField idTextField = null;
	private JButton getButton = null;
	private JLabel idLabel = null;
	private JPanel resultPanel = null;
	private JLabel statusLabel = null;
	private JTextArea resultTextArea = null;
	private JButton clearButton = null;
	private Vector osnames = null;
    private CEConnection ce = null;

	/**
	 * This is the default constructor.
	 */
	public GetCompoundDocPanel(CEConnection c)
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
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(2);
		this.setLayout(gridLayout);
		this.setSize(300, 200);
		this.add(getParameterPanel(), null);
		this.add(getResultPanel(), null);
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
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			idLabel = new JLabel();
			idLabel.setText("Specify ID");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			parameterPanel = new JPanel();
			parameterPanel.setLayout(new GridBagLayout());
			parameterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), "Compound Document Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			parameterPanel.add(getOsRadioButton(), gridBagConstraints);
			parameterPanel.add(getOsComboBox(), gridBagConstraints1);
			parameterPanel.add(getIdTextField(), gridBagConstraints5);
			parameterPanel.add(getGetButton(), gridBagConstraints10);
			parameterPanel.add(idLabel, gridBagConstraints2);
		}
		return parameterPanel;
	}

	/**
	 * This method initializes osRadioButton.	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getOsRadioButton()
	{
		if (osRadioButton == null)
		{
			osRadioButton = new JRadioButton();
			osRadioButton.setText("Select Object Store");
			osRadioButton.addActionListener(new java.awt.event.ActionListener()
	        {
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	                osRadioButtonActionPerformed(evt);
	            }
	        });
		}
		return osRadioButton;
	}

	/**
	 * This method initializes osComboBox.	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getOsComboBox()
	{
		if (osComboBox == null)
		{
			osComboBox = new JComboBox();
		}
		return osComboBox;
	}

	/**
	 * This method initializes idTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getIdTextField()
	{
		if (idTextField == null)
		{
			idTextField = new JTextField();
		}
		return idTextField;
	}

	/**
	 * This method initializes getButton.	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getGetButton()
	{
		if (getButton == null)
		{
			getButton = new JButton();
			getButton.setText("Get");
			getButton.addActionListener(new java.awt.event.ActionListener()
	        {
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	                getButtonActionPerformed(evt);
	            }
	        });
		}
		return getButton;
	}

	/**
	 * This method initializes resultPanel.	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResultPanel()
	{
		if (resultPanel == null)
		{
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 2;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.ipadx = 206;
			gridBagConstraints12.ipady = 84;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.weighty = 1.0;
			gridBagConstraints12.gridx = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.ipadx = 126;
			gridBagConstraints9.gridx = 0;
			statusLabel = new JLabel();
			statusLabel.setText("Get Compound Doc");
			resultPanel = new JPanel();
			resultPanel.setLayout(new GridBagLayout());
			resultPanel.add(statusLabel, gridBagConstraints9);
			resultPanel.add(getResultTextArea(), gridBagConstraints12);
			resultPanel.add(getClearButton(), gridBagConstraints11);
		}
		return resultPanel;
	}

	/**
	 * This method initializes resultTextArea.	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getResultTextArea()
	{
		if (resultTextArea == null)
		{
			resultTextArea = new JTextArea();
		}
		return resultTextArea;
	}

	/**
	 * This method initializes clearButton.	
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
     * Clears the textfield and textarea when user clicks Clear button.
     */
	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        idTextField.setText("");
        resultTextArea.setText("");
        statusLabel.setText("Get Compound Document");
    }
    
    /*
     * Populates the osComboBox with available
     * object store names when user selects osRadioButton.
     */
    private void osRadioButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        if(ce.isConnected())
        {
            osnames = ce.getOSNames();
            osComboBox.removeAllItems();
            for(int i = 0; i < osnames.size(); i++)
            {
                osComboBox.addItem(osnames.get(i));
            }
        }
    }
    
    /*
     * Retrieves the CompoundDocument object and displays it's properties
     * when user clicks Get button. CompoundDocument is specified by id.
     */
    private void getButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        try
        {
        	if(checkRequireFields() == true)
        	{
        		String osname = (String)osComboBox.getSelectedItem();
        		ObjectStore os = ce.fetchOS(osname);
        		String id = idTextField.getText();
        		ComponentRelationship cr = CEUtil.fetchComponentRelationship(os, id);
        		statusLabel.setText("Component Relationship object fetched.");
        		HashMap hm = CEUtil.getComponentRelationshipObjectProperties(cr);
        		Object[] names = hm.keySet().toArray();

        		resultTextArea.setText("");
        		for(int i = 0; i < hm.size(); i++)
        		{
        			resultTextArea.append((String)names[i]+ ":" +hm.get((String)names[i])+ '\n');
        		}
        	}
        }
        catch (EngineRuntimeException e)
        {
        	statusLabel.setText(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /*
     * Checks whether all required fields are filled before 
     * performing any action.
     */
    private boolean checkRequireFields()
    {
    	boolean con = true;
    	if(!osRadioButton.isSelected())
        {
        	statusLabel.setText("Select Object Store");
        	con = false;
        }
    	else if(idTextField.getText().equals(""))
    	{
    		statusLabel.setText("Enter ID");
    		con = false;
    	}
    	return con;
    }
}
