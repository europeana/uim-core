package eu.europeana.uim.gui.gwt.client;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;

import java.util.List;

/**
 * Utility methods for building CellTables
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class CellTableUtils {
    public static <C, T> void addColumn(CellTable<T> table, Cell<C> cell, String headerText, final GetValue<C, T> getter) {
        Column<T, C> column = new Column<T, C>(cell) {
            @Override
            public C getValue(T object) {
                return getter.getValue(object);
            }
        };
        table.addColumn(column, headerText);
    }

    // helper methods for the collections cellTable
    public static <T> void updateCellTableData(CellTable<T> table, List<T> data) {
        table.setRowData(0, data);
        table.setRowCount(data.size());
    }

    public static interface GetValue<C, T> {
        C getValue(T object);
    }
}
