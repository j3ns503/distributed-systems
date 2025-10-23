import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class URLpackageRequest {
    public void sendURLRequest() {
        try {
            String internetHost = "http://fh-wedel.de:80";
            String intranetHost = "http://stud.fh-wedel.de:80";
            URL url = new URL(internetHost);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            System.out.println("Status: " + status);
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();

            con.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendURLRequestThroughProxy() {
        try {
            String internetHost = "http://fh-wedel.de:80";
            String intranetHost = "http://stud.fh-wedel.de:80";
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.example.com", 8080));
            URL url = new URL(internetHost);
            HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            System.out.println("Status: " + status);
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();

            con.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
