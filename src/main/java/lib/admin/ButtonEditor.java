package lib.admin;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean clicked;
    private PersetujuanCuti parent;
    private int selectedRow;

    public ButtonEditor(JCheckBox checkBox, PersetujuanCuti parent) {
        super(checkBox);
        this.parent = parent;
        button = new JButton();
        button.setOpaque(true);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        clicked = true;
        selectedRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            parent.lihatDetail(selectedRow);
        }
        clicked = false;
        return label;
    }
}
