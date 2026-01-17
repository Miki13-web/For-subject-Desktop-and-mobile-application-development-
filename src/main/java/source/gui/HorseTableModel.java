package source.gui;

import java.util.List;
import source.Horse;

public class HorseTableModel extends CustomTableModel<Horse> {

    public HorseTableModel(List<Horse> rows){
        super(rows, new String[]{"Name", "Breed", "Age", "Status", "Price", "Ratings", "Avg Rating"});
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Horse horse = rows.get(rowIndex);
        switch(columnIndex){
            case 0: return horse.getName();
            case 1: return horse.getBreed();
            case 2: return horse.getAge();
            case 3: return horse.getStatus();
            case 4: return horse.getPrice();
            case 5: return horse.getRatingsCount();
            case 6: return String.format("%.2f", horse.getAverageRating());
            default: return null;
        }
    }
}