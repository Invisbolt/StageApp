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
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import res.R;

public class PDFUtil {

    public static void makePDF(Employe e, Bon b, String imagePath, InputStream boninput) throws URISyntaxException {
        // Replace these values with your actual data
        Map<String, String> data = new HashMap<>();
        String code_m = String.format("%012d", b.getCodeBon());
        data.put("Numero", code_m.substring(0, code_m.length() - 1));
        data.put("Date", String.valueOf(b.getDateBon()));
        data.put("Heure", String.valueOf(b.getHeureBon()));
        data.put("Nom", e.getNom());
        data.put("Prenom", e.getPrenom());
        data.put("Motif", b.getMotif());

        try {
            // Load existing PDF
            PdfReader reader = new PdfReader(boninput);
            // Output PDF file path
            String outputPath = R.BASE_DIR + File.separator + "output.pdf";
            // Create the directory if it doesn't exist
            File outputDir = new File(R.BASE_DIR);
            if (!outputDir.exists()) {
                outputDir.mkdirs(); // Creates the directory and any necessary parent directories
            }
            FileOutputStream outputStream = new FileOutputStream(new File(outputPath));
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
            // Get form fields

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

    public static void main(String[] args) throws IOException, URISyntaxException {
        // Create an instance of Employe
        Employe employe = new Employe(1, "John", "Doe", LocalDate.MIN, 0);

        // Create an instance of Bon
        Bon bon = new Bon(25, employe, "S", LocalDate.MIN, LocalTime.NOON, LocalTime.MIN, "Familial issues", 'V');

        // Call the makePDF method from PDFUtil
        makePDF(employe, bon, R.BARCODE_DIR, R.bonSStream());
        Desktop.getDesktop().open(new File(R.BASE_DIR + File.separator + "output.pdf"));
    }

    public static void main(Bon bon, Employe emp) throws IOException, URISyntaxException {
        System.out.println(String.valueOf(bon.getCodeBon()));
        String fullnum = CalculatorCheck.fullDigit(String.valueOf(bon.getCodeBon()));
        System.out.println(fullnum);
        bon.setCodeBon(Integer.valueOf(fullnum));
        makePDF(emp, bon, R.BARCODE_DIR, R.bonSStream());
        Desktop.getDesktop().open(new File(R.BASE_DIR + File.separator + "output.pdf"));
    }

}
