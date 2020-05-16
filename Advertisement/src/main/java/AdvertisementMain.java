
import commands.Commands;
import model.Category;
import model.Gender;
import model.Item;
import model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import storage.DataStorage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AdvertisementMain implements Commands {
    private static String fileName = null;
    private static Scanner scanner = new Scanner(System.in);
    private static DataStorage dataStorage = new DataStorage();
    private static User currentUser = null;
    private static XSSFWorkbook workbook = null;

    public static void main(String[] args) {
        dataStorage.initData();
        boolean isRun = true;
        while (isRun) {
            Commands.printMainCommands();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                command = -1;
            }
            switch (command) {
                case EXIT:
                    isRun = false;
                    break;
                case LOGIN:
                    loginUser();
                    break;
                case REGISTER:
                    registerUser();
                    break;
                case IMPORT_USERS:
                    importFromXlsx();
                    break;
                default:
                    System.out.println("Wrong command!");
            }
        }
    }

    private static void importFromXlsx() {
        System.out.println("Please xlsx path");
        String xlsxpath = scanner.nextLine();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(xlsxpath);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRovNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRovNum; i++) {
                Row row = sheet.getRow(i);
                String name = row.getCell(0).getStringCellValue();
                String surName = row.getCell(1).getStringCellValue();
                Double age = row.getCell(2).getNumericCellValue();
                Gender gender = Gender.valueOf(row.getCell(3).getStringCellValue());
                Cell phonenumber = row.getCell(4);
                String phoneNumberstr = phonenumber.getCellType() == CellType.NUMERIC ?
                        String.valueOf(Double.valueOf(phonenumber.getNumericCellValue()).intValue()) : phonenumber.getStringCellValue();
                Cell password = row.getCell(5);
                String passwordstr = password.getCellType() == CellType.NUMERIC ?
                        String.valueOf(Double.valueOf(password.getNumericCellValue()).intValue()) : password.getStringCellValue();

                User user = new User();
                user.setName(name);
                user.setSurname(surName);
                user.setAge(age.intValue());
                user.setGender(gender);
                user.setPhoneNumber(phoneNumberstr);
                user.setPassword(passwordstr);
                System.out.println(user);
                dataStorage.add(user);
                System.out.println("import was seccess");
            }
        } catch (IOException e) {
            System.out.println("error while importing users");
            e.printStackTrace();
        }
    }

    private static void registerUser() {
        System.out.println("Please input user data " +
                "name,surname,gender(MALE,FEMALE),age,phoneNumber,password");
        try {
            String userDataStr = scanner.nextLine();
            String[] userDataArr = userDataStr.split(",");
            User userFromStorage = dataStorage.getUser(userDataArr[4]);
            if (userFromStorage == null) {
                User user = new User();
                user.setName(userDataArr[0]);
                user.setSurname(userDataArr[1]);
                user.setGender(Gender.valueOf(userDataArr[2].toUpperCase()));
                user.setAge(Integer.parseInt(userDataArr[3]));
                user.setPhoneNumber(userDataArr[4]);
                user.setPassword(userDataArr[5]);
                dataStorage.add(user);
                System.out.println("User was successfully added");
            } else {
                System.out.println("User already exists!");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Wrong Data!");
        }
    }

    private static void loginUser() {
        System.out.println("Please input phoneNumber,password");
        try {
            String loginStr = scanner.nextLine();
            String[] loginArr = loginStr.split(",");
            User user = dataStorage.getUser(loginArr[0]);
            if (user != null && user.getPassword().equals(loginArr[1])) {
                currentUser = user;
                loginSuccess();
            } else {
                System.out.println("Wrong phoneNumber or password");
            }

        } catch (ArrayIndexOutOfBoundsException | IOException e) {
            System.out.println("Wrong Data!");
        }
    }

    private static void loginSuccess() throws IOException {
        System.out.println("Welcome " + currentUser.getName() + "!");
        boolean isRun = true;
        while (isRun) {
            Commands.printUserCommands();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                command = -1;
            }
            switch (command) {
                case LOGOUT:
                    isRun = false;
                    break;
                case ADD_NEW_AD:
                    addNewItem();
                    if (fileName != null) {
                        ForTread test =new ForTread(fileName,currentUser);
                    }
                    break;
                case PRINT_MY_ADS:
                    dataStorage.printItemsByUser(currentUser);
                    break;
                case PRINT_ALL_ADS:
                    dataStorage.printItems();
                    break;
                case PRINT_ADS_BY_CATEGORY:
                    printByCategory();
                    break;
                case PRINT_ALL_ADS_SORT_BY_TITLE:
                    dataStorage.printItemsOrderByTitle();
                    break;
                case PRINT_ALL_ADS_SORT_BY_DATE:
                    dataStorage.printItemsOrderByDate();
                    break;
                case DELETE_MY_ALL_ADS:
                    dataStorage.deleteItemsByUser(currentUser);
                    break;
                case DELETE_AD_BY_ID:
                    deleteById();
                    break;
                case IMPORT_ITEMS:
                    importForItems();
                    break;
                case EXPORTITEM:
                    exportItem();
                    break;
                default:
                    System.out.println("Wrong command!");
            }
        }
    }

    private static void importForItems() {
        System.out.println("Please xlsx path ");
        String xlsxpath = scanner.nextLine();
        fileName=xlsxpath;
        try {
            workbook = new XSSFWorkbook(xlsxpath);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRovNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRovNum; i++) {
                Row row = sheet.getRow(i);
                String title = row.getCell(0).getStringCellValue();
                String text = row.getCell(1).getStringCellValue();
                Double prise = row.getCell(2).getNumericCellValue();
                Category category = Category.valueOf(row.getCell(3).getStringCellValue());
                Item item = new Item();
                item.setTitle(title);
                item.setText(text);
                item.setPrice(prise);
                item.setCategory(category);
               // item.setCreatedDate(new Date());
                item.setUser(currentUser);
                System.out.println(item);
                dataStorage.add(item);
                System.out.println("import was seccess");
            }
        } catch (IOException e) {
            System.out.println(" error ");
            e.printStackTrace();
        }
    }

    private static void deleteById() {
        System.out.println("please choose id from list");
        try {


            dataStorage.printItemsByUser(currentUser);
            long id = Long.parseLong(scanner.nextLine());
            Item itemById = dataStorage.getItemById(id);
            if (itemById != null && itemById.getUser().equals(currentUser)) {
                dataStorage.deleteItemsById(id);
            } else {
                System.out.println("Wrong id!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong id!");
        }
    }

    private static void printByCategory() {
        System.out.println("Please choose category name from list: " + Arrays.toString(Category.values()));
        try {
            String categoryStr = scanner.nextLine();
            Category category = Category.valueOf(categoryStr);
            dataStorage.printItemsByCategory(category);
        } catch (Exception e) {
            System.out.println("Wrong Category!");
        }
    }

    private static void addNewItem() {
        System.out.println("Please input item data title,text,price,category");
        System.out.println("Please choose category name from list: " + Arrays.toString(Category.values()));
        try {
            String itemDataStr = scanner.nextLine();
            String[] itemDataArr = itemDataStr.split(",");
            Item item = new Item(itemDataArr[0], itemDataArr[1], Double.parseDouble(itemDataArr[2])
                    , currentUser, Category.valueOf(itemDataArr[3].toUpperCase()), new Date());
            dataStorage.add(item);
            System.out.println("Item was successfully added");
        } catch (Exception e) {
            System.out.println("Wrong Data!");
        }


    }

    private static void exportItem() {
        if (fileName == null) {
            System.out.println("please input xlsx path");
            fileName = scanner.nextLine();
            List<Item> items = dataStorage.itemsForUser(currentUser);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();

            String title0 = "Title";
            String text0 = "Text";
            String price0 = "Price";
            String category0 = "Category";
            int rowNum = 0;


            Row row0 = sheet.createRow(rowNum++);
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue(title0);
            Cell cell01 = row0.createCell(1);
            cell01.setCellValue(text0);
            Cell cell02 = row0.createCell(2);
            cell02.setCellValue(price0);
            Cell cell03 = row0.createCell(3);
            cell03.setCellValue(category0);

            for (Item item : items) {
                Row row = sheet.createRow(rowNum++);

                String title = item.getTitle();
                Cell cell = row.createCell(0);
                cell.setCellValue(title);
                Cell cell1 = row.createCell(1);
                String text = item.getText();
                cell1.setCellValue(text);
                Cell cell2 = row.createCell(2);
                Double price = item.getPrice();
                cell2.setCellValue(price);
                Cell cell3 = row.createCell(3);
                Category category = item.getCategory();
                cell3.setCellValue((String.valueOf(category)));

            }
            try {
                FileOutputStream outputStream = new FileOutputStream(fileName);
                workbook.write(outputStream);
                workbook.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("exported");
        }
    }
    public static List<Item> itemsForUsers(){
        return dataStorage.itemsForUser(currentUser);
    }

}


