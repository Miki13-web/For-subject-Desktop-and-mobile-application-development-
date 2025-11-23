package source.gui;

import source.Stable;
import java.util.List;

public class StableTableModel extends CustomTableModel<Stable>{
    public StableTableModel(List<Stable> rows){
        super(rows, new String[]{"Stable name", "Capacity", "Occupancy"});
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Stable stable = rows.get(rowIndex);
        switch(columnIndex){
            case 0: return stable.getStableName();
            case 1:
                return stable.getMaxCapacity();
            case 2:
                double occupancy = (double) stable.getHorseList().size() / stable.getMaxCapacity()*100;
                return String.format("%.1f%%", occupancy);
            default: return null;
        }
    }
}
