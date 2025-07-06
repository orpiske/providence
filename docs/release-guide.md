export CURRENT_DEVELOPMENT_VERSION=0.0.9 NEXT_DEVELOPMENT_VERSION=0.0.10

mvn -PPackage clean package
jreleaser full-release -Djreleaser.project.version=${CURRENT_DEVELOPMENT_VERSION}-SNAPSHOT --dry-run


mvn --batch-mode -Dtag=providence-${CURRENT_DEVELOPMENT_VERSION} release:prepare -DreleaseVersion=${CURRENT_DEVELOPMENT_VERSION} -DdevelopmentVersion=${NEXT_DEVELOPMENT_VERSION}-SNAPSHOT
mvn -PPackage release:perform -Dgoals=install

git checkout providence-${CURRENT_DEVELOPMENT_VERSION}
mvn -PPackage clean package
jreleaser full-release -Djreleaser.project.version=${CURRENT_DEVELOPMENT_VERSION}