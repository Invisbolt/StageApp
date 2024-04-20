package Utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Abidine
 */
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import Architecture.Employe;
import Architecture.Bon;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.time.LocalDate;
import java.time.LocalTime;

public class PDFUtil {

    public static void makePDF(Employe e, Bon b, String imagePath) {
        // Replace these values with your actual data
        Map<String, String> data = new HashMap<>();
        data.put("Numéro", String.format("%012d", b.getCodeBon()));
        data.put("Date", String.valueOf(LocalDate.now()));
        data.put("Heure", String.valueOf(LocalTime.now()));
        data.put("Nom", e.getNom());
        data.put("Prenom", e.getPrenom());
        data.put("Motif", b.getMotif());

        try {
            // Load existing PDF
            PdfReader reader = new PdfReader("C:\\Users\\Abidine\\Documents\\NetBeansProjects\\StageApp\\src\\Utils\\BonEs.pdf");
            FileOutputStream outputStream = new FileOutputStream(new File("C:\\Users\\Abidine\\Documents\\NetBeansProjects\\StageApp\\src\\Utils\\output.pdf"));
            PdfStamper stamper = new PdfStamper(reader, outputStream);

            // Get the size of the page
            int pageNumber = 1; // Page number to add the image
            int pageWidth = (int) reader.getPageSize(pageNumber).getWidth();
            int pageHeight = (int) reader.getPageSize(pageNumber).getHeight();

            // Calculate the position to center the image horizontally
            int imageWidth = 200; // Adjust this value based on the width of your image
            int imageX = (pageWidth - imageWidth) / 2;

            // Get the content byte to add the image
            PdfContentByte contentByte = stamper.getOverContent(pageNumber);

            // Load the image
            Image image = Image.getInstance(imagePath);

            // Set position and scale of the image
            image.setAbsolutePosition(imageX, 100); // Y position (adjust as needed)
            image.scaleAbsolute(imageWidth, 100); // Height (adjust as needed)

            // Add the image to the PDF content
            contentByte.addImage(image);

            // Get form fields
            AcroFields formFields = stamper.getAcroFields();

            // Replace placeholders with data
            for (Map.Entry<String, String> entry : data.entrySet()) {
                formFields.setField(entry.getKey(), entry.getValue());
            }

            // Close stamper and reader
            stamper.close();
            reader.close();
            outputStream.close();

            System.out.println("PDF modified successfully.");
        } catch (IOException | DocumentException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // Create an instance of Employe
        Employe employe = new Employe(1, "John", "Doe", LocalDate.MIN, 0);

        // Create an instance of Bon
        Bon bon = new Bon(1, employe, "S", LocalDate.MIN, LocalTime.NOON, LocalTime.MIN, "Familial issues", 'V');

        // Call the makePDF method from PDFUtil
        makePDF(employe, bon, "C:\\Users\\Abidine\\Documents\\NetBeansProjects\\StageApp\\barcode.png");
        Desktop.getDesktop().open(new File("C:\\Users\\Abidine\\Documents\\NetBeansProjects\\StageApp\\src\\Utils\\output.pdf"));
    }
    
    
    
    
}