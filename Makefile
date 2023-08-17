build:
	mvn -PPackage clean package

deploy:
	mkdir ~/tools/providence
	tar --strip-components=1 -xvf ./providence-collector/target/providence-collector-0.0.7-SNAPSHOT-bin.tar.gz -C ~/tools/providence
