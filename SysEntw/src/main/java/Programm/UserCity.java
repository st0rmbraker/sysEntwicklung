package Programm;

//benötigt um bilder in binary data zu konvertieren
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class UserCity extends Helpers{

    public static void main(String[] args) {
        Helpers h = new Helpers();
//        byte[] x = h.convertToBinary();
//        h.saveImage(h.convertToBinary());
//        System.out.println("Fertig");
//       // h.convertToImg(x);
//        System.out.println("abc");
//        h.convertToImg(h.getPictureByName("test4"));

        ODocument chat = h.getChat(h.getVertexByUsername("atheob"), h.getVertexByUsername("Sophie"));
        ODocument message = h.createMessage(h.getVertexByUsername("atheob"), "Das hier ist ja schon ein richtiger Chat");

        h.addMessageToChat(message, chat);
    }



}
