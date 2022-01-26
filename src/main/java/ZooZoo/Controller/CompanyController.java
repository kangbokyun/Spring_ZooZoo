package ZooZoo.Controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Controller
public class CompanyController {

    @GetMapping("/")
    public String CompanyCheck(){
    /*    try{
            URL url =  new URL("https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=hm1u3zRV0ba96YTa5BqV4zu0jYFV2LGfPe2aRk0NyJVQsoX5FCSjuVth8RKvBvQzOW8ApIHwaxmajW9%2FRaYR5A%3D%3D");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
            String result = bufferedReader.readLine();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            System.out.println(jsonObject);

            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
            System.out.println(jsonArray);
            for(int i =0; i<jsonArray.size(); i++){
                JSONObject obj = (JSONObject) jsonArray.get(i);
                System.out.println(obj);
            }
        }catch(Exception e){

        }*/

        return "Main";
    }

}
