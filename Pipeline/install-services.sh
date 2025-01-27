
docker run -d -v jenkins_home:/var/jenkins_home -p 8080:8080 -p 50000:50000 jenkins/jenkins:lts-jdk11;
docker run -d -v jenkins_home:/var/jenkins_home -p 8080:8080 -p 50000:50000 jenkins/jenkins:lts-jdk11
docker run -d \
  -v jenkins_home:/var/jenkins_home \
  -v /path/to/backend:/var/jenkins_home/workspace/Full_stack/backend \
  -p 8080:8080 \
  -p 50000:50000 \
  jenkins/jenkins:lts-jdk11

#docker run -d -p 8081:8081 --name nexus sonatype/nexus3;