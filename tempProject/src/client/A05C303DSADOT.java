
package client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for A05C303DSADOT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="A05C303DSADOT">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recordName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recordShortDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bytes" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="a05c303d__kod__pnia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__kod__nose" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__kod__zero" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__linkage__cics" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fill__0" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__k__bank" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="a05c303d__k__snif" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="a05c303d__k__mch" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="a05c303d__r__sch__x" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="a05c303d__r__mch__x" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="a05c303d__k__mat" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="fill__1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__p__matzav__bank" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__p__matzav__snif" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__p__matzav__mch" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__p__check" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__p__ifun__sch" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="a05c303d__p__sifrat__bikoret" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="a05c303d__p__error" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "A05C303DSADOT", namespace = "http://data.a05s303e", propOrder = {
    "recordName",
    "recordShortDescription",
    "bytes",
    "a05C303DKodPnia",
    "a05C303DKodNose",
    "a05C303DKodZero",
    "a05C303DLinkageCics",
    "fill0",
    "a05C303DKBank",
    "a05C303DKSnif",
    "a05C303DKMch",
    "a05C303DRSchX",
    "a05C303DRMchX",
    "a05C303DKMat",
    "fill1",
    "a05C303DPMatzavBank",
    "a05C303DPMatzavSnif",
    "a05C303DPMatzavMch",
    "a05C303DPCheck",
    "a05C303DPIfunSch",
    "a05C303DPSifratBikoret",
    "a05C303DPError"
})
public class A05C303DSADOT {

    @XmlElement(required = true, nillable = true)
    protected String recordName;
    @XmlElement(required = true, nillable = true)
    protected String recordShortDescription;
    @XmlElement(required = true)
    protected byte[] bytes;
    @XmlElement(name = "a05c303d__kod__pnia", required = true, nillable = true)
    protected String a05C303DKodPnia;
    @XmlElement(name = "a05c303d__kod__nose", required = true, nillable = true)
    protected String a05C303DKodNose;
    @XmlElement(name = "a05c303d__kod__zero", required = true, nillable = true)
    protected String a05C303DKodZero;
    @XmlElement(name = "a05c303d__linkage__cics", required = true, nillable = true)
    protected String a05C303DLinkageCics;
    @XmlElement(name = "fill__0", required = true, nillable = true)
    protected String fill0;
    @XmlElement(name = "a05c303d__k__bank")
    protected short a05C303DKBank;
    @XmlElement(name = "a05c303d__k__snif")
    protected short a05C303DKSnif;
    @XmlElement(name = "a05c303d__k__mch")
    protected int a05C303DKMch;
    @XmlElement(name = "a05c303d__r__sch__x")
    protected short a05C303DRSchX;
    @XmlElement(name = "a05c303d__r__mch__x")
    protected int a05C303DRMchX;
    @XmlElement(name = "a05c303d__k__mat")
    protected short a05C303DKMat;
    @XmlElement(name = "fill__1", required = true, nillable = true)
    protected String fill1;
    @XmlElement(name = "a05c303d__p__matzav__bank", required = true, nillable = true)
    protected String a05C303DPMatzavBank;
    @XmlElement(name = "a05c303d__p__matzav__snif", required = true, nillable = true)
    protected String a05C303DPMatzavSnif;
    @XmlElement(name = "a05c303d__p__matzav__mch", required = true, nillable = true)
    protected String a05C303DPMatzavMch;
    @XmlElement(name = "a05c303d__p__check", required = true, nillable = true)
    protected String a05C303DPCheck;
    @XmlElement(name = "a05c303d__p__ifun__sch", required = true, nillable = true)
    protected String a05C303DPIfunSch;
    @XmlElement(name = "a05c303d__p__sifrat__bikoret")
    protected short a05C303DPSifratBikoret;
    @XmlElement(name = "a05c303d__p__error", required = true, nillable = true)
    protected String a05C303DPError;

    /**
     * Gets the value of the recordName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordName() {
        return recordName;
    }

    /**
     * Sets the value of the recordName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordName(String value) {
        this.recordName = value;
    }

    /**
     * Gets the value of the recordShortDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordShortDescription() {
        return recordShortDescription;
    }

    /**
     * Sets the value of the recordShortDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordShortDescription(String value) {
        this.recordShortDescription = value;
    }

    /**
     * Gets the value of the bytes property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Sets the value of the bytes property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBytes(byte[] value) {
        this.bytes = value;
    }

    /**
     * Gets the value of the a05C303DKodPnia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DKodPnia() {
        return a05C303DKodPnia;
    }

    /**
     * Sets the value of the a05C303DKodPnia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DKodPnia(String value) {
        this.a05C303DKodPnia = value;
    }

    /**
     * Gets the value of the a05C303DKodNose property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DKodNose() {
        return a05C303DKodNose;
    }

    /**
     * Sets the value of the a05C303DKodNose property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DKodNose(String value) {
        this.a05C303DKodNose = value;
    }

    /**
     * Gets the value of the a05C303DKodZero property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DKodZero() {
        return a05C303DKodZero;
    }

    /**
     * Sets the value of the a05C303DKodZero property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DKodZero(String value) {
        this.a05C303DKodZero = value;
    }

    /**
     * Gets the value of the a05C303DLinkageCics property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DLinkageCics() {
        return a05C303DLinkageCics;
    }

    /**
     * Sets the value of the a05C303DLinkageCics property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DLinkageCics(String value) {
        this.a05C303DLinkageCics = value;
    }

    /**
     * Gets the value of the fill0 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFill0() {
        return fill0;
    }

    /**
     * Sets the value of the fill0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFill0(String value) {
        this.fill0 = value;
    }

    /**
     * Gets the value of the a05C303DKBank property.
     * 
     */
    public short getA05C303DKBank() {
        return a05C303DKBank;
    }

    /**
     * Sets the value of the a05C303DKBank property.
     * 
     */
    public void setA05C303DKBank(short value) {
        this.a05C303DKBank = value;
    }

    /**
     * Gets the value of the a05C303DKSnif property.
     * 
     */
    public short getA05C303DKSnif() {
        return a05C303DKSnif;
    }

    /**
     * Sets the value of the a05C303DKSnif property.
     * 
     */
    public void setA05C303DKSnif(short value) {
        this.a05C303DKSnif = value;
    }

    /**
     * Gets the value of the a05C303DKMch property.
     * 
     */
    public int getA05C303DKMch() {
        return a05C303DKMch;
    }

    /**
     * Sets the value of the a05C303DKMch property.
     * 
     */
    public void setA05C303DKMch(int value) {
        this.a05C303DKMch = value;
    }

    /**
     * Gets the value of the a05C303DRSchX property.
     * 
     */
    public short getA05C303DRSchX() {
        return a05C303DRSchX;
    }

    /**
     * Sets the value of the a05C303DRSchX property.
     * 
     */
    public void setA05C303DRSchX(short value) {
        this.a05C303DRSchX = value;
    }

    /**
     * Gets the value of the a05C303DRMchX property.
     * 
     */
    public int getA05C303DRMchX() {
        return a05C303DRMchX;
    }

    /**
     * Sets the value of the a05C303DRMchX property.
     * 
     */
    public void setA05C303DRMchX(int value) {
        this.a05C303DRMchX = value;
    }

    /**
     * Gets the value of the a05C303DKMat property.
     * 
     */
    public short getA05C303DKMat() {
        return a05C303DKMat;
    }

    /**
     * Sets the value of the a05C303DKMat property.
     * 
     */
    public void setA05C303DKMat(short value) {
        this.a05C303DKMat = value;
    }

    /**
     * Gets the value of the fill1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFill1() {
        return fill1;
    }

    /**
     * Sets the value of the fill1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFill1(String value) {
        this.fill1 = value;
    }

    /**
     * Gets the value of the a05C303DPMatzavBank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DPMatzavBank() {
        return a05C303DPMatzavBank;
    }

    /**
     * Sets the value of the a05C303DPMatzavBank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DPMatzavBank(String value) {
        this.a05C303DPMatzavBank = value;
    }

    /**
     * Gets the value of the a05C303DPMatzavSnif property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DPMatzavSnif() {
        return a05C303DPMatzavSnif;
    }

    /**
     * Sets the value of the a05C303DPMatzavSnif property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DPMatzavSnif(String value) {
        this.a05C303DPMatzavSnif = value;
    }

    /**
     * Gets the value of the a05C303DPMatzavMch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DPMatzavMch() {
        return a05C303DPMatzavMch;
    }

    /**
     * Sets the value of the a05C303DPMatzavMch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DPMatzavMch(String value) {
        this.a05C303DPMatzavMch = value;
    }

    /**
     * Gets the value of the a05C303DPCheck property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DPCheck() {
        return a05C303DPCheck;
    }

    /**
     * Sets the value of the a05C303DPCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DPCheck(String value) {
        this.a05C303DPCheck = value;
    }

    /**
     * Gets the value of the a05C303DPIfunSch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DPIfunSch() {
        return a05C303DPIfunSch;
    }

    /**
     * Sets the value of the a05C303DPIfunSch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DPIfunSch(String value) {
        this.a05C303DPIfunSch = value;
    }

    /**
     * Gets the value of the a05C303DPSifratBikoret property.
     * 
     */
    public short getA05C303DPSifratBikoret() {
        return a05C303DPSifratBikoret;
    }

    /**
     * Sets the value of the a05C303DPSifratBikoret property.
     * 
     */
    public void setA05C303DPSifratBikoret(short value) {
        this.a05C303DPSifratBikoret = value;
    }

    /**
     * Gets the value of the a05C303DPError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getA05C303DPError() {
        return a05C303DPError;
    }

    /**
     * Sets the value of the a05C303DPError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setA05C303DPError(String value) {
        this.a05C303DPError = value;
    }

}
