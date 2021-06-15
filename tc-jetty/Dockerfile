#
# Copyright 2021 - TOOP Project
#
# This file and its contents are licensed under the EUPL, Version 1.2
# or – as soon they will be approved by the European Commission – subsequent
# versions of the EUPL (the "Licence");
#
# You may not use this work except in compliance with the Licence.
# You may obtain a copy of the Licence at:
#
#       https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
#
# Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
# See the Licence for the specific language governing permissions and limitations under the Licence.
#

FROM adoptopenjdk:8-jre-hotspot

ARG VERSION="2.1.1"
ARG JAR_NAME=tc-jetty-${VERSION}-full.jar

#create tc folder
WORKDIR /tc-jetty

ENV JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/urandom" \
    JAR_NAME="${JAR_NAME}"

RUN curl https://oss.sonatype.org/service/local/repositories/releases/content/eu/toop/tc-jetty/${VERSION}/${JAR_NAME} -o tc-jetty.jar

ENTRYPOINT ["java", "-jar", "tc-jetty.jar"]
CMD ["start"]
