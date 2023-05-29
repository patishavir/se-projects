package mataf.infra.mq.bridge.notes;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import lotus.domino.AgentBase;

public class JavaAgent extends AgentBase {
	public void NotesMain() {
		String response = put();
		System.out.println("Response length: " + String.valueOf(response.length()) + "\nResponse: " + response);
		System.out.println(formatXml(response));
	}

	private static String put() {
		String response = null;
		HttpURLConnection httpUrlConnectionPut = null;
		try {
			MqBridge4HttpProperties mqBridge4HttpProperties = new MqBridge4HttpProperties();
			String message = mqBridge4HttpProperties.getMessage();
			URL urlPUT = new URL(mqBridge4HttpProperties.getUrlPUT());
			System.out.println("message length: " + String.valueOf(message.length()) + "\nmessage: " + message);
			System.out.println("Try to write message ... ");
			httpUrlConnectionPut = (HttpURLConnection) urlPUT.openConnection();
			httpUrlConnectionPut.setDoOutput(true);
			httpUrlConnectionPut.setRequestMethod("POST");
			httpUrlConnectionPut.setConnectTimeout(Integer.parseInt(mqBridge4HttpProperties.getConnectTimeOutString()));
			httpUrlConnectionPut.setReadTimeout(Integer.parseInt(mqBridge4HttpProperties.getReadTimeOutString()));
			httpUrlConnectionPut.setRequestProperty("x-msg-require-headers", "ALL");
			httpUrlConnectionPut.setRequestProperty("x-msg-expiry", mqBridge4HttpProperties.getExpiry());
			httpUrlConnectionPut.setRequestProperty("x-msg-replyTo", mqBridge4HttpProperties.getReplyTo());
			httpUrlConnectionPut.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
			OutputStream os = httpUrlConnectionPut.getOutputStream();
			os.write(message.getBytes("utf-8"));
			Map responseHeader = httpUrlConnectionPut.getHeaderFields();
			List msgID = (List) responseHeader.get("x-msg-msgId");
			httpUrlConnectionPut.setDoInput(true);
			int status = httpUrlConnectionPut.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK && msgID != null) {
				response = get(msgID);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (httpUrlConnectionPut != null) {
				httpUrlConnectionPut.disconnect();
				System.out.println("httpUrlConnectionPut has been disconnected");
			}
		}
		return response;
	}

	private static String get(final List msgID) {
		String response = null;
		HttpURLConnection httpUrlConnectionGet = null;
		try {
			MqBridge4HttpProperties mqBridge4HttpProperties = new MqBridge4HttpProperties();
			// Read Response
			if (msgID != null) {
				System.out.println("PUT message id is: " + msgID.get(0));
				System.out.println("Try to read response ...");
				URL urlGET = new URL(mqBridge4HttpProperties.getUrlGET());
				httpUrlConnectionGet = (HttpURLConnection) urlGET.openConnection();
				httpUrlConnectionGet = (HttpURLConnection) urlGET.openConnection();
				httpUrlConnectionGet.setDoInput(true);
				httpUrlConnectionGet.setRequestMethod("DELETE");
				httpUrlConnectionGet.setRequestProperty("x-msg-msgId", (String) msgID.get(0));
				httpUrlConnectionGet.setRequestProperty("x-msg-wait", mqBridge4HttpProperties.getWait());
				byte[] readBuffer = new byte[4096];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				InputStream is = httpUrlConnectionGet.getInputStream();
				int readCount;
				while ((readCount = is.read(readBuffer)) > 0) {
					baos.write(readBuffer, 0, readCount);
					System.out.println("read " + String.valueOf(readCount) + " bytes");
				}
				response = new String(baos.toByteArray(), "utf-8");
				httpUrlConnectionGet.disconnect();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (httpUrlConnectionGet != null) {
				httpUrlConnectionGet.disconnect();
				System.out.println("httpUrlConnectionGet has been disconnected");
			}
		}
		return response;
	}

	private static String formatXml(final String xmlString) {
		String xmlString1 = null;
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			Document document = getDocument(xmlString);
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			xmlString1 = result.getWriter().toString();
			System.out.println(xmlString1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return xmlString1;
	}

	public static Document getDocument(final String xml) throws Exception {
		DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
		DocumentBuilder bldr = fctr.newDocumentBuilder();
		InputSource insrc = new InputSource(new StringReader(xml));
		return bldr.parse(insrc);
	}

}
