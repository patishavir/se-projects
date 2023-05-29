package oz.temp.spnego;
import java.net.HttpURLConnection;
import java.net.URL;

import org.ietf.jgss.Oid;

import com.sun.org.apache.xml.internal.security.utils.Base64;


public class Xxxx {

     /**
     * @param args
     */
     public static void main(String[] args) {
          try{
              System.out.println("start");
              Oid oid = new Oid("1.2.840.113554.1.2.2");
              String auth = Base64.encode(oid.getDER());
              System.out.println(auth);
              System.out.println("end");
              URL url = new URL("https://intramgmt.fibi.corp:9045/ibm/console");
              HttpURLConnection conn = (HttpURLConnection)url.openConnection();
              // 200 OK ; 401 Need Login
// skip to get 401
conn.setRequestProperty("Authorization", auth);
              conn.connect();
              System.out.println(conn.getResponseCode());
              
          }catch(Exception e){e.printStackTrace();}
     }

}
