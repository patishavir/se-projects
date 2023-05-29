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
import java.io.File;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.exception.EngineRuntimeException;

/**
 * User Interface for creating a Document.
 */
public class CreateDocPanel extends JPanel 
{

	private static final long serialVersionUID = 1L;
	private JRadioButton osRadioButton = null;
	private JComboBox osComboBox = null;
	private JLabel titleLabel = null;
	private JCheckBox contentCheckBox = null;
	private JTextField titleTextField = null;
	private JTextField contentTextField = null;
	private JButton browseButton = null;
	private JLabel mimeLabel = null;
	private JTextField mimeTextField = null;
	private JLabel classLabel = null;
	private JTextField classTextField = null;
	private JCheckBox fileCheckBox = null;
	private JTextField fileTextField = null;
	private JCheckBox checkinCheckBox = null;
	private JPanel parameterPanel = null;
	private JPanel resultPanel = null;
	private JButton clearButton = null;
	private JButton createButton = null;
	private Vector osnames = null;
    private CEConnection ce = null;
	private JLabel statusLabel = null;

	/**
	 * This is the default constructor.
	 */
	public CreateDocPanel(CEConnection c) 
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
		BorderLayout borderLayout = new BorderLayout();
		this.setLayout(borderLayout);
		this.setSize(413, 265);
		this.add(getResultPanel(), BorderLayout.SOUTH);
		this.add(getParameterPanel(), BorderLayout.CENTER);
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
	 * This method initializes contentCheckBox.	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getContentCheckBox() 
	{
		if (contentCheckBox == null) 
		{
			contentCheckBox = new JCheckBox();
			contentCheckBox.setText("Include Content from location");
		}
		return contentCheckBox;
	}

	/**
	 * This method initializes titleTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTitleTextField() 
	{
		if (titleTextField == null) 
		{
			titleTextField = new JTextField();
		}
		return titleTextField;
	}

	/**
	 * This method initializes contentTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getContentTextField() 
	{
		if (contentTextField == null) 
		{
			contentTextField = new JTextField();
		}
		return contentTextField;
	}

	/**
	 * This method initializes browseButton	.
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBrowseButton() 
	{
		if (browseButton == null) 
		{
			browseButton = new JButton();
			browseButton.setText("Browse");
			browseButton.addActionListener(new java.awt.event.ActionListener()
	        {
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	                browseButtonActionPerformed(evt);
	            }
	        });
		}
		return browseButton;
	}

	/**
	 * This method initializes mimeTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getMimeTextField() 
	{
		if (mimeTextField == null) 
		{
			mimeTextField = new JTextField();
			mimeTextField.setText("text/plain");
		}
		return mimeTextField;
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
			classTextField.setText("Document");
		}
		return classTextField;
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
	 * This method initializes checkinCheckBox.	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCheckinCheckBox() 
	{
		if (checkinCheckBox == null) 
		{
			checkinCheckBox = new JCheckBox();
			checkinCheckBox.setText("Check-in");
		}
		return checkinCheckBox;
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
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 1;
			gridBagConstraints14.gridy = 7;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 7;
			gridBagConstraints13.gridx = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridy = 6;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 6;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.BOTH;
			gridBagConstraints10.gridy = 5;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 5;
			gridBagConstraints9.gridx = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.gridy = 4;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.gridy = 4;
			gridBagConstraints7.gridx = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 3;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.gridx = 0;
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
			parameterPanel.setBorder(BorderFactory.createTitledBorder(null, "Document Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			parameterPanel.add(getOsRadioButton(), gridBagConstraints);
			parameterPanel.add(getOsComboBox(), gridBagConstraints1);
			parameterPanel.add(getTitleLabel(), gridBagConstraints2);
			parameterPanel.add(getTitleTextField(), gridBagConstraints4);
			parameterPanel.add(getContentCheckBox(), gridBagConstraints3);
			parameterPanel.add(getContentTextField(), gridBagConstraints5);
			parameterPanel.add(getBrowseButton(), gridBagConstraints6);
			parameterPanel.add(getMimeLabel(), gridBagConstraints7);
			parameterPanel.add(getMimeTextField(), gridBagConstraints8);
			parameterPanel.add(getClassLabel(), gridBagConstraints9);
			parameterPanel.add(getClassTextField(), gridBagConstraints10);
			parameterPanel.add(getFileCheckBox(), gridBagConstraints11);
			parameterPanel.add(getFileTextField(), gridBagConstraints12);
			parameterPanel.add(getCheckinCheckBox(), gridBagConstraints13);
			parameterPanel.add(getCreateButton(), gridBagConstraints14);
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
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			resultPanel = new JPanel();
			resultPanel.setLayout(new GridBagLayout());
			statusLabel = new JLabel();
			statusLabel.setText("Create Document");
			resultPanel.add(statusLabel,gridBagConstraints2);
			resultPanel.add(getClearButton(),gridBagConstraints1);
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
	 * This method initializes classLabel.	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	 private JLabel getClassLabel() 
	{
		if (classLabel == null)
		{
			classLabel = new JLabel();
			classLabel.setText("Document Class");
		}
		return classLabel;
	}

	/**
	 * This method initializes mimeLabel.	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	 private JLabel getMimeLabel() {
		if (mimeLabel == null)
		{
			mimeLabel = new JLabel();
			mimeLabel.setText("Mime Type");
		}
		return mimeLabel;
	}

	/**
	 * This method initializes titleLabel.	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	 private JLabel getTitleLabel() {
		if (titleLabel == null)
		{
			titleLabel = new JLabel();
			titleLabel.setText("DocumentTitle");
		}
		return titleLabel;
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
	 * Action performed on clicking Create button.
	 * It creates the Document object with or without
	 * the content. Content can be included from any
	 * file on the file system. Created Document can be
	 * filed and checked in.
	 */
	private void createButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        try
        {
        	if(checkRequireFields() == true)
        	{
        		String fPath = (String) fileTextField.getText();
        		ObjectStore os = ce.fetchOS((String) osComboBox.getSelectedItem());
        		File f = new File(contentTextField.getText());
        		String conType = mimeTextField.getText();
        		String docTitle = titleTextField.getText();
        		String docClass = classTextField.getText();
        		Document doc;
        		if(contentCheckBox.isSelected())
        		{
        			doc = CEUtil.createDocWithContent(f, conType, os, docTitle, docClass);
        			doc.save(RefreshMode.REFRESH);
        			statusLabel.setText("Document saved with content");
        		}
        		else
        		{
        			doc = CEUtil.createDocNoContent(conType, os, docTitle, docClass);
        			doc.save(RefreshMode.REFRESH);
        			statusLabel.setText("Document saved");
        		}
        		if(fileCheckBox.isSelected())
        		{
        			ReferentialContainmentRelationship rcr = CEUtil.fileObject(os, doc, fPath);
        			rcr.save(RefreshMode.REFRESH);
        			statusLabel.setText("Document filed");
        		}
        		if(checkinCheckBox.isSelected())
        		{
        			CEUtil.checkinDoc(doc);
        			statusLabel.setText("Document checked in");
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
     * Action performed on clicking Browse button. 
     * It lets user select file from file system.
     * File content can be included in Document 
     * object.
     */
	private void browseButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        File f = fc.getSelectedFile();
        contentTextField.setText(f.getAbsolutePath());
    }
    
    /*
     * Action performed on clicking Clear button.
     * It clears all textfields.
     */
	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        titleTextField.setText("");
        contentTextField.setText("");
        fileTextField.setText("");
        statusLabel.setText("Create Document");
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
        }
    	else
    	{
    		if(contentCheckBox.isSelected())
    		{
    			if(contentTextField.getText().equals(""))
    			{
    				statusLabel.setText("Select file for content");
    				con = false;
    			}
    		}
    		else if(fileCheckBox.isSelected())
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
}  //  @jve:decl-index=0:visual-constraint="24,20"
