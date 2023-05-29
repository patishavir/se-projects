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
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridLayout;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JCheckBox;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.query.RepositoryRow;

import java.util.Iterator;
import java.util.Vector;

/**
 * User Interface for making SQL queries to 
 * Content Engine
 */
public class AdHocQueryPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JPanel resultPanel = null;
	private JLabel statusLabel = null;
	private JButton clearButton = null;
	private JScrollPane tableScrollPane = null;
	private JTable resultsTable = null;
	private JPanel parameterPanel = null;
	private JRadioButton osRadioButton = null;
	private JComboBox osComboBox = null;
	private JTextField selectTextField = null;
	private JButton queryButton = null;
	private JLabel selectLabel = null;
	private JLabel whereLabel = null;
	private JTextField whereTextField = null;
	private JLabel fromLabel = null;
	private JTextField fromTextField = null;
	private JCheckBox maxCheckBox = null;
	private JTextField maxTextField = null;
	private Vector osnames = null;
    private CEConnection ce = null;
   
	/**
	 * This is the default constructor.
	 */
	public AdHocQueryPanel(CEConnection c)
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
		this.setSize(383, 346);
		this.add(getParameterPanel(), null);
		this.add(getResultPanel(), null);
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.ipadx = 126;
			gridBagConstraints9.gridx = 0;
			statusLabel = new JLabel();
			statusLabel.setText("Query");
			resultPanel = new JPanel();
			resultPanel.setLayout(new GridBagLayout());
			resultPanel.add(statusLabel, gridBagConstraints9);
			resultPanel.add(getClearButton(), gridBagConstraints11);
			resultPanel.add(getTableScrollPane(), gridBagConstraints);
		}
		return resultPanel;
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
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints13.gridy = 4;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.ipadx = 40;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridy = 4;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 2;
			fromLabel = new JLabel();
			fromLabel.setText("FROM");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 3;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 3;
			whereLabel = new JLabel();
			whereLabel.setText("WHERE");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.gridx = 0;
			selectLabel = new JLabel();
			selectLabel.setText("SELECT");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 5;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridy = 0;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 0;
			parameterPanel = new JPanel();
			parameterPanel.setLayout(new GridBagLayout());
			parameterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), "Query Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			parameterPanel.add(getOsRadioButton(), gridBagConstraints1);
			parameterPanel.add(getOsComboBox(), gridBagConstraints12);
			parameterPanel.add(getSelectTextField(), gridBagConstraints5);
			parameterPanel.add(getQueryButton(), gridBagConstraints10);
			parameterPanel.add(selectLabel, gridBagConstraints2);
			parameterPanel.add(whereLabel, gridBagConstraints3);
			parameterPanel.add(getWhereTextField(), gridBagConstraints4);
			parameterPanel.add(fromLabel, gridBagConstraints6);
			parameterPanel.add(getFromTextField(), gridBagConstraints7);
			parameterPanel.add(getMaxCheckBox(), gridBagConstraints8);
			parameterPanel.add(getMaxTextField(), gridBagConstraints13);
		}
		return parameterPanel;
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
	 * This method initializes tableScrollPane.	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTableScrollPane()
	{
		if (tableScrollPane == null)
		{
			tableScrollPane = new JScrollPane();
			tableScrollPane.setViewportView(getResultsTable());
		}
		return tableScrollPane;
	}

	/**
	 * This method initializes resultsTable.	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getResultsTable()
	{
		if (resultsTable == null)
		{
			resultsTable = new JTable();
			resultsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		}
		return resultsTable;
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
	 * This method initializes selectTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSelectTextField()
	{
		if (selectTextField == null)
		{
			selectTextField = new JTextField();
		}
		return selectTextField;
	}

	/**
	 * This method initializes queryButton.	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getQueryButton()
	{
		if (queryButton == null)
		{
			queryButton = new JButton();
			queryButton.setText("Query");
			queryButton.addActionListener(new java.awt.event.ActionListener()
	        {
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	                queryButtonActionPerformed(evt);
	            }
	        });
		}
		return queryButton;
	}

	/**
	 * This method initializes whereTextField.	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getWhereTextField()
	{
		if (whereTextField == null)
		{
			whereTextField = new JTextField();
		}
		return whereTextField;
	}

	/**
	 * This method initializes fromTextField	.
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFromTextField()
	{
		if (fromTextField == null)
		{
			fromTextField = new JTextField();
		}
		return fromTextField;
	}

	/**
	 * This method initializes maxCheckBox.	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getMaxCheckBox()
	{
		if (maxCheckBox == null)
		{
			maxCheckBox = new JCheckBox();
			maxCheckBox.setText("Max Rows");
		}
		return maxCheckBox;
	}

	/**
	 * This method initializes maxTextField.
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getMaxTextField()
	{
		if (maxTextField == null)
		{
			maxTextField = new JTextField();
		}
		return maxTextField;
	}

	/*
	 * Queries the Content Engine and displays the
	 * returned result when user clicks the Query button.
	 */
	private void queryButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        try
        {
        	if(checkRequireFields()== true)
        	{
        		String osname = (String) osComboBox.getSelectedItem();
        		ObjectStore os = ce.fetchOS(osname);
        		Vector rows = new Vector();
        		Vector columns = new Vector();
        		int mRows = 0;
        		String select = selectTextField.getText();
        		String from = fromTextField.getText();
        		String where = "";
        		if (!(whereTextField.getText().equals("")))
        		{
        			where = whereTextField.getText();
        		}
        		if(maxCheckBox.isSelected())
        		{
        			mRows = Integer.parseInt(maxTextField.getText());
        		}
        		RepositoryRowSet rrs = CEUtil.fetchResultsRowSet(os, select, from, where, mRows);
        		Iterator it = rrs.iterator();
        		boolean firstPass = true;
        		while(it.hasNext())
        		{
        			RepositoryRow rr = (RepositoryRow) it.next();
        			if(firstPass)
        			{
        				columns = CEUtil.getResultProperties(rr);
        				for(int i = 0; i < columns.size(); i++)
        				{
        					((DefaultTableModel) resultsTable.getModel()).addColumn(columns.get(i));
        				}
        				firstPass = false;
        			}
        			rows = CEUtil.getResultRow(rr);
        			((DefaultTableModel) resultsTable.getModel()).addRow(rows);
        		}
        		statusLabel.setText("Query executed");
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
     * Clears the result table when user clicks Clear button.
     */
    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        ((DefaultTableModel) resultsTable.getModel()).setRowCount(0);
        ((DefaultTableModel) resultsTable.getModel()).setColumnCount(0);
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
    		if(selectTextField.getText().equals(""))
    		{
    			statusLabel.setText("Select field can not be empty");
    			con = false;
    		}
    		else if(fromTextField.getText().equals(""))
    		{
    			statusLabel.setText("From field can not be empty");
    			con = false;
    		}
    		else if(maxCheckBox.isSelected())
    		{
    			if(maxTextField.getText().equals(""))
    			{
    				statusLabel.setText("Enter max rows");
    				con = false;
    			}
    		}
    	}
    	return con;
    }
}  //  @jve:decl-index=0:visual-constraint="18,1"
