package com.company;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;

public class products {

    protected void makeXml() {
        try {
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создание пустого документа
            Document doc = builder.newDocument();
            // Создание корневого элемента window и добавление его в документ
            Node window = doc.createElement("window");
            doc.appendChild(window);
            // Создание дочерних элементов dataEmploy и присвоение значений атрибутам
            for (int i = 0; i < model.getRowCount(); i++) {
                Element dataEmploy = doc.createElement("dataProducts");
                window.appendChild(dataEmploy);
                dataEmploy.setAttribute("name", (String) model.getValueAt(i, 0));
                dataEmploy.setAttribute("country", (String) model.getValueAt(i, 1));
                dataEmploy.setAttribute("weight", (String) model.getValueAt(i, 2));
                dataEmploy.setAttribute("price", (String) model.getValueAt(i, 2));
            }
            try {
                // Создание преобразователя документа
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                // Создание файла с именем dataEmploy.xml для записи документа
                java.io.FileWriter fw = new FileWriter("dataProducts.xml");
                // Запись документа в файл
                trans.transform(new DOMSource(doc), new StreamResult(fw));
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected void loadXML(){
        try{
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            // Чтение документа из файла
            JFileChooser fileChooser = new JFileChooser("C:\\Users\\frose\\IdeaProjects\\labi");
            int ret = fileChooser.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                doc = dBuilder.parse(fileChooser.getSelectedFile());
                // Нормализация документа
                doc.getDocumentElement().normalize();
                // Получение списка элементов с именем dataProducts
                NodeList nldataProducts = doc.getElementsByTagName("dataProducts");
                // Цикл просмотра списка элемента и запись данных в таблицу
                for (int temp = 0; temp < nldataProducts.getLength(); temp++) {
                    // Выбор очередного элемента списка
                    Node elem = nldataProducts.item(temp);
                    // Получение списка атрибутов документа
                    NamedNodeMap attrs = elem.getAttributes();
                    // Чтение атрибутов элемента
                    String name = attrs.getNamedItem("name").getNodeValue();
                    String country = attrs.getNamedItem("country").getNodeValue();
                    String weight = attrs.getNamedItem("weight").getNodeValue();
                    String price = attrs.getNamedItem("price").getNodeValue();
                    // Запись данных в таблицу
                    model.addRow(new String[]{name, country, weight, price});
                }
            }
            else
                JOptionPane.showMessageDialog(null,"Вы не выбрали файл");
        }
        catch (ParserConfigurationException e){e.printStackTrace();}
        // Обработка ошибки парсера при чтении данных из XML-файла
        catch (SAXException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
    }

    products(){
        show();
    }

    /**
     * Окно приложения
     */
    private JFrame window;
    /**
     * Модель таблицы
     */
    private DefaultTableModel model;
    /**
     * Добавить
     */
    private JButton add;
    /**
     * Удалить
     */
    private JButton delete;
    /**
     * Изменить
     */
    private JButton edit;
    /**
     * Сохранить изменения
     */
    private JButton save;
    /**
     * Открыть файл
     */
    private JButton folder;
    /**
     * Печать
     */
    private JButton print;
    /**
     * Панель инструментов
     */
    private JToolBar toolBar;
    /**
     * Таблица
     */
    protected JTable dataProducts;
    /**
     * Выпадающий список
     */
    private JComboBox comboBox;
    /**
     * Поле поискового запроса
     */
    private JTextField textSearch;
    /**
     * Поиск
     */
    private JButton search;
    /**
     * Скролл
     */
    private JScrollPane scroll;

    private AddDialogProd addDialogProd;
    private EditDialogProd editDialogProd;

    Thread t1 = new Thread();
    Thread t2 = new Thread();
    Thread t3 = new Thread();

    public void show(){
        window = new JFrame("Список продуктов");
        window.setSize(1000,500);
        window.setLocation(310,130);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Создание кнопок и прикрепление иконок
        add = new JButton("Добавить", new ImageIcon("./img/add.png"));
        delete = new JButton("Удалить", new ImageIcon("./img/delete.png"));
        edit = new JButton("Редактировать", new ImageIcon("./img/edit.png"));
        save = new JButton("Сохранить", new ImageIcon("./img/save.png"));
        folder = new JButton("Загрузить", new ImageIcon("./img/folder.png"));
        print = new JButton("Печать",new ImageIcon("./img/print.png"));

        // Настройка подсказок
        add.setToolTipText("Добавить информацию о товаре");
        delete.setToolTipText("Удалить информацию о товаре");
        edit.setToolTipText("Изменить информацию о товаре");
        save.setToolTipText("Сохранить информацию о товарах");
        folder.setToolTipText("Загрузить информацию о товарах");
        print.setToolTipText("Распечатать информацию о товарах");
        // Добавление кнопок на панель инструментов
        toolBar = new JToolBar("Панель инструментов");
        toolBar.add(add);
        toolBar.add(delete);
        toolBar.add(edit);
        toolBar.add(save);
        toolBar.add(folder);
        toolBar.add(print);
        // Размещение панели инструментов
        window.setLayout(new BorderLayout());
        window.add(toolBar,BorderLayout.NORTH);
        // Создание таблицы с данными
        String[] columns = {"Название", "Страна производства", "Вес", "Цена"};

        // Настройка таблицы
        model = new DefaultTableModel(columns,0){
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }};
        this.dataProducts = new JTable(model);
        dataProducts.setFont(new Font(Font.SERIF,Font.BOLD,14));
        dataProducts.setIntercellSpacing(new Dimension(0,1));
        dataProducts.setRowHeight(dataProducts.getRowHeight()+10);
        dataProducts.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        dataProducts.setDefaultRenderer(dataProducts.getColumnClass(1), new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.setHorizontalAlignment(SwingConstants.CENTER);
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return this;
            }

        });

        scroll = new JScrollPane(this.dataProducts);

        // Размещение таблицы с данными
        window.add(scroll,BorderLayout.CENTER);
        // Подготовка компонентов поиска
        comboBox = new JComboBox(new String[]{"Фамилия", "Имя", "Должность"});
        textSearch = new JTextField();
        textSearch.setColumns(20);
        search = new JButton("Поиск");
        window.getRootPane().setDefaultButton(search);
// remove the binding for pressed
        window.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "none");
// retarget the binding for released
        window.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("released ENTER"), "press");
        // Добавление компонентов на панель
        JPanel searchPanel = new JPanel();
        searchPanel.add(comboBox);
        searchPanel.add(textSearch);
        searchPanel.add(search);

        // Размещение панели поиска внизу окна
        window.add(searchPanel,BorderLayout.SOUTH);

        add.addActionListener((e) -> {
            addDialogProd = new AddDialogProd(window, products.this, "Добавление записи");
            addDialogProd.setVisible(true);
        });

        add.setMnemonic(KeyEvent.VK_A);
        delete.addActionListener((e) -> {
            if (dataProducts.getRowCount() > 0) {
                if (dataProducts.getSelectedRow() != -1) {
                    try {
                        model.removeRow(dataProducts.convertRowIndexToModel(dataProducts.getSelectedRow()));
                        JOptionPane.showMessageDialog(window, "Вы удалили строку");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ошибка");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали строку для удаления");
                }
            } else {
                JOptionPane.showMessageDialog(null, "В данном окне нет записей. Нечего удалять");
            }
        });

        delete.setMnemonic(KeyEvent.VK_D);

        edit.addActionListener((e)-> {
            if (t1.isAlive()) {
                try {
                    JOptionPane.showMessageDialog(window, "Ждем, пока отработает 1 поток");
                    t1.join();
                    JOptionPane.showMessageDialog(window, "1 поток отработал, пробуем запустить 2 поток");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if (model.getRowCount() != 0) {
                if (dataProducts.getSelectedRow() != -1) {
                    t2 = new Thread(() -> {
                        JOptionPane.showMessageDialog(null,"2 поток запущен");
                        editDialogProd = new EditDialogProd(window, products.this, "Редактирование");
                        editDialogProd.setVisible(true);
                    });
                    t2.start();
                } else {
                    JOptionPane.showMessageDialog(null, "Не выбрана строка. Нечего редактировать");
                }
            } else {
                JOptionPane.showMessageDialog(null, "В данном окне нет записей. Нечего редактировать");
            }
        });
        edit.setMnemonic(KeyEvent.VK_E);

        save.addActionListener((e) -> {
            if (t2.isAlive()) {
                try {
                    t2.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if (model.getRowCount() != 0) {
                t3 = new Thread(() -> {
                    makeXml();
                });
                t3.start();
            }
        });

        folder.addActionListener((e) -> {
            t1 = new Thread(() -> {
                loadXML();
                dataProducts.setRowSelectionInterval(0,0);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(window, "1 поток закончил работу.");
                });
            });
            t1.start();

        });

        search.addActionListener((e) -> {
            if (model.getRowCount() != 0) {
                TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) model));
                sorter.setStringConverter(new TableStringConverter() {
                    @Override
                    public String toString(TableModel model, int row, int column) {
                        return model.getValueAt(row, column).toString().toLowerCase();
                    }
                });
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textSearch.getText().toLowerCase()));
                dataProducts.setRowSorter(sorter);
            }
        });

        print.addActionListener((e)->{
            if (model.getRowCount() != 0) {
                employs.print("dataProducts.xml", "window/dataProducts", "prod.jrxml", "otchetProd.pdf");
            }
        });

        // Если не выделена строка, то прячем кнопки
        dataProducts.getSelectionModel().addListSelectionListener((e) -> {
            Boolean check = true;
            if (dataProducts.getSelectionModel().isSelectionEmpty()) {
                check = false;
            }
            edit.setVisible(check);
            delete.setVisible(check);
        });

        window.setVisible(true);
    }
    public void addR(String[] arr){
        model.addRow(arr);
    }

}
