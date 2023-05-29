package mataf.reuters;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import mataf.cryptography.oleg.UsrPwd;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;



public class RI2API
{
    private static int  connectionTimeOut = 5000; // 5 seconds
    private static int  socketReadTimeOut = 30000; // 30 seconds
    private static byte XML_HEADER_BYTES[] = "<?xml version=\"1.0\" encoding=\"utf-8\"?>".getBytes();

    private static long maxSessionTime = 3600000; // 1 hour

    private static Exception initEx = null;

    private static String usr = null;
    private static String pwd = null;
    private static String appId = null;
    private static String host = null;

    private static String getTokenRequestTemplate = null;
    private static String getTokenURL = null;

    private static String snapRequestTemplate = null;
    private static String snapURL = null;

    private static String searchByNameRequestTemplate = null;
    private static String searchByNameURL = null;

    private static String searchBySymbRequestTemplate = null;
    private static String searchBySymbURL = null;

    private static String getChart1D_Template = null;
    private static String getChart1W_Template = null;
    private static String getChart3M_Template = null;
    private static String getChart6M_Template = null;
    private static String getChart1Y_Template = null;
    private static String getChartURL = null;


    static
    {
        Properties p = new Properties();
        try
        {	
            ResourceBundle reutersConfig = ResourceBundle.getBundle( "TRDK" );
            Enumeration enm = reutersConfig.getKeys();
            while( enm.hasMoreElements() )
            {
                String key = (String)enm.nextElement();
                p.setProperty( key, reutersConfig.getString(key) );
            }
        }
        catch (Exception ex)
        {
            initEx = new Exception( "Can't get ResourceBundle \"TRDK\"", ex ); 
            ex.printStackTrace();
        }
        String usrPwd = null;
        if ( initEx == null )
        {
            usrPwd = p.getProperty("UPTOKEN");
            if (usrPwd == null   ||   usrPwd.length() == 0)
                initEx = new Exception( "Parameter 'UPTOKEN' is absent in ResourceBundle" );
        }
        if ( initEx == null )
        {
            try
            {
                UsrPwd up = new UsrPwd(usrPwd);
                usr = up.getUser().trim();
                pwd = up.getPwd().trim();        
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Error during UPToken decryption: " + ex, ex );
            }
        }
        if ( initEx == null )
        {
            if ( (appId = p.getProperty( "APPID" )) == null )
                initEx = new Exception( "Parameter 'APPID' is absent in ResourceBundle" );
        }
        if ( initEx == null )
        {
            if ( (host = p.getProperty( "HOST" )) == null )
                initEx = new Exception( "Parameter 'HOST' is absent in ResourceBundle" );
        }
        if ( initEx == null )
        {
            try
            {
                getTokenRequestTemplate = getFile( p, "GetTokenRequest" );
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Wrong parameter 'GetTokenRequest' in ResourceBundle", ex );
            }
        }
        if ( initEx == null )
        {
            if ( (getTokenURL = p.getProperty( "GetTokenURL" )) == null )
                initEx = new Exception( "Parameter 'GetTokenURL' is absent in ResourceBundle" );
        }
        if ( initEx == null )
        {
            try
            {
                snapRequestTemplate = getFile( p, "SnapRequest" );
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Wrong parameter 'SnapRequest' in ResourceBundle", ex );
            }
        }
        if ( initEx == null )
        {
            if ( (snapURL = p.getProperty( "SnapURL" )) == null )
                initEx = new Exception( "Parameter 'SnapURL' is absent in ResourceBundle" );
        }
        if ( initEx == null )
        {
            try
            {
                searchBySymbRequestTemplate = getFile( p, "SearchBySymbolRequest" );
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Wrong parameter 'SearchBySymbolRequest' in ResourceBundle", ex );
            }
        }
        if ( initEx == null )
        {
            if ( (searchBySymbURL = p.getProperty( "SearchBySymbolURL" )) == null )
                initEx = new Exception( "Parameter 'SearchBySymbolURL' is absent in ResourceBundle" );
        }
        if ( initEx == null )
        {
            try
            {
                searchByNameRequestTemplate = getFile( p, "SearchByNameRequest" );
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Wrong parameter 'SearchByNameRequest' in ResourceBundle", ex );
            }
        }
        if ( initEx == null )
        {
            if ( (searchByNameURL = p.getProperty( "SearchByNameURL" )) == null )
                initEx = new Exception( "Parameter 'SearchByNameURL' is absent in ResourceBundle" );
        }
        if ( initEx == null )
        {
            try
            {
                getChart1D_Template = getFile( p, "RequestChart1D" );
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Wrong parameter 'RequestChart1D' in ResourceBundle", ex );
            }
        }
        if ( initEx == null )
        {
            try
            {
                getChart1W_Template = getFile( p, "RequestChart1W" );
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Wrong parameter 'RequestChart1W' in ResourceBundle", ex );
            }
        }
        if ( initEx == null )
        {
            try
            {
                getChart3M_Template = getFile( p, "RequestChart3M" );
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Wrong parameter 'RequestChart3M' in ResourceBundle", ex );
            }
        }
        if ( initEx == null )
        {
            try
            {
                getChart6M_Template = getFile( p, "RequestChart6M" );
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Wrong parameter 'RequestChart6M' in ResourceBundle", ex );
            }
        }
        if ( initEx == null )
        {
            try
            {
                getChart1Y_Template = getFile( p, "RequestChart1Y" );
            }
            catch( Exception ex )
            {
                initEx = new Exception( "Wrong parameter 'RequestChart1Y' in ResourceBundle", ex );
            }
        }
        if ( initEx == null )
        {
            if ( (getChartURL = p.getProperty( "ChartURL" )) == null )
                initEx = new Exception( "Parameter 'ChartURL' is absent in ResourceBundle" );
        }
    }



    private String token = null;
    private long tokenExpTime;



    private synchronized void checkToken() throws Exception
    {
        if ( initEx != null )
            throw initEx;
        if ( (token != null)     &&     (System.currentTimeMillis() < tokenExpTime) )
            return;
        tokenExpTime = System.currentTimeMillis() + maxSessionTime;
        token = null;
        Properties prm = new Properties();
        prm.setProperty( "MSGID", getNewMsgId() );
        prm.setProperty( "APPID", appId );
        prm.setProperty( "USR", usr );
        prm.setProperty( "PWD", pwd );
        InputStream is = sendRequest( getTokenURL, substitute( getTokenRequestTemplate, prm ) );
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(is);
        if ( document == null )
            throw new Exception( "XML Document in response to get_token is null" );
/* DEBUG */
/*
        XMLOutputter outputer = new XMLOutputter();
        Format format = Format.getCompactFormat();
        format.setEncoding("utf-8");
        format.setOmitDeclaration( true );
        outputer.setFormat(format);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        outputer.output(document, baos);
        System.out.println( "XML Document in response to get_token is:[" +
                            (new String(baos.toByteArray(),"utf-8")) + "]" );
*/
        XPath xp = XPath.newInstance("//global:Token");
        xp.addNamespace( "global", "http://www.reuters.com/ns/2006/05/01/webservices/rkd/Common_1" );
        Element tokenEl = (Element) xp.selectSingleNode(document.getRootElement());
        if ( tokenEl == null )
            throw new Exception( "XML Document in response to get_token does not contain a token" );
        is.close();
        token = tokenEl.getTextTrim();
    }



    private static String getFile( Properties p, String propName ) throws Exception
    {
        String fName = p.getProperty( propName );
        if ( fName == null )
            throw new Exception( "Parameter '" + propName + "' is absent in ResourceBundle" );
        FileInputStream fis = new FileInputStream( fName );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buf[] = new byte[409600];
        int k;
        while ((k = fis.read(buf)) > 0)
        {
            baos.write(buf, 0, k);
        }
        fis.close();
        return (baos.toString());
    }



    private byte[] getChart(String RIC, String getChartTemplate) throws Exception
    {
        checkToken();
        Properties prm = new Properties();
        prm.setProperty( "MSGID", getNewMsgId() );
        prm.setProperty( "APPID", appId );
        prm.setProperty( "TOKEN", token );
        prm.setProperty( "RIC", RIC );
        String request = substitute( getChartTemplate, prm );
        // Get XML with HTTP-address of chart from REUTERS:
        byte[] b = getResponseBodyBytes( sendRequest( getChartURL, request ), false );
        // Get HTTP-address from response:
        String s = new String( b );
        int i = -1;
        int i1 = -1;
        if ( (i = s.toLowerCase().indexOf( "url=\"" )) > 0 )
        {
            i += 5;
            i1 = s.indexOf( "\"", i );
        }
        if ( i1 <= 0 )
            throw new Exception( "Wrong response on chart request: [" + s + "]" );
        s = s.substring( i, i1 );
        // Get bytes with image and return them
        return getResponseBodyBytes( sendRequest( s, null ), false );
    }



    private synchronized String getNewMsgId()
    {
        return "FIBI_MSGID_" + Thread.currentThread().getId() + "_" + System.currentTimeMillis();
    }



    private static byte[] getResponseBodyBytes(InputStream is, boolean withXmlHeader ) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if ( withXmlHeader )
        	baos.write(XML_HEADER_BYTES);
        byte buf[] = new byte[409600];
        int k;
        while ((k = is.read(buf)) > 0)
        {
            baos.write(buf, 0, k);
        }
        is.close();
        return (baos.toByteArray());
    }


    
    private static InputStream sendRequest(String urlString, String body) throws IOException
    {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout( connectionTimeOut );
        connection.setReadTimeout( socketReadTimeOut );
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setAllowUserInteraction(false);
        connection.setRequestMethod( (body == null) ? "GET" : "POST");
        connection.setRequestProperty("Host", host);
        if (body != null)
        {
            connection.setRequestProperty("Content-Type", "application/soap+xml");
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
            OutputStreamWriter os = new OutputStreamWriter(new BufferedOutputStream(connection.getOutputStream()));
            os.write(body);
            os.flush();
        }
        return new BufferedInputStream(connection.getInputStream());
    }



    /*
     *   Substitutes texts in the first parameter (template) according to the second
     * parameter (substs).
     *   All texts for substitution inside of TEMPLATE (first parameter) are looks like
     *       {$$some_text}   where "some_text" is some text and will be replaced by
     *       substs.getProperty( some_text, "" )
     */
    public static String substitute(String template, Properties substs)
    {
        if (template == null)
            return null;
        StringBuffer sb = new StringBuffer();
        int start = 0;
        while( true )
        {
            int i = template.indexOf("{$$", start);
            if (i < 0)
                break;
            sb.append( template.substring(start, i) );
            start = i;
            i = template.indexOf( "}", start );
            if (i < 0)
                break;
            String sKey = template.substring(start+3, i);
            sb.append( substs.getProperty(sKey, "") );
            start = i+1;
        }
        sb.append( template.substring(start) );
        return sb.toString();
    }




    public byte[] SNAP(String RIC, String format) throws Exception
    {
        checkToken();
        Properties prm = new Properties();
        prm.setProperty( "MSGID", getNewMsgId() );
        prm.setProperty( "APPID", appId );
        prm.setProperty( "TOKEN", token );
        prm.setProperty( "RIC", RIC );
        String request = substitute( snapRequestTemplate, prm );
        return getResponseBodyBytes( sendRequest( snapURL, request ), true );
    }




    public byte[] SEARCH_BY_NAME(String name, String format) throws Exception
    {
        checkToken();
        Properties prm = new Properties();
        prm.setProperty( "MSGID", getNewMsgId() );
        prm.setProperty( "APPID", appId );
        prm.setProperty( "TOKEN", token );
        prm.setProperty( "NAME", name );
        String request = substitute( searchByNameRequestTemplate, prm );
        return getResponseBodyBytes( sendRequest( searchByNameURL, request ), true );
    }



    public byte[] SEARCH_BY_SYMBOL(String symbol, String format) throws Exception
    {
        checkToken();
        Properties prm = new Properties();
        prm.setProperty( "MSGID", getNewMsgId() );
        prm.setProperty( "APPID", appId );
        prm.setProperty( "TOKEN", token );
        prm.setProperty( "SYMB", symbol );
        String request = substitute( searchBySymbRequestTemplate, prm );
        return getResponseBodyBytes( sendRequest( searchBySymbURL, request ), true );
    }



    public byte[] INTRADAY_CHART(String RIC) throws Exception
    {
        return getChart( RIC, getChart1D_Template );
    }



    public byte[] INTERDAY_CHART(String RIC, String time) throws Exception
    {
        String getChartTemplate = null;

        if ( time.equalsIgnoreCase( "1W" ) )
            getChartTemplate = getChart1W_Template ;
        else if ( time.equalsIgnoreCase( "3M" ) )
            getChartTemplate = getChart3M_Template ;
        else if ( time.equalsIgnoreCase( "6M" ) )
            getChartTemplate = getChart6M_Template ;
        else if ( time.equalsIgnoreCase( "1Y" ) )
            getChartTemplate = getChart1Y_Template ;
        else
            throw new Exception( "Illegal time period for chart: '" + time + "'" );
        return getChart( RIC, getChartTemplate );
    }


// Params:
// 1) RIC for SNAP ("TEVA.P")
// 2) NAME for SEARCH_BY_NAME ("google")
// 3) SYMB for SEARCH_BY_SYMBOL ("goog")
// 4) RIC for  INTRADAY_CHART ("TEVA.P")
// 5) RIC for  INTERDAY_CHART_1W ("TEVA.P")
// 6) RIC for  INTERDAY_CHART_3M ("TEVA.P")
// 7) RIC for  INTERDAY_CHART_6M ("TEVA.P")
// 8) RIC for  INTERDAY_CHART_1Y ("TEVA.P")
 

    public static void main(String[] args)
    {
        if ( initEx != null )
        {
            initEx.printStackTrace();
            System.exit(0);
        }
        RI2API r = null;
        byte[] b = null;
        try
        {
            r = new RI2API();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }
        String s = "TEVA.P";
        if ( args.length > 0 )
           s = args[0];
        try
        {
            b = r.SNAP(s, "");
            writeBytes2file( "SNAP_" + s + ".xml", b );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        s = "google";
        if ( args.length > 1 )
           s = args[1];
        try
        {
            b = r.SEARCH_BY_NAME(s, "");
            writeBytes2file( "SEARCH_BY_NAME_" + s + ".xml", b );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        s = "goog";
        if ( args.length > 2 )
           s = args[2];
        try
        {
            b = r.SEARCH_BY_SYMBOL(s, "");
            writeBytes2file( "SEARCH_BY_SYMBOL_" + s + ".xml", b );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        s = "TEVA.P";
        if ( args.length > 3 )
           s = args[3];
        try
        {
            b = r.INTRADAY_CHART(s);
            s = "INTRADAY_CHART_" + s + ".PNG";
            writeBytes2file( s, b );
            showChart( s );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        s = "TEVA.P";
        if ( args.length > 4 )
           s = args[4];
        try
        {
            b = r.INTERDAY_CHART(s, "1W");
            s = "INTERDAY_CHART_1W_" + s + ".PNG";
            writeBytes2file( s, b );
            showChart( s );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        s = "TEVA.P";
        if ( args.length > 5 )
           s = args[5];
        try
        {
            b = r.INTERDAY_CHART(s, "3M");
            s = "INTERDAY_CHART_3M_" + s + ".PNG";
            writeBytes2file( s, b );
            showChart( s );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        s = "TEVA.P";
        if ( args.length > 6 )
           s = args[6];
        try
        {
            b = r.INTERDAY_CHART(s, "6M");
            s = "INTERDAY_CHART_6M_" + s + ".PNG";
            writeBytes2file( s, b );
            showChart( s );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        s = "TEVA.P";
        if ( args.length > 7 )
           s = args[7];
        try
        {
            b = r.INTERDAY_CHART(s, "1Y");
            s = "INTERDAY_CHART_1Y_" + s + ".PNG";
            writeBytes2file( s, b );
            showChart( s );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private static void writeBytes2file( String fileName, byte[] b ) throws Exception
    {
        FileOutputStream f = new FileOutputStream( fileName );
        f.write( b );
        f.close();
    }



    private static void showChart( String s ) throws Exception
    {
        String[] prm = new String[2];
        prm[0] = "C:\\Program Files\\Internet Explorer\\iexplore.exe";
        prm[1] = (new File(s)).getAbsolutePath();
        Runtime.getRuntime().exec( prm );
    }
}
