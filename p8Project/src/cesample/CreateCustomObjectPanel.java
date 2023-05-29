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
import java.awt.Insets;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.exception.EngineRuntimeException;

/**
 * User Interface for creating a CustomObject.
 */
public class CreateCustomObjectPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JPanel parameterPanel = null;
	private JRadioButton osRadioButton = null;
	private JComboBox osComboBox = null;
	private JLabel classLabel = null;
	private JTextField classTextField = null;
	private JButton createButton = null;
	private JCheckBox fileCheckBox = null;
	private JTextField fileTextField = null;
	private JPanel resultPanel = null;
	private JLabel statusLabel = null;
	private JButton clearButton = null;
	private Vector osnames = null;
    private CEConnection ce = null;

	/**
	 * This is the default constructor.
	 */
	public CreateCustomObjectPanel(CEConnection c)
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
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getResultPanel(), BorderLayout.SOUTH);
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
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.CENTER;
			gridBagConstraints8.gridy = 3;
			gridBagConstraints8.weightx = 0.0;
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.gridx = 0;
			classLabel = new JLabel();
			classLabel.setText("Object Class");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			parameterPanel = new JPanel();
			parameterPanel.setLayout(new GridBagLayout());
			parameterPanel.setBorder(BorderFactory.createTitledBorder(null, "CustomObject Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			parameterPanel.add(getOsRadioButton(), gridBagConstraints);
			parameterPanel.add(getOsComboBox(), gridBagConstraints1);
			parameterPanel.add(classLabel, gridBagConstraints6);
			parameterPanel.add(getClassTextField(), gridBagConstraints7);
			parameterPanel.add(getCreateButton(), gridBagConstraints8);
			parameterPanel.add(getFileCheckBox(), gridBagConstraints2);
			parameterPanel.add(getFileTextField(), gridBagConstraints3);
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
	 * This method initializes classTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getClassTextField()
	{
		if (classTextField == null)
		{
			classTextField = new JTextField();
			classTextField.setText("CustomObject");
		}
		return classTextField;
	}

	/**
	 * This method initializes createButton.	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCreateButton()
	{
		if (createButton == null)
		{
			createButton = new JButton();
			createButton.setText("Create");
			createButton.addActionListener(new java.awt.event.ActionListener()
	        {
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	                createButtonActionPerformed(evt);
	            }
	        });
		}
		return createButton;
	}

	/**
	 * This method initializes fileCheckBox.	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getFileCheckBox()
	{
		if (fileCheckBox == null)
		{
			fileCheckBox = new JCheckBox();
			fileCheckBox.setText("File in Folder");
		}
		return fileCheckBox;
	}

	/**
	 * This method initializes fileTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFileTextField()
	{
		if (fileTextField == null)
		{
			fileTextField = new JTextField();
		}
		return fileTextField;
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
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.CENTER;
			gridBagConstraints10.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.ipadx = 0;
			gridBagConstraints10.ipady = 0;
			gridBagConstraints10.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.ipadx = 0;
			gridBagConstraints9.weightx = 2.0;
			gridBagConstraints9.gridheight = 1;
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			statusLabel = new JLabel();
			statusLabel.setText("Create Custom Object");
			resultPanel = new JPanel();
			resultPanel.setLayout(new GridBagLayout());
			resultPanel.add(statusLabel, gridBagConstraints9);
			resultPanel.add(getClearButton(), gridBagConstraints10);
		}
		return resultPanel;
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
     * Clears all textfields when user clicks Clear button.
     */
	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        classTextField.setText("");
        fileTextField.setText("");
        statusLabel.setText("Create Custom Object");
        fileCheckBox.setSelected(false);
    }
    
    /*
     * Creates the CustomObject when user clicks Create button. 
     * Created CustomObject can also be filed.
     */
	private void createButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        try
        {
        	if(checkRequireFields() == true)
        	{
        		String osname = (String) osComboBox.getSelectedItem();
        		String className = classTextField.getText();
        		ObjectStore os = ce.fetchOS(osname);
        		CustomObject co = CEUtil.createCustomObject(os, className);
        		co.save(RefreshMode.REFRESH);
        		String id = co.get_Name();
        		statusLabel.setText("Custom Object is created with Id " + id);
        		if(fileCheckBox.isSelected())
        		{
        			ReferentialContainmentRelationship rcr = CEUtil.fileObject(os,co,fileTextField.getText());
        			rcr.save(RefreshMode.REFRESH);
        			statusLabel.setText("Custom Object is created and filed with Id " + id);
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
    	else
    	{
    		if (fileCheckBox.isSelected())
    		{
    			if(fileTextField.getText().equals(""))
    			{
    				statusLabel.setText("Enter folder name");
    				con = false;
    			}
    		}
    	}
    	return con;
    }
}
