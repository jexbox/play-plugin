## About Jexbox

<a target="_blank" href="https://jexbox.com">Jexbox<a/> is a powerful Java based exception tracking system which captures and stores application exceptions in online database. There is no need to search endless log files - stored exception data (type, message, stack trace, line in page, request & session data) is available at any time in your account. Thanks to its intelligent grouping & report system, software engineers are now capable to track all possible exception situations by searching and analyzing existing data.

## How to Install the Connector for Play! 2.3.x

**Managed dependencies**

Add `jexbox-play` dependencies to your `project/Build.scala` file as follow

    libraryDependencies ++= Seq(
        "commons-codec" % "commons-codec" % "1.9",
        "com.google.code.gson" % "gson" % "2.2.4",
        "com.jexbox.connector" % "jexbox-core" % "0.0.1",
        "com.jexbox.connector" % "jexbox-play" % "0.0.2"
    )
    resolvers += "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

**Manual Jar Installation**  
Download the latest <a href="https://jexbox.com/download/jexbox-play-0.0.2.jar">jexbox-play-0.0.2.jar</a> and place it in the `lib/` folder.  
The connector depends on  
<a target="_blank" href="https://jexbox.com/download/jexbox-core-0.0.1.jar">jexbox-core-0.0.1.jar</a>  
<a target="_blank" href="https://code.google.com/p/google-gson/">com.google.code.gson</a>  
<a target="_blank" href="http://commons.apache.org/proper/commons-codec/download_codec.cgi">commons-codec</a>  

The Play! Framework connector is implemented as plug-in for Play! applications and can be declared in `conf/play.plugins` file as follow  
  
    10000:com.jexbox.connector.play.JexboxConnectorPlayPlugin  

Then to handle automatically non-catched exceptions, decoration of the `GlobalSettings` will be required in application `Global` class  

    public Promise<Result> onError(RequestHeader request, Throwable t) {  
        Session session = Http.Context.current().session();  
        JexboxConnectorPlayPlugin jbp = Play.application().plugin(JexboxConnectorPlayPlugin.class);  
        jbp.getJexboxConnector().send(t, request, session);  
        return super.onError(request, t);  
    }  

Then the connector should be configured with settings in the `conf/application.conf` file as follow  
            
    #Required  
    com.jexbox.connector.play.app-id=copy here the application ID, available in www.jexbox.com admin panel  
      
    #Optional  
    com.jexbox.connector.play.host=  
    com.jexbox.connector.play.environment=  
    com.jexbox.connector.play.ssl=  
    com.jexbox.connector.play.appVersion=  
    com.jexbox.connector.play.background=  
    com.jexbox.connector.play.proxyHost=  
    com.jexbox.connector.play.proxyPort=  
    com.jexbox.connector.play.useSystemProxy=

To send catched exceptions, the instance of Jexbox connector can be obtained in application controllers as follow  

    ...  
    ...  
    ...
    } catch (Throwable e) {  
        Session session = Http.Context.current().session();  
        Request request = Http.Context.current().request();  
        JexboxConnectorPlayPlugin jbp = Play.application().plugin(JexboxConnectorPlayPlugin.class);  
        jbp.getJexboxConnector().send(t, request, session);
    }


