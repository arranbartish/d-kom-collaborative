# d-kom-collaborative
Project to be used for collaboration driven development


# Useful commands

```
mvn jcabi-mysql:run
```

starts the mysql instance in the target dorectory for any module that has one

```
mvn jetty:run -Djetty.port=7070
```

Starts the configured jetty instance on port 7070. The configured war files will also be deployed.


# snipits for the exercise

```
<changeSet id="2015-03-17-add-location-to-guest" author="Your name here">
    <addColumn tableName="GUEST">
            <column name="LOCATION" type="varchar(50)">
                <constraints nullable="false"/>
            </column>
    </addColumn>
</changeSet>
```

Change set to add a location


# Network issues?

Use the profile NEXUS to use a LAN based remote repo for artifacts

```
mvn clean install -PNEXUS
```


Requirements

Java 7 or 8
Maven 3.2.5
chrome driver - http://chromedriver.storage.googleapis.com/index.html?path=2.14/

Windows is special

As well as above a  windows machine also need perl. The easiest way to install perl for windows is with padre IDE
perl - https://code.google.com/p/padre-perl-ide/downloads/detail?name=padre-on-strawberry-5.12.3.0-v5.exe&can=2&q=
