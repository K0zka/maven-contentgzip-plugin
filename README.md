# maven-contentgzip-plugin

A static content compressor for maven web applications.

### Basic usage

```
<build>
        <plugins>
                <plugin>
                        <groupId>com.github.K0zka</groupId>
                        <artifactId>maven-contentgzip-plugin</artifactId>
                        <version>0.0.1</version>
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

