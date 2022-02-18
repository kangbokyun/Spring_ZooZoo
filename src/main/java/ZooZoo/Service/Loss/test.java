package ZooZoo.Service.Loss;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
        Date time = new Date();
        String current = format.format(time);
        Date today = null;
        try {
            today = format.parse(current);
        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date end = null;
        try {
            end = format.parse("2022-02-19");
        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        int result = today.compareTo(end);

        System.out.println("결과 : " + result);



    }
}
