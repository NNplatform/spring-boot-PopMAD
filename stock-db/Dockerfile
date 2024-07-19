FROM postgres:latest

ENV POSTGRES_DB=users
ENV POSTGRES_USER=nhoona
ENV POSTGRES_PASSWORD=nhoona

COPY drop /docker-entrypoint-initdb.d/1_drop
COPY schema /docker-entrypoint-initdb.d/2_schema
COPY index /docker-entrypoint-initdb.d/3_index
COPY initial /docker-entrypoint-initdb.d/4_initial

#COPY ./schema/create-schema.sql /docker-entrypoint-initdb.d/
VOLUME ["/var/lib/postgresql/data"]

RUN chmod -R 755 /docker-entrypoint-initdb.d/

EXPOSE 5600
