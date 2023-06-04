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
public abstract class DialogEmploy extends JDialog {
    protected JTextField familia;
    protected JTextField name;
    protected JTextField rang;
    protected Boolean[] check = {false,false,false};
    private JButton ok = new JButton("Принять");
    private JButton cancel = new JButton("Закрыть");
    private JLabel famLab = new JLabel("Фамилия");
    private JLabel namLab = new JLabel("Имя");
    private JLabel ranLab = new JLabel("Должность");

    /**
     * Выполнение манипуляций с данными
     *
     * @param parent Объект класса приложения
     */
    public abstract void progress(employs parent);

    /**
     * Инициализация
     *
     * @param parent Объект класса приложения
     */
    public abstract void init(employs parent);

    /**
     * Основной конструктор
     *
     * @param owner  JFrame приложения
     * @param parent Объект класса приложения
     * @param title  Title окна
     */
    public DialogEmploy(JFrame owner, employs parent, String title) {
        super(owner, title, true);
        ok.setEnabled(false);
        // Инит кнопок
        init(parent);

        // Создание валидатора полей
        familia.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(0, familia);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(0, familia);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        name.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(1,name);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(1,name);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        rang.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(2,rang);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(2,rang);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        ok.addActionListener((e) -> progress(parent));
        cancel.addActionListener((e) -> dispose());
        JPanel mainp = new JPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 2, 2));

        panel.setSize(300, 100);

        // adds to the GridLayout
        panel.add(famLab);
        panel.add(familia);
        panel.add(namLab);
        panel.add(name);
        panel.add(ranLab);
        panel.add(rang);
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
        Pattern r = Pattern.compile("^[А-ЯЁ][а-яЁё]{1,10}$");
        Matcher m = r.matcher(field.getText());
        if (m.matches()) {
            field.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            check[i] = true;
        }
        else {
            field.setBorder(BorderFactory.createLineBorder(Color.RED));
            check[i] = false;
        }
        if(check[0] && check[1] && check[2])
            ok.setEnabled(true);
        else
            ok.setEnabled(false);
    }
}