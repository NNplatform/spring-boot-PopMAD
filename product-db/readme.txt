docker build --no-cache -t product-database .
docker volume create product-db-postgres
docker run -dp 5602:5432 -v product-db-postgres:/var/lib/postgresql/data product-database