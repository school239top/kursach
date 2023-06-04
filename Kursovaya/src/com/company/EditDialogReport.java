package com.company;

import javax.swing.*;

public class EditDialogReport extends DialogReport {


    @Override
    public void progress(report parent) {
        setVisible(false);
        int row = parent.dataReport.getSelectedRow();
        parent.dataReport.setValueAt(name.getSelectedItem().toString(), row, 0);
        parent.dataReport.setValueAt(kolvo.getText(), row, 1);
        parent.makeXml();
    }

    @Override
    public void init(report parent) {
        int row = parent.dataReport.getSelectedRow();
        name = new JComboBox();
        name.addItem(parent.dataReport.getValueAt(row, 0).toString());
        name.setEnabled(false);
        kolvo = new JTextField(parent.dataReport.getValueAt(row, 1).toString(), 20);
        checker(1,kolvo);
    }

    public EditDialogReport(JFrame owner, report parent, String title){
        super(owner,parent, title);
    };
}