# maven-contentgzip-plugin

A static content compressor for maven web applications. It runs through your web application at build time and compresses some of the static content, creating a .gz file for each such resource.

This allows the browsers to download compressed content, which saves bandwidth and more importantly saves time for the user and improves the overall experience of the webapplication.

Some of [webjars](http://webjars.org/) also have this enabled and your webjar-packaged libraries will be compressed as well.

### Container support

 - In [jetty](http://eclipse.org/jetty) the support is built into the default servlet for quite some time and it is enalbled by default.
 - In [tomcat](http://tomcat.apache.org/) support for compressed content first appeared in 8.0 and __not enabled by default__, you have to [enable it](http://tomcat.apache.org/tomcat-8.0-doc/default-servlet.html#change) in order to use it!

### Basic usage

```
<build>
        <plugins>
                <plugin>
                        <groupId>com.github.K0zka</groupId>
                        <artifactId>maven-contentgzip-plugin</artifactId>
                        <version>0.0.6</version>
                        <executions>
                                <execution>
                                        <phase>prepare-package</phase>
                                        <goals><goal>process</goal></goals>
                                </execution>
                        </executions>
                </plugin>
        </plugins>
</build>
```

