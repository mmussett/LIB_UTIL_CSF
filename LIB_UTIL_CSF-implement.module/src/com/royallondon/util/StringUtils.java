package com.royallondon.util;

import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StringUtils extends URLDecoder {

	static final Logger LOG = LoggerFactory.getLogger(StringUtils.class);
	private static final String LESS_SYMBOL = "<";
	private static final String GREATER_SYMBOL = ">";
	private static final String DELIMITED_LESS_SYMBOL = "&lt;";
	private static final String DELIMITED_GREATER_SYMBOL = "&gt;";
	
	
	public StringUtils() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//StringUtils x = new StringUtils();
		//String x1 = x.compress("richard was here");
		//String y1 = x.uncompress(x1);
		//System.out.println(x.getFilterString("A,B,C,D(1,2,3),E,F(7,8,9),G,H"));
		//System.out.println(new StringUtils().getNameOfCaller("tester.module.Process2/SubProcess->util.module.SubProcess/SubProcess1->util.module.SubProcess1", false));
		//System.out.println(new StringUtils().getNameOfCaller("tester.module.Process2/SubProcess->tester.module.SubProcess/SubProcess1->tester.module.SubProcess1", false));
		//System.out.println(new StringUtils().getNameOfCaller("tester.module.Process2/SubProcess->tester.module.SubProcess/SubProcess1->tester.module.SubProcess1", true));
		//System.out.println(new StringUtils().getNameOfCaller("tester.module.Process2/SubProcess->util.module.SubProcess/SubProcess1->tester.module.SubProcess1", true));
		//System.out.println(new StringUtils().getNameOfCaller("tester.module.Process2/SubProcess->util.module.SubProcess/SubProcess1->util.module.SubProcess1", true));
	}

	public String getNameOfCaller(String stack, boolean ignoreInternal) {
		String name = getStackElement(stack, ignoreInternal);

		if (LOG.isDebugEnabled())
			LOG.debug("Using name element '" + name + "' from stack '" + stack + "'");

		if (name.length() == 0)
			return "UKNOWN";
		else
			return name;
	}

	private String getStackElement(String stack, boolean ignoreInternal) {
		String[] elements = stack.split("->");

		for (int i = elements.length - 2; i >= 0; --i) {
			String item = elements[i];
			int pos = item.indexOf("/");
			if (pos >= 0)
				item = item.substring(0, pos);

			if (ignoreInternal) {
				if (!item.startsWith("resource.") && !item.startsWith("util."))
					return item;
			}
			else
				return item;
		}
		
		return stack;
	}

	public String getFilterString(String filter) {
		StringBuilder reply = new StringBuilder(filter);
		
		boolean inSub = false;
		for (int pos = 0; pos < filter.length(); ++pos) {
			char testChar = reply.charAt(pos);
			if (testChar == '(') {
				inSub = true;
			}
			else if (testChar == ')') {
				inSub = false;
			}
			else if (inSub && (testChar == ',')) {
				reply.setCharAt(pos, '-');
			}
		}
		return reply.toString();
	}
	
	public String replaceRegEx(String str, String regex, String replacement) {
		return str.replaceAll(regex, replacement);
	}

	public String[] replaceRegExes(String str[], String regex[], String replacement[]) {
		assert str.length == regex.length;
		assert str.length == replacement.length;
		String response[] = new String[str.length];
		for (int i = 0; i < str.length; ++i) {
			response[i] = replaceRegEx(str[i], regex[i], replacement[i]);			
		}
		return response;
	}
	
	public String replaceMatchingExp(String str, String open,
			String close, String lastPosTag) {
		int firstPos = 0;
		int lastPos = 0;
		String replacementValue;
		do {
			firstPos = str.toLowerCase().indexOf(open, lastPos);
			if (firstPos != -1) {
				lastPos = str.indexOf(close, firstPos + open.length());
				replacementValue= convertToAsterisk(str.substring(firstPos+open.length(), lastPos));
				
				str = str.substring(0, firstPos + open.length()) + replacementValue
						+ str.substring(lastPos);
				lastPos = str.indexOf(lastPosTag, lastPos);
			}
		} while (firstPos != -1);

		return str;
	}
	
	public String convertToAsterisk(String inputStr) {
        if (inputStr == null || (inputStr != null && inputStr.length() == 0 )) {
            return "";
        }
        
        StringBuffer sb = new StringBuffer();
        for (int ctr = 0; ctr < inputStr.length(); ctr++) {
            sb.append("*");
        }
        return sb.toString();
    }
	
	
	public String redactLogPayload(String logPayload, String replacements, boolean validateMessageTypes, String messageTypes) {

        boolean containsMessageType = false;
        
        if (validateMessageTypes == true) {
            String[] msgTypes = messageTypes.split(",");
            int size = msgTypes.length;
            for (int i = 0; i < size && containsMessageType == false; i++) {
            	
                if (logPayload.contains(msgTypes[i]) == true) {
                      containsMessageType = true;
                }
            }
        }

        if (validateMessageTypes == true && containsMessageType == false) {
          return logPayload;
          }
          else {
        	  
              String[] redactKeys = replacements.split(",");
              
              for (String val: redactKeys) {
                  String redactKeyOpenTag = val.toLowerCase() + GREATER_SYMBOL;
                  logPayload = replaceMatchingExp(logPayload,redactKeyOpenTag,
                		  LESS_SYMBOL,GREATER_SYMBOL);
                  
                  if(logPayload.contains(DELIMITED_LESS_SYMBOL)
                		  || logPayload.contains(DELIMITED_GREATER_SYMBOL)) {
                	  redactKeyOpenTag = val.toLowerCase() + DELIMITED_GREATER_SYMBOL;
                	  logPayload = replaceMatchingExp(logPayload,redactKeyOpenTag,
                			  DELIMITED_LESS_SYMBOL, DELIMITED_GREATER_SYMBOL);                	  
                  }
              }
              return logPayload;
          }
	}

	public String compress(String str) throws IOException {
		ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
		GZIPOutputStream zos = new GZIPOutputStream(rstBao);
		zos.write(str.getBytes());
		zos.close();
		
		byte[] bytes = rstBao.toByteArray();
		rstBao.close();

		return Base64.getEncoder().encodeToString(bytes);
	}

	public String uncompress(String str) throws IOException {
		byte[] bytes = Base64.getDecoder().decode(str);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		GZIPInputStream gzin = new GZIPInputStream(bais);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    byte[] buffer = new byte[8192];
        int len;
        while((len = gzin.read(buffer)) > 0)
        	baos.write(buffer, 0, len);
        baos.close();
        gzin.close();
        bais.close();

        return new String(baos.toByteArray(), "UTF-8");
    }

	public String decryptPassword(String pwd) throws AXSecurityException {
		return new String(ObfuscationEngine.decrypt(pwd));
    }
	
	public final String[] decodeElements(String format, String[] encodedElements) throws UnsupportedEncodingException {

		int length=encodedElements.length;
		String[] decodedElements = new String[length];
		for (int i = 0; i < length; i++) {
			decodedElements[i] = decode(encodedElements[i], format);			
		}
		return decodedElements;
				
	}
}
