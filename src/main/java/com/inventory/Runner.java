package com.inventory;
import java.io.*;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class Runner {

    private static HashMap<String, Items> inventoryItems = new HashMap<>();
    private static HashSet<String> cards = new HashSet<>();
    private static double totalAmount=0;
    private static String dataSetFilePath ="src/main/resources/Dataset.csv";
    private static String cardFilePath ="src/main/resources/Cards.csv";
    private static String errorFilePath ="src/main/resources/error.txt";
    private static String outputFilePath ="src/main/resources/input/Output.csv";
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception
    {
        clearOutputFiles();
        System.out.println("We have receive File with Path :" + args[0]);
        List<String[]> inventory =readCSVFile(dataSetFilePath);
        List<String[]> inputCards =readCSVFile(cardFilePath);
        List<String[]> inputsItems =readCSVFile(args[0]);
        // Storing inventory and card details to Map
        prepareInventory(inventory);
        addCards(inputCards);
        //validate Input
        Set<String> corrections = new HashSet<>();
        //starting chain of responsibility to calculate bill amount
        calculateTotalPrice(corrections,inputsItems);
        //Create record
        //update Cards.csv
        updateCards();
        // Prepare output
        if(corrections.size()==0) {
            prepareOutputCSV();
        }
        else
        {
           prepareOutputError(corrections);
        }
    }

    private static void updateCards() throws IOException {
        List<String[]> updatedCards = new LinkedList<>();
        String[] header= new String[]{"CardNumber"};
        updatedCards.add(header);
        for(String card :cards)
        {
            String[] addCard= new String[]{card};
            updatedCards.add(addCard);
        }
        CSVWriter writer = new CSVWriter(new FileWriter(cardFilePath), ',');
        writer.writeAll(updatedCards);
        writer.flush();
        writer.close();
    }

    private static void prepareOutputError(Set<String> corrections) {
        System.out.println("Please correct quantities");
        File file = new File(errorFilePath);
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
            bf.write("Please correct quantities.");
            bf.newLine();
            for ( String input : corrections) {

                bf.write(input);
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void calculateTotalPrice(Set<String> corrections, List<String[]> inputsItems) {
        Essentials essentials = new Essentials();
        Luxury luxury = new Luxury();
        Miscellaneous miscellaneous = new Miscellaneous();
        essentials.setNextCategory(luxury);
        luxury.setNextCategory(miscellaneous);
        Set<String> essentialsSeen = new HashSet<>();
        Set<String> luxurySeen = new HashSet<>();
        Set<String> MissSeen = new HashSet<>();
        for(String[] map : inputsItems) {

            cards.add(map[2]);
            if(inventoryItems.get(map[0]).Category== Category.NOCATEGORY)
                corrections.add(map[0]);
            else{
            double amount= essentials.calculatePrice(inventoryItems,map[0],Integer.parseInt(map[1]),corrections,essentialsSeen,luxurySeen,MissSeen);
            totalAmount+=amount;
            }
        }

        if(CategoryLimit.getTotalEssential()<0)
            corrections.addAll(essentialsSeen);

        if(CategoryLimit.getTotalLuxury()<0)
            corrections.addAll(luxurySeen);

        if(CategoryLimit.getTotalMisc()<0)
            corrections.addAll(MissSeen);
    }

    private static void prepareOutputCSV() throws IOException {
        List<String[]> writing= new LinkedList<>();
        writing.add(new String[]{"Amt Paid"});
        writing.add(new String[]{""+totalAmount});
        CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath), ',');
        writer.writeAll(writing);
        writer.flush();
        writer.close();
    }

    private static void addCards(List<String[]> inputCards) {
        for(String[] card:inputCards)
        {
            cards.add(card[0]);
        }
    }

    private static void prepareInventory(List<String[]> inventory) {
        for(String[] row : inventory){
            Items item= toItems(Arrays.toString(row));
            if(item!=null)
                inventoryItems.put(item.getItem(), item);
        }
    }

    private static void clearOutputFiles() throws IOException {
        new FileWriter("src/main/resources/input/Output.csv",false);
        new FileWriter("src/main/resources/input/error.txt",false);
    }

    private static List<String[]> readCSVFile(String filePath) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filePath), ',', '"', 1);
        return  reader.readAll();

    }

    private static Items toItems(String column) {
        column = column.substring(1,column.length()-1);
        String[] columns = column.split(",");
        if(columns.length<4)
             return null;


        return Items.builder().Category(toValue(columns[0].trim()))
                .item(columns[1].trim())
                .quantity(Integer.valueOf(columns[2].trim()))
                .price(Double.valueOf(columns[3].trim()))
                .build();
    }

    static Category toValue(String category)
    {
        return switch (category) {
            case "Essential" -> Category.ESSENTIALS;
            case "Luxury" -> Category.LUXURY;
            case "Miscellaneous" -> Category.MISCELLENEOUS;
            default -> Category.NOCATEGORY;
        };
    }
}

