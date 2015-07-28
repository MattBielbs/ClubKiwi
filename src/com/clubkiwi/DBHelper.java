package com.clubkiwi;
import com.clubkiwi.Character.Accessory;
import com.clubkiwi.Character.Kiwi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mathew on 7/28/2015.
 * This will hold all the http requests to the api file pretty much
 */
public class DBHelper
{
    //private String api = "http://matypatty.zapto.org/clubkiwi/api.php";
    private String api = "http://winserver-pc/clubkiwi/api.php";

    //Used to hold the login info once login is a success so it can be used in other requests. (tokens are too hard)
    private String username, password, userid;
    private boolean bLoggedIn = false;

    public boolean Login(String username, String password)
    {
        String response = doPost(api, "Action=login&username=" + username + "&password=" + password);
        if(response.compareToIgnoreCase("invalid") == 0)
            return false;

        else
        {
            this.bLoggedIn = true;
            this.userid = response;
            this.username = username;
            this.password = password;
        }

        return true;
    }

    //returns empty array if no characters present.
    public ArrayList<Kiwi> GetCharacters() throws IllegalStateException
    {
        if(!bLoggedIn)
            throw new IllegalStateException("You need to be logged in to get characters");

        ArrayList<Kiwi> temp = new ArrayList<Kiwi>();

        String response = doPost(api, "Action=characters&uid=" + userid);
        if(response.isEmpty())
            return temp;

        String[] holder = response.split("/");
        int nChars = Integer.parseInt(holder[0]);

        for(int i = 1; i < nChars * 12; i += 12)
            temp.add(new Kiwi(Integer.parseInt(holder[i]), holder[i + 1], Double.parseDouble(holder[i + 2]), Double.parseDouble(holder[i + 3]), Double.parseDouble(holder[i + 4]), Double.parseDouble(holder[i + 5]), Double.parseDouble(holder[i + 6]), Double.parseDouble(holder[i + 7]), Double.parseDouble(holder[i + 8]), Double.parseDouble(holder[i + 9]), Double.parseDouble(holder[i + 10])));

        return temp;
    }

    //returns empty array if no accessories present.
    public ArrayList<Accessory> GetAccessories() throws IllegalStateException
    {
        if(!bLoggedIn)
            throw new IllegalStateException("You need to be logged in to get accessories");

        ArrayList<Accessory> temp = new ArrayList<Accessory>();

        String response = doPost(api, "Action=accessories");
        if(response.isEmpty())
            return temp;

        String[] holder = response.split("/");
        int nAccs = Integer.parseInt(holder[0]);

        for(int i = 1; i < nAccs * 6; i += 6)
        {
            //Build hashmaps
            HashMap<String, Double> sReqs = new HashMap<String, Double>();
            HashMap<String, Double> sBoosts = new HashMap<String, Double>();

            String[] rtemp = holder[i+4].split(",");
            sReqs.put("Strength", Double.parseDouble(rtemp[0]));
            sReqs.put("Speed", Double.parseDouble(rtemp[1]));
            sReqs.put("Flight", Double.parseDouble(rtemp[2]));
            sReqs.put("Swag", Double.parseDouble(rtemp[3]));

            rtemp = holder[i+5].split(",");
            sBoosts.put("Hunger", Double.parseDouble(rtemp[0]));
            sBoosts.put("Social", Double.parseDouble(rtemp[1]));
            sBoosts.put("Energy", Double.parseDouble(rtemp[2]));

            temp.add(new Accessory(Integer.parseInt(holder[i]), holder[i+1], holder[i + 2], Double.parseDouble(holder[i + 3]), sReqs, sBoosts));
        }

        return temp;
    }

    //Borrowed from stackoverflow
    public static String doPost(String targetURL, String urlParameters)
    {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            connection.disconnect();
            return response.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if(connection != null)
                connection.disconnect();
        }
    }
}
