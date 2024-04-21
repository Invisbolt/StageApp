package res;

import java.io.File;
import java.io.InputStream;
public class R {
    
    public static InputStream bonEsStream() {
        return getResourceStream("BonES.pdf");
    }

    public static InputStream bonSStream() {
        return getResourceStream("BonS.pdf");
    }

    private static InputStream getResourceStream(String fileName) {
        return R.class.getResourceAsStream(fileName);
    }
    
    public static final String BASE_DIR= System.getProperty("user.home")+File.separator+"stageapp";
    public static final String OUTPUT_PDF_DIR= BASE_DIR+File.separator+"output.pdf";
    public static final String BARCODE_DIR= BASE_DIR+File.separator+"barcode.png";


    
}