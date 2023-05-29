package jtablelistener;
 
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 
import javax.swing.JOptionPane;
import javax.swing.JTable;
 
/**
 * A mouse listener class which is used to handle mouse clicking event
 * on column headers of a JTable.
 * @author www.codejava.net
 *
 */
public class TableHeaderMouseListener extends MouseAdapter {
     
    private JTable table;
     
    public TableHeaderMouseListener(JTable table) {
        this.table = table;
    }
     
    public void mouseClicked(MouseEvent event) {
        Point point = event.getPoint();
        int column = table.columnAtPoint(point);
         
        JOptionPane.showMessageDialog(table, "Column header #" + column + " is clicked");
         
        // do your real thing here...
    }
}