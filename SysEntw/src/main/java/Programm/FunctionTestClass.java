package Programm;

import java.sql.Date;

public class FunctionTestClass {
    public static void main(String[] args) {
        Helpers h = new Helpers();

        h.createUserInfos("Mannheim", "mail@mail.mail", new Date(2000,4,26));
    }
}
