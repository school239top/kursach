package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame("Зоопарк");
        JButton prodWindow = new JButton("Звери", new ImageIcon("./img/zoo.png"));
        JButton employWindow = new JButton("Сотрудники Зоопарка", new ImageIcon("./img/hum.png"));
        JButton reportWindow = new JButton("Покормленые звери", new ImageIcon("./img/eat.png"));
       // JButton refWindow = new JButton("Открыть справку о магазине", new ImageIcon("./img/info.png"));

        prodWindow.addActionListener((e) -> new animal());

        reportWindow.addActionListener((e)-> new report());
        employWindow.addActionListener((e) -> new employs());
        //refWindow.addActionListener((e) -> new ref(window,"Справка"));
       // window.setLayout(new FlowLayout());
        JPanel mainp = new JPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        panel.setSize(350, 100);

        // adds to the GridLayout
        panel.add(employWindow);
        panel.add(prodWindow);

        //panel.add(reportWindow);
        //load = new JButton("Загрузить");
        reportWindow.setBounds(0,0,250,80);
        window.add(reportWindow);
        //panel.add(refWindow);
        mainp.add(panel);
        window.add(BorderLayout.CENTER, mainp);
        window.setSize(900,300);
        window.setLocation(500,200);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
