package res;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class R {

    public static InputStream bonEntre() {
        return getResourceStream("B.pdf");
    }

    public static InputStream bonSStream() {
        return getResourceStream("BonS.pdf");
    }

    private static InputStream getResourceStream(String fileName) {
        return R.class.getResourceAsStream(fileName);
    }

    public static final String BASE_DIR = System.getProperty("user.home") + File.separator + "Documents\\NetBeansProjects\\sysGB";
    public static final String OUTPUT_PDF_DIR = BASE_DIR + File.separator + "output.pdf";
    public static final String BARCODE_DIR = BASE_DIR + File.separator + "barcode.png";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "3152002";
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/sysGB";
    public static Connection DB_CON;
    
    
    public static Connection getConnection() throws ClassNotFoundException, SQLException{
        if(DB_CON==null) {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        
        }
        return DB_CON;
    }

}
