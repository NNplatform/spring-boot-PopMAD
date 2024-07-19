docker volume create stock-db-postgres
docker run -dp 5601:5432 -v stock-db-postgres:/var/lib/postgresql/data stock-database
docker build --no-cache -t stock-database .