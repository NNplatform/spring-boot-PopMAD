docker build --no-cache -t order-database .
docker volume create order-db-postgres
docker run -dp 5604:5432 -v order-db-postgres:/var/lib/postgresql/data order-database
