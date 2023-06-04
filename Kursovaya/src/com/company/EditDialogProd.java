package com.company;

import javax.swing.*;

public class EditDialogProd extends DialogProd {

    @Override
    public void progress(animal parent) {
        setVisible(false);
        int row = parent.dataProducts.getSelectedRow();
        parent.dataProducts.setValueAt(name.getText(), row, 0);
        parent.dataProducts.setValueAt(country.getText(), row, 1);
        parent.dataProducts.setValueAt(weight.getText(), row, 2);
        parent.dataProducts.setValueAt(price.getText(), row, 3);
        //parent.makeXml();
    }

    @Override
    public void init(animal parent) {
        int row = parent.dataProducts.getSelectedRow();
        name = new JTextField(parent.dataProducts.getValueAt(row, 0).toString(), 20);
        country = new JTextField(parent.dataProducts.getValueAt(row, 1).toString(), 20);
        weight = new JTextField(parent.dataProducts.getValueAt(row, 2).toString(), 20);
        price = new JTextField(parent.dataProducts.getValueAt(row, 3).toString(), 20);
        checker(0,name);
        checker(1,country);
        checkerInt(2,weight);
        checkerInt(3,price);
    }

    public EditDialogProd(JFrame owner, animal parent, String title){
        super(owner,parent, title);
    };
}