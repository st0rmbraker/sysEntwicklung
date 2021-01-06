package Programm;

import com.orientechnologies.orient.core.record.OVertex;

import java.sql.Date;



public class FunctionTestClass {
    public static void main(String[] args) {
        Helpers h = new Helpers();

        System.out.println("Von der Print: " + h.getUsersFromLand("de"));

        OVertex user = h.getVertexByUsername(h.getUsersFromLand("de"));

        user.setProperty("firstName", "Erfolg");

        user.save();
    }
}
