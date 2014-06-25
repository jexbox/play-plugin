package com.jexbox.connector.play;

import java.util.Map;

import play.mvc.Http.RequestHeader;
import play.mvc.Http.Session;

import com.jexbox.connector.JexboxConnector;

public interface JexboxConnectorPlay extends JexboxConnector{
	public void send(Throwable e, RequestHeader request, Session session);
	public void sendWithMeta(Throwable e, RequestHeader request, Session session, Map<String, Map<String, String>> metaD);
}
