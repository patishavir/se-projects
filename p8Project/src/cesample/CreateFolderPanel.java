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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;

/**
 * User Interface for creating a Folder.
 */
public class CreateFolderPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JPanel parameterPanel = null;
	private JRadioButton osRadioButton = null;
	private JComboBox osComboBox = null;
	private JLabel nameLabel = null;
	private JTextField nameTextField = null;
	private JLabel parentLabel = null;
	private JTextField parentTextField = null;
	private JLabel classLabel = null;
	private JTextField classTextField = null;
	private JButton createButton = null;
	private Vector osnames = null;
    private CEConnection ce = null;
	private JPanel resultPanel = null;
	private JLabel statusLabel = null;
	private JButton clearButton = null;
	
	/**
	 * This is the default constructor.
	 */
	public CreateFolderPanel(CEConnection c)
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
		this.setLayout(new BorderLayout());
		this.setSize(302, 200);
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
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.anchor = GridBagConstraints.CENTER;
			gridBagConstraints8.weightx = 0.0;
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
			classLabel = new JLabel();
			classLabel.setText("Folder Class");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 2;
			parentLabel = new JLabel();
			parentLabel.setText("Parent Folder");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			nameLabel = new JLabel();
			nameLabel.setText("Name");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			parameterPanel = new JPanel();
			parameterPanel.setLayout(new GridBagLayout());
			parameterPanel.setBorder(BorderFactory.createTitledBorder(null, "Folder Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			parameterPanel.add(getOsRadioButton(), gridBagConstraints);
			parameterPanel.add(getOsComboBox(), gridBagConstraints1);
			parameterPanel.add(nameLabel, gridBagConstraints2);
			parameterPanel.add(getNameTextField(), gridBagConstraints3);
			parameterPanel.add(parentLabel, gridBagConstraints4);
			parameterPanel.add(getParentTextField(), gridBagConstraints5);
			parameterPanel.add(classLabel, gridBagConstraints6);
			parameterPanel.add(getClassTextField(), gridBagConstraints7);
			parameterPanel.add(getCreateButton(), gridBagConstraints8);
		}
		return parameterPanel;
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
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			statusLabel = new JLabel();
			statusLabel.setText("Create Folder");
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
	 * This method initializes nameTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNameTextField()
	{
		if (nameTextField == null)
		{
			nameTextField = new JTextField();
		}
		return nameTextField;
	}

	/**
	 * This method initializes parentTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getParentTextField()
	{
		if (parentTextField == null)
		{
			parentTextField = new JTextField();
		}
		return parentTextField;
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
			classTextField.setText("Folder");
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

	/*
	 * Clears the textfields when user clicks Clear button.
	 */
	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        nameTextField.setText("");
        parentTextField.setText("");
        statusLabel.setText("Create Folder");
    }
    
    /*
     * Creates the folder at supplied path with supplied name when user clicks Create button.
     */
	private void createButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        try
        {
        	if(checkRequireFields() == true)
        	{
        		String osname = (String) osComboBox.getSelectedItem();
        		String name = nameTextField.getText();
        		String path = parentTextField.getText();
        		String className = classTextField.getText();
        		ObjectStore os = ce.fetchOS(osname);
        		CEUtil.createFolder(os,path,name,className);
        		statusLabel.setText("Folder Created");
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
    		if(nameTextField.getText().equals(""))
    		{
    			statusLabel.setText("Enter folder name");
    			con = false;
    		}
    		else if(parentTextField.getText().equals(""))
    		{
    			statusLabel.setText("Enter parent folder");
    			con = false;
    		}
    	}
    	return con;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
