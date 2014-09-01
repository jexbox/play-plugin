## Jexbox connector for Play! 2.3.x

## How to Install

**Managed dependencies**

    libraryDependencies ++= Seq(
        "commons-codec" % "commons-codec" % "1.9",
        "com.google.code.gson" % "gson" % "2.2.4",
        "com.jexbox.connector" % "jexbox-core" % "0.0.1",
        "com.jexbox.connector" % "jexbox-play" % "0.0.2"
    )
    resolvers += "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

**Manual Jar Installation**  
Download the latest <a class="downloadlink" href="/download/jexbox-play-0.0.2.jar"><i>jexbox-play-0.0.2.jar</i></a> and place it in the lib/ folder.  
The connector depends on  
<a class="downloadlink" target="_blank" href="/download/jexbox-core-0.0.1.jar">jexbox-core-0.0.1.jar</a>  
<a class="downloadlink" target="_blank" href="https://code.google.com/p/google-gson/">com.google.code.gson</a>  
<a class="downloadlink" target="_blank" href="http://commons.apache.org/proper/commons-codec/download_codec.cgi">commons-codec</a>  

The Play! Framework connector is implemented as plug-in for Play! applications and can be declared in conf/play.plugins file as follow  
    10000:com.jexbox.connector.play.JexboxConnectorPlayPlugin  

Then to handle automatically non-catched exceptions, decoration of the GlobalSettings will be required in application Global class  
    public Promise&lt;Result&gt; onError(RequestHeader request, Throwable t) {  
        Session session = Http.Context.current().session();  
        JexboxConnectorPlayPlugin jbp = Play.application().plugin(JexboxConnectorPlayPlugin.class);  
        jbp.getJexboxConnector().send(t, request, session);  
        return super.onError(request, t);  
    }  
    



