package com.jexbox.connector.play;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;

import play.api.PlayException;
import play.mvc.Http.RequestHeader;
import play.mvc.Http.Session;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jexbox.connector.JexboxConnectorImpl;
import com.jexbox.connector.TransportException;

public class JexboxConnectorPlayImpl extends JexboxConnectorImpl implements JexboxConnectorPlay{
    private static Logger _logger = Logger.getLogger(JexboxConnectorPlayImpl.class.getName());
    
	public JexboxConnectorPlayImpl(Properties props) {
		super(props);
	}
	
	public void send(Throwable e, RequestHeader request, Session session){
		sendWithMeta(e, request, session, null);
	}
	
	public void sendWithMeta(Throwable ex, RequestHeader request, Session session, Map<String, Map<String, String>> metaD){
		try {
			if(metaD == null){
				metaD = new HashMap<String, Map<String, String>>();
			}
			
			Throwable e = removeInfrastructure(ex);

			addPageTrace(e, metaD);
			
			JsonObject json = json(e, metaD);
			addRequestMetaData(request, json);
			
			addSessionMetaData(session, json);
			_notifier.send(json);
		} catch (UnsupportedEncodingException e1) {
			_logger.log(Level.SEVERE, "Could not able to send error to Jexbox", e1);
		} catch (TransportException e1) {
			_logger.log(Level.SEVERE, "Could not able to send error to Jexbox", e1);
		}
	}
	
	protected void addPageTrace(Throwable e, Map<String, Map<String, String>> metaD){
		if(e instanceof PlayException.ExceptionSource){
			PlayException.ExceptionSource error = (PlayException.ExceptionSource) e;
			
			StringBuffer buff = new StringBuffer();
			buff.append("<div class=\"play-error-page\">");
			buff.append("<h2> In "+error.sourceName()+":"+error.line()+"</h2>");
			buff.append("<div class=\"source-code\">");

			String[] split = error.input().split("\n");
			for (int i = 1; i <= split.length; i++) {
				String line = split[i-1];
				if(i == error.line()){
					buff.append("<pre class=\"error\" data-file=\""+error.sourceName()+"\" data-line=\""+i+"\">");
				}else{
					buff.append("<pre data-file=\""+error.sourceName()+"\" data-line=\""+i+"\">");
				}
				buff.append("<span class=\"line\">"+i+"</span>");
				buff.append("<span class=\"code\">"+line+"</span>");
				buff.append("</pre>");
			}
				
			buff.append("</div>");			
			buff.append("</div>");			
					
			Map<String, String> meta = new HashMap<String, String>();
			metaD.put("Page Trace", meta);
			String pageTrace64 = Base64.encodeBase64String( buff.toString().getBytes() );
			meta.put("data", pageTrace64);
		}
	}
	
	protected void addSessionMetaData(Session session, JsonObject json){
		if(session != null){
			JsonObject meta = json.getAsJsonObject("meta");
			if(meta == null){
				meta = new JsonObject();
				json.add("meta", meta);
			}
			
			JsonObject sessionD = new JsonObject();
			meta.add("Session", sessionD);
			
			Set<String> names = session.keySet();
			for (String name : names) {
				Object attr = session.get(name);
				sessionD.add(name, new JsonPrimitive(String.valueOf(attr)));
			}
		}
	}
	
	protected void addRequestMetaData(RequestHeader reqHTTP, JsonObject json){
			JsonObject meta = json.getAsJsonObject("meta");
			if(meta == null){
				meta = new JsonObject();
				json.add("meta", meta);
			}
			
			json.add("uri",  new JsonPrimitive(reqHTTP.uri()));
			
			JsonObject reqPara = new JsonObject();
			meta.add("Query Strings", reqPara);
			Map<java.lang.String,java.lang.String[]> queryString = reqHTTP.queryString();
			for (String name : queryString.keySet()) {
				String attr = reqHTTP.getQueryString(name);
				if(attr != null){
					reqPara.add(name, new JsonPrimitive(String.valueOf(attr)));
				}else{
					reqPara.add(name, new JsonPrimitive("null"));
				}
			}
			
			JsonObject reqHead = new JsonObject();
			meta.add("Request Headers", reqHead);
			Map<java.lang.String,java.lang.String[]> headers = reqHTTP.headers();
			for (String name : headers.keySet()) {
				String attr = reqHTTP.getHeader(name);
				if(attr != null){
					reqHead.add(name, new JsonPrimitive(String.valueOf(attr)));
				}else{
					reqHead.add(name, new JsonPrimitive("null"));
				}
			}
			
			
			JsonObject req = new JsonObject();
			meta.add("Request", req);
			
			req.add("Host", new JsonPrimitive(String.valueOf(reqHTTP.host())));
			req.add("Method", new JsonPrimitive(String.valueOf(reqHTTP.method())));
			req.add("Path", new JsonPrimitive(String.valueOf(reqHTTP.path())));
			req.add("Remote Addr", new JsonPrimitive(String.valueOf(reqHTTP.remoteAddress())));
			req.add("Secure", new JsonPrimitive(String.valueOf(reqHTTP.secure())));
			req.add("URI", new JsonPrimitive(String.valueOf(reqHTTP.uri())));
			req.add("Version", new JsonPrimitive(String.valueOf(reqHTTP.version())));
	}
	
    protected Throwable removeInfrastructure(Throwable e){
    	if(e.getCause() != null){
    		if(e.getClass().getName().startsWith("play.api.Application$$anon$")){
        			return removeInfrastructure(e.getCause());
    		}else{
    			return e;
    		}
    	}else{
    		return e;
    	}
    }
}
