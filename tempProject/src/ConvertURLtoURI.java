// -----------------------------------------------------------------------------
// ConvertURLtoURI.java
// -----------------------------------------------------------------------------

/*
 * =============================================================================
 * Copyright (c) 1998-2005 Jeffrey M. Hunter. All rights reserved.
 * 
 * All source code and material located at the Internet address of
 * http://www.idevelopment.info is the copyright of Jeffrey M. Hunter, 2005 and
 * is protected under copyright laws of the United States. This source code may
 * not be hosted on any other site without my express, prior, written
 * permission. Application to host any of the material elsewhere can be made by
 * contacting me at jhunter@idevelopment.info.
 *
 * I have made every effort and taken great care in making sure that the source
 * code and other content included on my web site is technically accurate, but I
 * disclaim any and all responsibility for any loss, damage or destruction of
 * data or any other property which may arise from relying on it. I will in no
 * case be liable for any monetary damages arising from such loss, damage or
 * destruction.
 * 
 * As with any code, ensure to test this code in a development environment 
 * before attempting to run it in production.
 * =============================================================================
 */

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * -----------------------------------------------------------------------------
 * This class provides an example of how to convert a Uniform Resource Locator
 * (URL) to a Uniform Resource Identifier (URI).
 * 
 * @version 1.0
 * @author Jeffrey M. Hunter (jhunter@idevelopment.info)
 * @author http://www.idevelopment.info
 *         ------------------------------------------
 *         -----------------------------------
 */

public class ConvertURLtoURI {

	public static void main(String[] args) {

		URL url = null;
		URI uri = null;
		String urlString = "http://www.idevelopment.info/data/Oracle/DBA_tips/Linux/LINUX_5.shtml";

		// Create a URL object
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// Create a URI object from the String object returned by the URL object
		try {
			uri = new URI(url.toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// Print the original URL object and the newly converted URI object
		System.out.println("Original URL  : " + url);
		System.out.println("Converted URI : " + uri);

	}

}
