FROM gradle:4.2.1-jdk-alpine as build
WORKDIR jericho
ADD . ./
USER root
RUN chown -R gradle:gradle /home/gradle/jericho && \
    gradle --no-daemon -Pkotlin.incremental=false -Dkotlin.compiler.execution.strategy=in-proces distTar

FROM java:openjdk-8-jre-alpine
EXPOSE 8080
COPY --from=build /home/gradle/jericho/build/distributions/*.tar /tmp/WhoAmI.tar
WORKDIR /usr/src/WhoAmI
RUN tar -xf /tmp/WhoAmI.tar -C ./ && \
    mv Consul-WhoAmI-0.0.1/* ./ && \
    rmdir Consul-WhoAmI-0.0.1
ENTRYPOINT ["bin/Consul-WhoAmI"]