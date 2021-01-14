package Programm;

//benötigt um bilder in binary data zu konvertieren
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class UserCity {

    public static void main(String[] args) {
        Helpers h = new Helpers();
        byte[] x = h.convertToBinary();
        h.saveImage(h.convertToBinary());
        System.out.println("Fertig");
        h.convertToImg(x);
    }



}
