package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

    private static final String INPUT_FILE_PATH = "src/main/resources/input.xml"; // Путь к входному XML файлу
    private static final String OUTPUT_FILE_PATH = "output/data.csv"; // Путь к выходному CSV файлу

    public static void main(String[] args) {
        try {
            // Чтение XML файла
            File xmlFile = new File(INPUT_FILE_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            // Запрос названия авиакомпании у пользователя
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите название авиакомпании: ");
            String airlineName = scanner.nextLine();

            // Получение списка записей
            NodeList nodeList = document.getElementsByTagName("самолёт");
            List<String[]> records = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String id = element.getAttribute("рейс");
                String time = element.getElementsByTagName("время").item(0).getTextContent();
                String destination = element.getElementsByTagName("направление").item(0).getTextContent();
                String status = element.getElementsByTagName("статус").item(0).getTextContent();
                String type = element.getElementsByTagName("тип").item(0).getTextContent();
                String company = element.getElementsByTagName("авиакомпания").item(0).getTextContent();

                // Фильтрация по названию авиакомпании
                if (company.equalsIgnoreCase(airlineName)) {
                    records.add(new String[]{id, time, destination, status, type, company});
                }
            }

            // Запись CSV файла
            try (FileWriter writer = new FileWriter(OUTPUT_FILE_PATH);
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL.withDelimiter(';'))) {

                csvPrinter.printRecord("рейс", "время", "направление", "статус", "тип", "авиакомпания");
                for (String[] record : records) {
                    csvPrinter.printRecord((Object[]) record);
                }
                System.out.println("CSV файл успешно создан.");
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}
