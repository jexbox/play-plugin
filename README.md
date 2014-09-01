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