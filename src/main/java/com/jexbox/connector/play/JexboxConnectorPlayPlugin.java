package com.jexbox.connector.play;

import java.util.Properties;

import play.Application;
import play.Configuration;
import play.Plugin;

public class JexboxConnectorPlayPlugin extends Plugin{
	protected JexboxConnectorPlay _jexboxConnector = null;

    public JexboxConnectorPlay getJexboxConnector() {
    	if(_jexboxConnector == null){
    		onStart();
    	}
		return _jexboxConnector;
	}

	private final Application _application;

    public JexboxConnectorPlayPlugin(Application application)
    {
        _application = application;
    }
    
	@Override
	public void onStart() {
		Properties props = contruct();
		_jexboxConnector = new JexboxConnectorPlayImpl(props);
	}
	
	private Properties contruct(){
		Configuration configuration = _application.configuration();
		Properties props = new Properties();
		
		String appID = configuration.getString("com.jexbox.connector.play.app-id");
		if(appID == null || appID.length() == 0) throw new RuntimeException("Jexbox application id not found in application.conf");
		props.put("appId", appID);
		
		String host = configuration.getString("com.jexbox.connector.play.host");
		if(host != null && host.length() > 0) props.put("host", host);
		
		String environment = configuration.getString("com.jexbox.connector.play.environment");
		if(environment != null && environment.length() > 0) props.put("environment", environment);

		String ssl = configuration.getString("com.jexbox.connector.play.ssl");
		if(ssl != null && ssl.length() > 0) props.put("ssl", ssl);

		String appVersion = configuration.getString("com.jexbox.connector.play.appVersion");
		if(appVersion != null && appVersion.length() > 0) props.put("appVersion", appVersion);

		String background = configuration.getString("com.jexbox.connector.play.background");
		if(background != null && background.length() > 0) props.put("background", background);
		
		String proxyHost = configuration.getString("com.jexbox.connector.play.proxyHost");
		if(proxyHost != null && proxyHost.length() > 0) props.put("proxyHost", proxyHost);
		
		String proxyPort = configuration.getString("com.jexbox.connector.play.proxyPort");
		if(proxyPort != null && proxyPort.length() > 0) props.put("proxyPort", proxyPort);
		
		String useSystemProxy = configuration.getString("com.jexbox.connector.play.useSystemProxy");
		if(useSystemProxy != null && useSystemProxy.length() > 0) props.put("useSystemProxy", useSystemProxy);
		
		return props;
	}
}
