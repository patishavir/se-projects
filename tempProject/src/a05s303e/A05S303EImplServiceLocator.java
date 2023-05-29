/**
 * A05S303EImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package a05s303e;

public class A05S303EImplServiceLocator extends org.apache.axis.client.Service implements a05s303e.A05S303EImplService {

    public A05S303EImplServiceLocator() {
    }


    public A05S303EImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public A05S303EImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for A05S303EImpl
    private java.lang.String A05S303EImpl_address = "http://10.18.188.115:9083/CtgServicesCICST/services/A05S303EImpl";

    public java.lang.String getA05S303EImplAddress() {
        return A05S303EImpl_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String A05S303EImplWSDDServiceName = "A05S303EImpl";

    public java.lang.String getA05S303EImplWSDDServiceName() {
        return A05S303EImplWSDDServiceName;
    }

    public void setA05S303EImplWSDDServiceName(java.lang.String name) {
        A05S303EImplWSDDServiceName = name;
    }

    public a05s303e.A05S303EImpl getA05S303EImpl() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(A05S303EImpl_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getA05S303EImpl(endpoint);
    }

    public a05s303e.A05S303EImpl getA05S303EImpl(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            a05s303e.A05S303EImplSoapBindingStub _stub = new a05s303e.A05S303EImplSoapBindingStub(portAddress, this);
            _stub.setPortName(getA05S303EImplWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setA05S303EImplEndpointAddress(java.lang.String address) {
        A05S303EImpl_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (a05s303e.A05S303EImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                a05s303e.A05S303EImplSoapBindingStub _stub = new a05s303e.A05S303EImplSoapBindingStub(new java.net.URL(A05S303EImpl_address), this);
                _stub.setPortName(getA05S303EImplWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("A05S303EImpl".equals(inputPortName)) {
            return getA05S303EImpl();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://a05s303e", "A05S303EImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://a05s303e", "A05S303EImpl"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("A05S303EImpl".equals(portName)) {
            setA05S303EImplEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
