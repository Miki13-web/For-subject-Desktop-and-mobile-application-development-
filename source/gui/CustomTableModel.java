package source.gui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;

//abstract generic model based on JTable
public abstract class CustomTableModel<T> extends AbstractTableModel {
    protected List<T> rows;
    protected String[] columnNames;

    //constructor
    public CustomTableModel(List<T> rows, String[] columnNames){
        this.rows = rows;
        this.columnNames = columnNames;
    }
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    @Override
    public int getRowCount() {
        return rows.size();
    }
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public T getObjectAt(int row) {
        return rows.get(row);
    }

    public void setData(List<T> data) {
        this.rows = data;
        fireTableDataChanged();//redraw JTable
    }
    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);
}
