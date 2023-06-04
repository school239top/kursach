package com.company;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Абстрактный класс Диалогового окна Добавления/Редактирования данных сотрудников
 */
public abstract class DialogReport extends JDialog {
    protected JComboBox name;
    protected JTextField kolvo;
    protected Boolean[] check = {false,false};
    private JButton ok = new JButton("Принять");
    private JButton cancel = new JButton("Закрыть");
    private JLabel nameLab = new JLabel("Название");
    private JLabel kolvoLab = new JLabel("Количество");

    /**
     * Выполнение манипуляций с данными
     *
     * @param parent Объект класса приложения
     */
    public abstract void progress(report parent);

    /**
     * Инициализация
     *
     * @param parent Объект класса приложения
     */
    public abstract void init(report parent);
    /**
     * Основной конструктор
     *
     * @param owner  JFrame приложения
     * @param parent Объект класса приложения
     * @param title  Title окна
     */
    public DialogReport(JFrame owner, report parent, String title) {
        super(owner, title, true);
        ok.setEnabled(false);
        // Инит кнопок
        init(parent);

        kolvo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(1,kolvo);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(1,kolvo);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });


        ok.addActionListener((e) -> progress(parent));
        cancel.addActionListener((e) -> dispose());
        JPanel mainp = new JPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 2, 2));

        panel.setSize(300, 100);

        // adds to the GridLayout
        panel.add(nameLab);
        panel.add(name);
        panel.add(kolvoLab);
        panel.add(kolvo);
        mainp.add(panel);
        add(BorderLayout.CENTER, mainp);
        JPanel but = new JPanel();
        but.add(ok);
        but.add(cancel);
        add(BorderLayout.SOUTH, but);
        setLocation(500, 250);
        setSize(500, 155);
        this.getRootPane().setDefaultButton(ok);
// remove the binding for pressed
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "none");
// retarget the binding for released
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("released ENTER"), "press");
    }

    protected void checker(int i, JTextField field){
        Pattern r = Pattern.compile("^[+-]?(([1-9][0-9]*)|(0))([.,][0-9]+)?$");
        Matcher m = r.matcher(field.getText());
        if (m.matches()) {
            field.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            check[i] = true;
        }
        else {
            field.setBorder(BorderFactory.createLineBorder(Color.RED));
            check[i] = false;
        }
        if(name.getSelectedIndex()==-1)
            check[0] = false;
        else
            check[0] = true;
        if(check[0] && check[1])
            ok.setEnabled(true);
        else
            ok.setEnabled(false);
    }

}