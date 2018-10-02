install ojdbc & Jasypt to maven repository

mvn install:install-file -Dfile=lib/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar

<dependency>
    <groupId>com.oracle</groupId>
    <artifactId>ojdbc6</artifactId>
    <version>11.2.0</version>
</dependency>


mvn install:install-file -Dfile=lib/jasypt-1.9.2.jar -DgroupId=org.jasypt -DartifactId=jasypt -Dversion=1.9.2 -Dpackaging=jar

		<dependency>
			<groupId>org.jasypt</groupId>
			<artifactId>jasypt</artifactId>
			<version>1.9.2</version>
		</dependency>


mvn install:install-file -Dfile=lib/jasypt-spring-boot-starter-2.0.0.jar -DgroupId=com.github.ulisesbocchio -DartifactId=jasypt-spring-boot-starter -Dversion=2.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=lib/jasypt-spring-boot-2.0.0.jar -DgroupId=com.github.ulisesbocchio -DartifactId=jasypt-spring-boot -Dversion=2.0.0 -Dpackaging=jar

	    <dependency>
			<groupId>com.github.ulisesbocchio</groupId>
			<artifactId>jasypt-spring-boot-starter</artifactId>
			<version>2.0.0</version>
		</dependency>
		<dependency>
			<groupId>com.github.ulisesbocchio</groupId>
			<artifactId>jasypt-spring-boot</artifactId>
			<version>2.0.0</version>
		</dependency>