
package client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg" type="{http://data.a05s303e}A05C303DSADOT"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "arg"
})
@XmlRootElement(name = "run")
public class Run {

    @XmlElement(required = true, nillable = true)
    protected A05C303DSADOT arg;

    /**
     * Gets the value of the arg property.
     * 
     * @return
     *     possible object is
     *     {@link A05C303DSADOT }
     *     
     */
    public A05C303DSADOT getArg() {
        return arg;
    }

    /**
     * Sets the value of the arg property.
     * 
     * @param value
     *     allowed object is
     *     {@link A05C303DSADOT }
     *     
     */
    public void setArg(A05C303DSADOT value) {
        this.arg = value;
    }

}
