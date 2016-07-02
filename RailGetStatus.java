import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Bhaskar
 */
public class RailGetStatus {
    public static final String API_KEY="*********"; //enter your apikey 
    public String showPosition(String trainno,String date) throws JSONException {
        JSONObject obj = null;
        String responsecode = "-";
        String str="";
        int count=1;
        while (count<=2) { //keep on pinging till you get a response but no of trials limited to 3
            System.out.println(date);
            str = getLocation(trainno,date);
            System.out.println(str);
            obj = new JSONObject(str);      
            responsecode = obj.getString("error");
            if(responsecode.length()>0)
                break;
            
            if(!obj.getString("position").equals("-"))
                break;
            count++;
        }
        String ret = "";
       if(responsecode.equals("")){ 
        ret = obj.getString("position");
       }
       else{
           ret = obj.getString("error");
       }
       return ret;
    }

    public int showPNRStatus(String pnr) throws JSONException {
        JSONObject obj = null;
        String responsecode = "400";
        String str="";
        int count=1;
        while (!responsecode.equals("200") && count<=2) { //keep on pinging till you get a response but no of trials limited to 3
            str = getPnrResponse(pnr);
            obj = new JSONObject(str);
            responsecode = obj.getString("response_code");
            System.out.println(responsecode);
            count++;
        }
        
       if(responsecode.equals("200")){
       
           System.out.println("HAVE A HAPPY JOURNEY!");
           System.out.println("TICKET DETAILS :-");
        String trainno = obj.getString("train_num");
  //      System.out.println(trainno);
  //      JSONArray fromarr = obj.getJSONObject("from_station").getJSONArray("people");
  
//String fromstn = new JSONObject("from_station").getString("name") + "(" + new JSONObject("from_station").getString("code") + ")";
        String classnm = obj.getString("class");
        String numpass = obj.getString("total_passengers");
        String doj = obj.getString("doj");
        String error = obj.getString("error");
        String chartmade = obj.getString("chart_prepared");
       // String reserupto = new JSONObject("reservation_upto").getString("name") + "(" + new JSONObject("reservation_upto").getString("code") + ")";
        String trainname = obj.getString("train_name");
        String[] currstat = new String[6];
        String pass = obj.getString("passengers");
        currstat = pass.split(",");
        String status = "";
        
        JSONArray stat = obj.getJSONArray("passengers");
        for(int i = 0; i<stat.length();i++){
            JSONObject childjson = stat.getJSONObject(i);
            String st = childjson.getString("current_status");
            status = status + st+","; 
        }
        status =  status.substring(0,status.length()-1);
        System.out.println("TRAIN NO : "+trainno);
        System.out.println("TRAIN NAME : "+trainname);
        System.out.println("CLASS : " +classnm);  
        System.out.println("NO OF PASSENGERS : "+numpass);
        System.out.println("DATE OF JOURNEY : "+doj);
        String chart = "";
        if(chartmade.equals("Y")){
            chart = "YES";
        System.out.println("CHART MADE : "+"YES");}
        else{
            chart = "NO";
        System.out.println("CHART MADE : "+"NO");}
        System.out.println("STATUS :"+status);
        new PNRCheck(pnr,trainno,trainname,classnm,numpass,doj,chart,status).setVisible(true);
        return 1;
       }
       else{
           System.out.println("Please try again");
       }
       return 0;
    }
    
    public static String getPnrResponse(String pnr) {

        String endpoint = "http://api.railwayapi.com/pnr_status/pnr/" + pnr + "/apikey/" + API_KEY + "/";
        HttpURLConnection request = null;
        BufferedReader rd = null;
        StringBuilder response = null;

        try {
            URL endpointUrl = new URL(endpoint);
            request = (HttpURLConnection) endpointUrl.openConnection();
            request.setRequestMethod("GET");
            request.connect();

            rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
            response = new StringBuilder();
            String line = null;
            while ((line = rd.readLine()) != null) {
                response.append(line + "\n");
            }
        } catch (MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
            //e.printStackTrace();
        } catch (ProtocolException e) {
            System.out.println("Exception: " + e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            //e.printStackTrace();
        } finally {
            try {
                request.disconnect();
            } catch (Exception e) {
            }

            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException ex) {
                }
                rd = null;
            }
        }
        return response != null ? response.toString() : "No Response";
    }
    
    public static String getLocation(String trainnum,String date) {

     
        String endpoint = "http://api.railwayapi.com/live/train/"+trainnum+"/doj/"+date+"/apikey/"+API_KEY+"/";
        HttpURLConnection request = null;
        BufferedReader rd = null;
        StringBuilder response = null;

        try {
            URL endpointUrl = new URL(endpoint);
            request = (HttpURLConnection) endpointUrl.openConnection();
            request.setRequestMethod("GET");
            request.connect();

            rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
            response = new StringBuilder();
            String line = null;
            while ((line = rd.readLine()) != null) {
                response.append(line + "\n");
            }
        } catch (MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
            //e.printStackTrace();
        } catch (ProtocolException e) {
            System.out.println("Exception: " + e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            //e.printStackTrace();
        } finally {
            try {
                request.disconnect();
            } catch (Exception e) {
            }

            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException ex) {
                }
                rd = null;
            }
        }
        return response != null ? response.toString() : "No Response";
    }
}
