package com.company;

import org.apache.log4j.Logger;

import javax.swing.*;

public class EditDialogEmploy extends DialogEmploy {
    /**
     * Логгер класса EditDialogEmploy
     */
    private static final Logger log = Logger.getLogger(EditDialogEmploy.class);

    @Override
    public void progress(employs parent) {
        log.debug("Старт метода progress");
        setVisible(false);
        int row = parent.dataEmploy.getSelectedRow();
        log.warn("Попытка изменить запись "+ parent.dataEmploy.getValueAt(row,0) +" "+parent.dataEmploy.getValueAt(row,1) + " " + parent.dataEmploy.getValueAt(row,2));
        parent.dataEmploy.setValueAt(familia.getText(), row, 0);
        parent.dataEmploy.setValueAt(name.getText(), row, 1);
        parent.dataEmploy.setValueAt(rang.getText(), row, 2);
        log.info("Заменена на "+ parent.dataEmploy.getValueAt(row,0) +" "+parent.dataEmploy.getValueAt(row,1) + " " + parent.dataEmploy.getValueAt(row,2));
        parent.makeXml();
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "2 поток закончил работу, данные сохранены"));
    }

    @Override
    public void init(employs parent) {
        log.debug("Старт метода Init");
        int row = parent.dataEmploy.getSelectedRow();
        familia = new JTextField(parent.dataEmploy.getValueAt(row, 0).toString(), 20);
        name = new JTextField(parent.dataEmploy.getValueAt(row, 1).toString(), 20);
        rang = new JTextField(parent.dataEmploy.getValueAt(row, 2).toString(), 20);
        checker(0,familia);
        checker(1,name);
        checker(2,rang);
    }

    public EditDialogEmploy(JFrame owner, employs parent, String title){
        super(owner,parent, title);
    };
}