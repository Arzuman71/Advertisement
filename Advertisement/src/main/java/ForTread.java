import model.Category;
import model.Item;
import model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;



public class ForTread implements Runnable {
    Thread thread;
    String fileName;
    User currentUser;

    List<Item> items = AdvertisementMain.itemsForUsers();

    public ForTread(String fileName, User currentUser) {
        thread = new Thread(this, "MY TREAD1");
        this.fileName = fileName;
        this.currentUser = currentUser;
        thread.start();

    }

    @Override
    public void run() {

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

