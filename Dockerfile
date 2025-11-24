FROM confluentinc/cp-kafka-connect:7.5.0
USER root
# Copy the JDBC driver into the plugin directory used by the JDBC connector
COPY postgresql-42.7.8.jar /usr/share/java/kafka-connect-jdbc/postgresql-42.7.8.jar
# (Optional) install confluent-hub connector if not already installed
# RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.9.3
USER appuser
