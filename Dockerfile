FROM debian:buster-slim

RUN sed -i 's/deb\.debian\.org\/debian/opensource.nchc.org.tw\/debian/g' /etc/apt/sources.list && \
    apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y curl wget zip unzip

RUN rm /bin/sh && \
    ln -s /bin/bash /bin/sh

RUN curl -s "https://get.sdkman.io" | bash
RUN source "$HOME/.sdkman/bin/sdkman-init.sh"
RUN chmod a+x "$HOME/.sdkman/bin/sdkman-init.sh"

ENV JDK_VER "14.0.2.j9-adpt"
RUN yes | /bin/bash -l -c "sdk install java ${JDK_VER}"
ENV JAVA_HOME "/root/.sdkman/candidates/java/current"

ENV GRD_VER "6.6"
RUN yes | /bin/bash -l -c "sdk install gradle ${GRD_VER}"
ENV GRADLE_HOME "/root/.sdkman/candidates/gradle/current"

VOLUME [ "/app" ]
WORKDIR /app
EXPOSE 8080

COPY . /app
RUN ${GRADLE_HOME}/bin/gradle build

CMD ${GRADLE_HOME}/bin/gradle bootRun
