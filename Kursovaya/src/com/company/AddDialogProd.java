package com.company;

import javax.swing.*;

public class AddDialogProd extends DialogProd {
    /**
     * Логгер класса AddDialogEmploy
     */
    @Override
    public void progress(products parent) {
        setVisible(false);
        String[] arr = {name.getText(),country.getText(),weight.getText(),price.getText()};
        parent.addR(arr);
        JOptionPane.showMessageDialog(null, "Вы добавили товар \""+arr[0]+" из "+arr[1]+"\"");
    }

    @Override
    public void init(products parent) {
        name = new JTextField(20);
        country= new JTextField(20);
        weight = new JTextField(20);
        price = new JTextField(20);
    }

    public AddDialogProd(JFrame owner, products parent, String title){
        super(owner,parent,title);
    }
}
