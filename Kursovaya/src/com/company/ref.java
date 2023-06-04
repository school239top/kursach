package com.company;

import javax.swing.*;
import java.awt.*;

// Класс окна справки
public class ref extends JDialog{
    public ref(JFrame owner, String title){
        super(owner,title,true);
        setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel text = new JLabel();
        text.setText("<html>Магазин \"Кукумбер\"<br><br>Директор: Азаматова Алтана <br><br>Адрес: г. СПб, Новоизмайловский проспект, 16к2</html>");
        ImageIcon icon = new ImageIcon("img/ref.png");
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(230, 140,  java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);
        JLabel label = new JLabel();
        label.setIcon(icon);
        add(text,BorderLayout.WEST);
        add(label, BorderLayout.EAST);
        setSize(520,200);
        setLocation(400,250);
        setVisible(true);
    }
}
