
package client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "A05S303EImplService", targetNamespace = "http://a05s303e", wsdlLocation = "file:/C:/temp/bibi/A05S303EImpl.wsdl")
public class A05S303EImplService
    extends Service
{

    private final static URL A05S303EIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException A05S303EIMPLSERVICE_EXCEPTION;
    private final static QName A05S303EIMPLSERVICE_QNAME = new QName("http://a05s303e", "A05S303EImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/temp/bibi/A05S303EImpl.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        A05S303EIMPLSERVICE_WSDL_LOCATION = url;
        A05S303EIMPLSERVICE_EXCEPTION = e;
    }

    public A05S303EImplService() {
        super(__getWsdlLocation(), A05S303EIMPLSERVICE_QNAME);
    }

    public A05S303EImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), A05S303EIMPLSERVICE_QNAME, features);
    }

    public A05S303EImplService(URL wsdlLocation) {
        super(wsdlLocation, A05S303EIMPLSERVICE_QNAME);
    }

    public A05S303EImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, A05S303EIMPLSERVICE_QNAME, features);
    }

    public A05S303EImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public A05S303EImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns A05S303EImpl
     */
    @WebEndpoint(name = "A05S303EImpl")
    public A05S303EImpl getA05S303EImpl() {
        return super.getPort(new QName("http://a05s303e", "A05S303EImpl"), A05S303EImpl.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns A05S303EImpl
     */
    @WebEndpoint(name = "A05S303EImpl")
    public A05S303EImpl getA05S303EImpl(WebServiceFeature... features) {
        return super.getPort(new QName("http://a05s303e", "A05S303EImpl"), A05S303EImpl.class, features);
    }

    private static URL __getWsdlLocation() {
        if (A05S303EIMPLSERVICE_EXCEPTION!= null) {
            throw A05S303EIMPLSERVICE_EXCEPTION;
        }
        return A05S303EIMPLSERVICE_WSDL_LOCATION;
    }

}
