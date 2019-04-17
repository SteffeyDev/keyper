public class JsonIo {

    public static class MyThread extends Thread implements Runnable
    {
        String url, payload;
        public MyThread( String url, String payload )
        {
            this.url = url;
            this.payload = payload;
        }

        @Override
        public void run()
        {
            String ret = sendPostRequest(url, payload);
            try
            {
                jobj = new JSONObject(ret);
            }
            catch(Exception ex){}
        }
    }

    public static JSONObject jobj = null;
    public static JSONObject doJsonIo( String url, String payload )
    {
        jobj = null;

        MyThread thd = new MyThread( url, payload );
        thd.start();

        int count = 0;
        int delay = 10;
        while( count * delay < 5000 && jobj == null )
        {
            try
            {
                Thread.sleep(delay);
            }
            catch(Exception ex){}
            count++;
        }

        return jobj;
    }

    public static String sendPostRequest(String requestUrl, String payload)
    {
        try
        {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null)
            {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }

}
