package com.company;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Класс окна Добавления данных
 */
public class AddDialogReport extends DialogReport {

    @Override
    public void progress(report parent) {

        setVisible(false);
        String[] arr = {name.getSelectedItem().toString(),kolvo.getText()};

        parent.addR(arr);
        JOptionPane.showMessageDialog(null, "Вы добавили товар в отчет \""+arr[0]+" "+arr[1]+"\"");
    }

    @Override
    public void init(report parent) {
        name = loadComboBox();
        kolvo= new JTextField(20);
    }

    public AddDialogReport(JFrame owner, report parent, String title){
        super(owner,parent,title);
    }

    protected JComboBox loadComboBox(){
        JComboBox combo = new JComboBox();
        try{
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            // Чтение документа из файла
            doc = dBuilder.parse("dataProducts.xml");
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
                combo.addItem(name);
            }
        }
        catch (ParserConfigurationException e){e.printStackTrace();}
        // Обработка ошибки парсера при чтении данных из XML-файла
        catch (SAXException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
        return combo;
    }
}
