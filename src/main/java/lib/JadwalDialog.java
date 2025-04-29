package lib;

import javax.swing.*;
import java.awt.*;

class JadwalDialog extends JDialog {
    private final JTextField nrpField = new JTextField(20);
    private final JComboBox<String> hariCombo = new JComboBox<>(
            new String[] { "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu" });
    private final JTextField shiftField = new JTextField(20);
    private final JTextField jamMasukField = new JTextField(20); // format HH:MM:SS
    private final JTextField jamKeluarField = new JTextField(20);
    private boolean confirmed = false;

    public JadwalDialog(JadwalData data) {
        setTitle(data == null ? "Tambah Jadwal" : "Edit Jadwal");
        setModal(true);
        setLayout(new GridLayout(6, 2, 5, 5));
        setSize(300, 250);
        setLocationRelativeTo(null);

        add(new JLabel("NRP:"));
        add(nrpField);

        add(new JLabel("Hari:"));
        add(hariCombo);

        add(new JLabel("Shift:"));
        add(shiftField);

        add(new JLabel("Jam Masuk (HH:MM:SS):"));
        add(jamMasukField);

        add(new JLabel("Jam Keluar (HH:MM:SS):"));
        add(jamKeluarField);

        JButton okButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");
        add(okButton);
        add(cancelButton);

        if (data != null) {
            nrpField.setText(data.nrp());
            hariCombo.setSelectedItem(data.hari());
            shiftField.setText(data.shift());
            jamMasukField.setText(data.jamMasuk());
            jamKeluarField.setText(data.jamKeluar());
        }

        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getNrp() {
        return nrpField.getText();
    }

    public String getHari() {
        return (String) hariCombo.getSelectedItem();
    }

    public String getShift() {
        return shiftField.getText();
    }

    public String getJamMasuk() {
        return jamMasukField.getText();
    }

    public String getJamKeluar() {
        return jamKeluarField.getText();
    }
}

record JadwalData(String nrp, String hari, String shift, String jamMasuk, String jamKeluar) {
}
