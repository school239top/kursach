package com.company;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.log4j.Logger;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

/**
 * Класс приложения, визуализирующий экранную форму
 */
public class employs {
    employs(){
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
    protected JTable dataEmploy;
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
     * Диалоговое окно редактирования данных
     */
    private EditDialogEmploy dialog;
    /**
     * Диалоговое окно добавления данных
     */
    private AddDialogEmploy dialogAdd;

    /**
     * Поток 1 отвечает за загрузку данных из XML-файла в экранную форму
     */
    Thread t1 = new Thread();
    /**
     * Поток 2 отвечает за редактирование данных и сохранение XML-файла
     */
    Thread t2 = new Thread();
    /**
     * Поток 3 отвечает за формирование отчета
     */
    Thread t3 = new Thread();

    final static public Object shared=new Object();

    /**
     * Скролл
     */
    private JScrollPane scroll;

    /**
     * Логгер класса employs
     */
    private static final Logger log = Logger.getLogger(employs.class);


    /**
     * Метод генерации отчетов в форматах DOCX и HTML.
     * P.S генерация в формате PDF возможна, но символы кириллицы отображаться не будут.
     * @param datasource Имя файла XML с данными
     * @param xpath Директория до полей с данными. Ex.: "BookList/Books" - Fields
     * @param template Имя файла шаблона .jrxml
     * @param resultpath Имя файла, в который будет помещен отчет
     */

    public static void print(String datasource, String xpath, String template, String resultpath){
        try {
            // Указание источника XML-данных
            JRDataSource jr = new JRXmlDataSource(datasource, xpath);
            // Создание отчета на базе шаблона
            JasperReport report = JasperCompileManager.compileReport(template);
            // Заполнение отчета данными
            JasperPrint print = JasperFillManager.fillReport(report, null, jr);
            //JasperExportManager.exportReportToHtmlFile(print,resultpath);
            if(resultpath.toLowerCase().endsWith("pdf")) {
                JRExporter exporter;
                exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,resultpath);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT,print);
                exporter.exportReport();
            }
            else
                JasperExportManager.exportReportToHtmlFile(print,resultpath);
            JOptionPane.showMessageDialog(null,"3 поток закончил работу. Отчет создан");
        }
        catch (JRException e){
            e.printStackTrace();
        }
    }

    /**
     * Метод чтения данных из файла
     * @param filename Имя файла
     */
    public  void read(String filename){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            for(int i = 0; i<model.getRowCount();i++)
                model.removeRow(0);
            String temp;
            do{
                temp = reader.readLine();
                if(temp!=null){
                    String[] temp2 = temp.split(";");
                    model.addRow(temp2);
                }
            }while(temp!=null);
            reader.close();

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Метод записи данных в файл
     * @param filename Имя файла
     */
    public void write(String filename){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for(int i = 0; i<model.getRowCount();i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write((String) model.getValueAt(i, j));
                    if(j!=model.getColumnCount()-1)
                        writer.write(";");
                }
                if(i!=model.getRowCount()-1)
                    writer.write("\r\n");
            }
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Метод проверки списка на отсутсвие записей
     * @throws MyException моё исключение
     */
    private void checkList() throws MyException{
        if(model.getRowCount() == 0)
            throw new MyException();
        write("file.csv");
        JOptionPane.showMessageDialog(window, "Данные сохранены в файл file.csv");
    }

    /**
     * Метод загрузки данных в XML файл
     */
    public void makeXml() {
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
                Element dataEmploy = doc.createElement("dataEmploy");
                window.appendChild(dataEmploy);
                dataEmploy.setAttribute("familia", (String) model.getValueAt(i, 0));
                dataEmploy.setAttribute("name", (String) model.getValueAt(i, 1));
                dataEmploy.setAttribute("rang", (String) model.getValueAt(i, 2));
            }
            try {
                // Создание преобразователя документа
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                // Создание файла с именем dataEmploy.xml для записи документа
                java.io.FileWriter fw = new FileWriter("dataEmploy.xml");
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

    /**
     * Метод выгрузки данных из XML файла
     */
    public void loadXML(){
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
                // Получение списка элементов с именем dataEmploy
                NodeList nlDataEmploy = doc.getElementsByTagName("dataEmploy");
                // Цикл просмотра списка элемента и запись данных в таблицу
                for (int temp = 0; temp < nlDataEmploy.getLength(); temp++) {
                    // Выбор очередного элемента списка
                    Node elem = nlDataEmploy.item(temp);
                    // Получение списка атрибутов документа
                    NamedNodeMap attrs = elem.getAttributes();
                    // Чтение атрибутов элемента
                    String familia = attrs.getNamedItem("familia").getNodeValue();
                    String name = attrs.getNamedItem("name").getNodeValue();
                    String rang = attrs.getNamedItem("rang").getNodeValue();
                    // Запись данных в таблицу
                    model.addRow(new String[]{familia, name, rang});
                }
            }
        }
        catch (ParserConfigurationException e){e.printStackTrace();}
        // Обработка ошибки парсера при чтении данных из XML-файла
        catch (SAXException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
    }

    /**
     *      Метод отображения окна
     */
    public void show(){
        /**
         * Запись в лог warn о старте метода show() employs
         */
        log.warn("Старт App.show");
        log.debug("Создание окна приложения");
        // Создание окна
        window = new JFrame("Список сотрудников");
        window.setSize(800,500);
        window.setLocation(310,130);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        log.debug("Создание кнопок и прикрепление иконок");
        // Создание кнопок и прикрепление иконок
        add = new JButton("Добавить", new ImageIcon("./img/add.png"));
        delete = new JButton("Удалить", new ImageIcon("./img/delete.png"));
        edit = new JButton("Редактировать", new ImageIcon("./img/edit.png"));
        save = new JButton("Сохранить", new ImageIcon("./img/save.png"));
        folder = new JButton("Загрузить", new ImageIcon("./img/folder.png"));
        print = new JButton("Печать",new ImageIcon("./img/print.png"));

        // Настройка подсказок
        add.setToolTipText("Добавить информацию о сотрудниках");
        delete.setToolTipText("Удалить информацию о сотрудниках");
        edit.setToolTipText("Изменить информацию о сотрудниках");
        save.setToolTipText("Сохранить информацию о сотрудниках");
        folder.setToolTipText("Загрузить информацию о сотрудниках");
        print.setToolTipText("Распечатать информацию о сотрудниках");
        log.debug("Добавление кнопок на панель инструментов");
        // Добавление кнопок на панель инструментов
        toolBar = new JToolBar("Панель инструментов");
        toolBar.add(add);
        toolBar.add(delete);
        toolBar.add(edit);
        toolBar.add(save);
        toolBar.add(folder);
        toolBar.add(print);
        log.debug("Размещение панели инструментов");
        // Размещение панели инструментов
        window.setLayout(new BorderLayout());
        window.add(toolBar,BorderLayout.NORTH);
        log.debug("Создание таблицы с данными");
        // Создание таблицы с данными
        String[] columns = {"Фамилия", "Имя", "Должность"};
        //String [][] data = {{"Лазарев","Сергей","Директор"}, {"Трамп", "Дональд","Бухгалтер"}, {"Кек", "Чебурек","Уборщик"}};

        // Настройка таблицы
        model = new DefaultTableModel(columns,0){
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }};
        this.dataEmploy = new JTable(model);
        dataEmploy.setFont(new Font(Font.SERIF,Font.BOLD,14));
        dataEmploy.setIntercellSpacing(new Dimension(0,1));
        dataEmploy.setRowHeight(dataEmploy.getRowHeight()+10);
        dataEmploy.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        dataEmploy.setDefaultRenderer(dataEmploy.getColumnClass(1), new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.setHorizontalAlignment(SwingConstants.CENTER);
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return this;
            }

        });

        scroll = new JScrollPane(this.dataEmploy);

        // Размещение таблицы с данными
        window.add(scroll,BorderLayout.CENTER);
        log.debug("Подготовка компонентов поиска");
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

        // Слушатели


        log.debug("Старт Add listener");
        add.addActionListener((e) -> {
            dialogAdd = new AddDialogEmploy(window, employs.this, "Добавление записи");
            dialogAdd.setVisible(true);
        });

        add.setMnemonic(KeyEvent.VK_A);
        delete.addActionListener((e) -> {
            log.debug("Старт Delete listener");
            if (dataEmploy.getRowCount() > 0) {
                if (dataEmploy.getSelectedRow() != -1) {
                    try {
                        model.removeRow(dataEmploy.convertRowIndexToModel(dataEmploy.getSelectedRow()));
                        JOptionPane.showMessageDialog(window, "Вы удалили строку");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ошибка");
                    }
                } else {
                    log.error("Не выбрана строка");
                    JOptionPane.showMessageDialog(null, "Вы не выбрали строку для удаления");
                }
            } else {
                log.error("В окне нет записей");
                JOptionPane.showMessageDialog(null, "В данном окне нет записей. Нечего удалять");
            }
        });

        delete.setMnemonic(KeyEvent.VK_D);


        window.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                log.warn("Закрытие приложения");
                JOptionPane.showMessageDialog(window,"Вы закрываете окно)");
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        edit.addActionListener((e)-> {
            log.debug("Старт Edit listener");
                    t2 = new Thread(() -> {
                        if (t1.isAlive()) {
                            try {
                                log.info("Ожидание завершения 1 потока");
                                JOptionPane.showMessageDialog(window, "Ждем, пока отработает 1 поток");
                                t1.join();
                                JOptionPane.showMessageDialog(window, "1 поток отработал, пробуем запустить 2 поток");
                            } catch (InterruptedException ex) {
                                log.fatal("Join doesn't work");
                                ex.printStackTrace();
                            }
                        }
                        if (model.getRowCount() != 0) {
                            if (dataEmploy.getSelectedRow() != -1) {
                                 JOptionPane.showMessageDialog(null,"2 поток запущен");
                                 dialog = new EditDialogEmploy(window, employs.this, "Редактирование");
                                 dialog.setVisible(true);
                                 try {
                                      Thread.sleep(5000);

                                 } catch (InterruptedException ex) {
                                      ex.printStackTrace();
                                 }
                            } else {
                                log.error("Не выбрана строка");
                                JOptionPane.showMessageDialog(null, "Не выбрана строка. Нечего редактировать");
                            }
                        } else {
                            log.error("В данном окне нет записей");
                            JOptionPane.showMessageDialog(null, "В данном окне нет записей. Нечего редактировать");
                        }
                    });
                    t2.start();

        });
        edit.setMnemonic(KeyEvent.VK_E);

        save.addActionListener((e) -> {
            log.debug("Старт Save listener");
            t3 = new Thread(() -> {
                if (t2.isAlive()) {
                    try {
                        JOptionPane.showMessageDialog(window, "Ждем, пока отработает 2 поток");
                        t2.join();
                        JOptionPane.showMessageDialog(window, "2 поток отработал");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                if (model.getRowCount() != 0) {
                    JOptionPane.showMessageDialog(null, "3 поток создает отчет");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    print("dataEmploy.xml", "window/dataEmploy", "Cherry.jrxml", "otchet.pdf");
                }
            });
            t3.start();
        });

        folder.addActionListener((e) -> {
            log.warn("Старт Folder listener");
            t1 = new Thread(() -> {
                JOptionPane.showMessageDialog(window, "1 поток начал работу");
                loadXML();
                dataEmploy.setRowSelectionInterval(0,0);
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(window, "1 поток закончил работу.");
                });
            });
            t1.start();
        });

        search.addActionListener((e) -> {
            if (model.getRowCount() != 0) {
                if (!textSearch.getText().isEmpty())
                    log.debug("Запуск нового поиска по ключевому слову: " + textSearch.getText());
                else
                    log.debug("Сброс ключевого слова поиска");
                TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) model));
                sorter.setStringConverter(new TableStringConverter() {
                    @Override
                    public String toString(TableModel model, int row, int column) {
                        return model.getValueAt(row, column).toString().toLowerCase();
                    }
                });
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textSearch.getText().toLowerCase()));
                dataEmploy.setRowSorter(sorter);
            }
        });

        print.addActionListener((e)-> {
            class myThread extends Thread{
                private int type;
                public myThread(int i) {
                    type=i;
                }

                public void run() {

                    if (type==1) {
                        synchronized (shared) {
                            try {
                                shared.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            try {

                                loadXML();
                                dataEmploy.setRowSelectionInterval(0,0);

                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                    if (type==2) {
                        synchronized (shared) {
                            shared.notifyAll();
                            try {
                                shared.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            dialog = new EditDialogEmploy(window, employs.this, "Редактирование");
                            dialog.setVisible(true);

                            shared.notifyAll();
                        }
                    }

                    if (type==3) {
                        synchronized (shared) {
                            shared.notifyAll();
                            try {
                                shared.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            print("dataEmploy.xml", "window/dataEmploy", "Cherry.jrxml", "otchet.pdf");

                        }
                    }
                }
            }
            new myThread(1).start();
            new myThread(2).start();
            new myThread(3).start();
        });


        // Если не выделена строка, то прячем кнопки
        dataEmploy.getSelectionModel().addListSelectionListener((e) -> {
            Boolean check = true;
            if (dataEmploy.getSelectionModel().isSelectionEmpty()) {
                check = false;
            }
            edit.setVisible(check);
            delete.setVisible(check);
        });


        // Визуализация экранной формы
        window.setVisible(true);
    }

    /**
     * Вспомогательная функция для добавления данных из формы
     * @param arr Массив данных: фамилия, имя, должность
     */
    public void addR(String[] arr){
        model.addRow(arr);
    }
}
