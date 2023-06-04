package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
       /* String userName = "root";
        String password = "amp40wlmhv";
        String connectionUrl = "jdbc:mysql://localhost:3306/shop?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
        try (Connection connection = DriverManager.getConnection(connectionUrl,userName,password)){
            System.out.println("We are connected");
            connection.close();
        }catch (SQLException e){e.printStackTrace();};
        */
        JFrame window = new JFrame("Главное окно");
        JButton prodWindow = new JButton("Список продуктов магазина", new ImageIcon("./img/prodList.png"));
        JButton employWindow = new JButton("Список сотрудников магазина", new ImageIcon("./img/employ.png"));
        JButton reportWindow = new JButton("Отчет о состоянии магазина", new ImageIcon("./img/report.png"));
        JButton refWindow = new JButton("Открыть справку о магазине", new ImageIcon("./img/info.png"));

        prodWindow.addActionListener((e) -> new products());
        employWindow.addActionListener((e) -> new employs());
        reportWindow.addActionListener((e)-> new report());
        refWindow.addActionListener((e) -> new ref(window,"Справка"));
       // window.setLayout(new FlowLayout());
        JPanel mainp = new JPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        panel.setSize(350, 100);

        // adds to the GridLayout
        panel.add(prodWindow);
        panel.add(employWindow);
        panel.add(reportWindow);
        panel.add(refWindow);
        mainp.add(panel);
        window.add(BorderLayout.CENTER, mainp);
        window.setSize(400,380);
        window.setLocation(500,200);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
