FROM postgres:latest

ENV POSTGRES_DB=cart
ENV POSTGRES_USER=nhoona
ENV POSTGRES_PASSWORD=nhoona

COPY drop/*.sql /docker-entrypoint-initdb.d/01-drop.sql
COPY schema/*.sql /docker-entrypoint-initdb.d/02-schema.sql
COPY index/*.sql /docker-entrypoint-initdb.d/03-index.sql


#COPY ./schema/create-schema.sql /docker-entrypoint-initdb.d/
VOLUME ["/var/lib/postgresql/data"]

RUN chmod -R 755 /docker-entrypoint-initdb.d/

EXPOSE 5603
