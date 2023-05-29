package a05s303e;

public class A05S303EImplProxy implements a05s303e.A05S303EImpl {
  private String _endpoint = null;
  private a05s303e.A05S303EImpl a05S303EImpl = null;
  
  public A05S303EImplProxy() {
    _initA05S303EImplProxy();
  }
  
  public A05S303EImplProxy(String endpoint) {
    _endpoint = endpoint;
    _initA05S303EImplProxy();
  }
  
  private void _initA05S303EImplProxy() {
    try {
      a05S303EImpl = (new a05s303e.A05S303EImplServiceLocator()).getA05S303EImpl();
      if (a05S303EImpl != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)a05S303EImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)a05S303EImpl)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (a05S303EImpl != null)
      ((javax.xml.rpc.Stub)a05S303EImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public a05s303e.A05S303EImpl getA05S303EImpl() {
    if (a05S303EImpl == null)
      _initA05S303EImplProxy();
    return a05S303EImpl;
  }
  
  public a05s303e.data.A05C303DSADOT run(a05s303e.data.A05C303DSADOT arg) throws java.rmi.RemoteException{
    if (a05S303EImpl == null)
      _initA05S303EImplProxy();
    return a05S303EImpl.run(arg);
  }
  
  
}