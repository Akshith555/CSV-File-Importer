package com.project.springbootimportcsvfileapp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;


public class CSVHelper {
  public static String TYPE = "text/csv";
  static String[] HEADERs = { "Code","Name","Batch","Stock","Deal","Free","Mrp","Rate","Exp","Company","Supplier"  };

  public static boolean hasCSVFormat(MultipartFile file) {
 System.out.println(file.getContentType());
    if (TYPE.equals(file.getContentType())
    		|| file.getContentType().equals("application/vnd.ms-excel")) {
    	
      return true;
    }
   
    return false;
  }

  public static List<Inventory> csvToTutorials(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

      List<Inventory> inventoryList = new ArrayList<>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
    	  Inventory inventory = new Inventory(
    			  csvRecord.get("Code"),
        		  csvRecord.get("Name"),
        		  csvRecord.get("Batch"),
        		  Long.parseLong(csvRecord.get("Stock")),
        		  Long.parseLong(csvRecord.get("Deal")),
        		  Long.parseLong(csvRecord.get("Free")),
        		  Long.parseLong(csvRecord.get("Mrp")),
        		  Long.parseLong(csvRecord.get("Rate")),
        		  csvRecord.get("Exp"),
        		  csvRecord.get("Company"),
        		  csvRecord.get("Supplier")
            );

    	  inventoryList.add(inventory);
      }

      return inventoryList;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

  public static ByteArrayInputStream tutorialsToCSV(List<Inventory> developerTutorialList) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
      for (Inventory inventory : developerTutorialList) {
        List<String> data = Arrays.asList(
        	  inventory.getCode(),
        	  inventory.getName(),
        	  inventory.getBatch(),
        	  String.valueOf(inventory.getStock()),
        	  String.valueOf(inventory.getDeal()),
        	  String.valueOf(inventory.getFree()),
        	  String.valueOf(inventory.getMrp()),
        	  String.valueOf(inventory.getRate()),
        	  inventory.getExp(),
        	  inventory.getCompany(),
        	  inventory.getSupplier()
        		
            );

        csvPrinter.printRecord(data);
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
    }
  }
}
