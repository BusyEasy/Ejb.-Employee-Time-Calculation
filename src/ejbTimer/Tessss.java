package ejbTimer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Base64;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;





public class Tessss {
	
	
	   public void GetTwoRequest() throws IOException, JSONException {


	        String urlIp = "http://192.168.3.207:8080/Synergy/rest/api/registry/data_ext?registryCode=fom_registry";
	        String username = "Алиев";
	        String password = "1";
	        String auth = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());
	        
	        GetTwoRequest jsnJava = new GetTwoRequest();

	        String result = "";

	        BufferedReader reader = null;
	        URL url = null;

	        try {

	            url = new URL(urlIp);
	            URLConnection connection = url.openConnection();
	            connection.setRequestProperty("Authorization", auth);
	            connection.connect();

	            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String line = "";

	            while ((line = reader.readLine()) != null) {

	                result += line;

	            }

	        } catch (Exception e) {

	            e.printStackTrace();

	        } finally {
	            if (reader != null) {
	                reader.close();
	            }
	        }


	        JSONObject myResponse = new JSONObject();
	        JsonParser parser = new JsonParser();
	        JsonObject mainObject = parser.parse(result).getAsJsonObject();
	        JsonArray ar = new JsonArray();

	        ar = mainObject.getAsJsonArray("data");


	        String dataUUID = "dataUUID";
	        String dataUD = null;
	        for (JsonElement element : ar) {

	            JsonObject goblin = element.getAsJsonObject();

	            dataUD = goblin.get(dataUUID).getAsString();

	            jsnJava.GetRequest(dataUD);


	        }


	    }



	//GetTwo Continue

	    class GetTwoRequest {


	        public JsonArray GetRequest(String get) throws JSONException, IOException { //GET запрос

	            String[] curDate = DateFormat.getDateInstance().format(new Date()).split("\\.");
	            int sumSignificanceAll = 0;
	            int sumSignificanceTillToDay = 0;
	            int sumSignificanceCompleted = 0;
	            double fact = 0;
	            double plan = 0;
	            double difference = 0;
	            int indexCMP = 1;


	            String url = "http://192.168.3.207:8080/Synergy/rest/api/asforms/data/" + get;
	            String username = "Алиев";
	            String password = "1";
	            String authString = username + ":" + password;
	            System.out.println("auth string: " + authString);
	            byte[] encodedBytes = Base64.getEncoder().encode(authString.getBytes());
	            String authStringEnc = new String(encodedBytes);
	            System.out.println("Base64 encoded auth string: " + authStringEnc);

	            System.out.println();

	            String result = "";
	            BufferedReader in = null;
	            try {
	                String urlNameString = url;
	                URL realUrl = new URL(urlNameString);
	                URLConnection connection = realUrl.openConnection();
	                connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
	                connection.connect();
	                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String line;
	                while ((line = in.readLine()) != null) result += line;
	            } catch (Exception e) {
	                System.out.println(e);
	                e.printStackTrace();
	            } finally {
	                try {
	                    if (in != null) in.close();
	                } catch (Exception e2) {
	                    e2.printStackTrace();
	                }
	            }
	            JSONObject  myResponse = new JSONObject(result.toString()); // JSONObject myResponse;
	            JsonParser  parser = new JsonParser(); // JsonParser parser;
	            JsonObject mainObject = parser.parse(result).getAsJsonObject(); // JsonObject mainObject;
	            JsonArray ar = new JsonArray();

	            ar = mainObject.getAsJsonArray("data"); // JsonArray ar;

	            String table = "table1";

	            for (JsonElement user : ar) {

	                JsonObject userObject = user.getAsJsonObject();

	                if (userObject.get("id").getAsString().equals(table)) {
	                    JsonArray tblData = userObject.getAsJsonArray("data");
	                    String deadLineDate = null;

	                    int weiTmp = 0;

	                    for (JsonElement objJson2 : tblData) {
	                        JsonObject tblCMP = objJson2.getAsJsonObject();
	                        String componentID = tblCMP.get("id").getAsString();
	                        String[] regex = componentID.split(".+\\-b");
	                        indexCMP = regex.length > 1 && Integer.parseInt(regex[1]) > indexCMP ? Integer.parseInt(regex[1]) : indexCMP;
	                        String index = String.valueOf(indexCMP);

	                        if (componentID.equals("deadline-b" + index)) {
	                            String deadline = tblCMP.get("value").getAsString();
	                            deadLineDate = deadline;
	                        }

	                        if (componentID.equals("weight-b" + index)) {

	                            String weight = tblCMP.get("value").getAsString();
	                            weiTmp = Integer.parseInt(weight);

	                            sumSignificanceAll += Integer.parseInt(weight);
	                            System.out.println("sumSignificanceAll: "+sumSignificanceAll);

	                            // Алг. sumSignificanceAll
	                            if (deadLineDate != null) {

	                                String[] date = deadLineDate.split("\\."); // [31, 08, 2017 г]
	                                String[] year = date[2].split(" "); // [2017, г]

	                                if (Integer.parseInt(date[1]) == Integer.parseInt(curDate[1]) && Integer.parseInt(curDate[2]) == Integer.parseInt(year[0]) && Integer.parseInt(date[0]) < Integer.parseInt(curDate[0]))
	                                    sumSignificanceTillToDay += Integer.parseInt(weight); // Алг. sumSignificanceTillToDay
	                                System.out.println("sumSignificanceTillToDay: "+sumSignificanceTillToDay);
	                            }
	                        }
	                        if (componentID.equals("end-b" + index)) {

	                            String end = tblCMP.getAsJsonArray("values").get(0).toString();

	                            if (!end.equals("null")) {

	                                sumSignificanceCompleted += weiTmp; // Алг.
	                                System.out.println("sumSignificanceCompleted: "+ sumSignificanceCompleted);

	                            }

	                        }
	                    }
	                }
	            }


	            fact = ((double) sumSignificanceCompleted / (double) sumSignificanceAll) * 100;
	            plan = ((double) sumSignificanceTillToDay / (double) sumSignificanceAll) * 100;

	            int factInt = 0;
	            int planInt = 0;
	            int differenceInt = 0;

	            factInt = Math.toIntExact(Math.round(fact));
	            planInt = Math.toIntExact(Math.round(plan));
	            differenceInt = factInt - planInt;


	            for (JsonElement objJson1 : ar) {
	                JsonObject formCMP = objJson1.getAsJsonObject();
	                if (formCMP.get("id").getAsString().equals("fact")) {
	                    formCMP.addProperty("value", String.valueOf(factInt));
	                } else if (formCMP.get("id").getAsString().equals("plan")) {
	                    formCMP.addProperty("value", String.valueOf(planInt));
	                } else if (formCMP.get("id").getAsString().equals("difference")) {
	                    formCMP.addProperty("value", String.valueOf(differenceInt));
	                }
	            }
	           Tessss jsnJava = new Tessss();

	            jsnJava.RequestPost(get, ar);
	            return ar;
	        }


	    }
	    
	    
	    
	    public void RequestPost(String get, JsonArray array) throws IOException, JSONException {

	        String data = String.valueOf(array);

	        System.out.println(data);


	        String url = "http://192.168.3.207:8080";
	        String username = "�����";
	        String password = "1";
	        String authString = username + ":" + password;
	        System.out.println("auth string: " + authString);
	        String auth = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

	        StringBuilder sb = new StringBuilder();
	        HttpURLConnection urlConnection=null;

	        try {
	            URL url1 = new URL(url+"/Synergy/rest/api/asforms/data/save");
	            urlConnection = (HttpURLConnection) url1.openConnection();
	            urlConnection.setDoOutput(true);
	            urlConnection.setRequestMethod("POST");
	            urlConnection.setUseCaches(false);
	            urlConnection.setRequestProperty("Authorization", auth);
	            urlConnection.setRequestProperty("Content-Type",
	                    "application/x-www-form-urlencoded;charset=utf-8");

	            urlConnection.setConnectTimeout(10000);
	            urlConnection.setReadTimeout(10000);

	            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());

	            out.writeBytes("uuid=" +get);
	            out.writeBytes("&");

	            out.writeBytes("data=\"data\":");
	            out.write(URLEncoder.encode(data, "UTF-8").getBytes("UTF-8"));

	            out.flush();
	            out.close();

	            int HttpResult =urlConnection.getResponseCode();
	            if(HttpResult ==HttpURLConnection.HTTP_OK){
	                BufferedReader br = new BufferedReader(new InputStreamReader(
	                        urlConnection.getInputStream(),"utf-8"));

	                String line = null;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line + "\n");
	                }
	                br.close();

	                System.out.println(""+sb.toString());

	            }else{
	                System.out.println(urlConnection.getResponseMessage());
	            }
	        } catch (MalformedURLException e) {

	            e.printStackTrace();
	        }
	        catch (IOException e) {

	            e.printStackTrace();
	        } finally {
	            if (urlConnection != null)
	                urlConnection.disconnect();


	        }



	    }
	
	
}
