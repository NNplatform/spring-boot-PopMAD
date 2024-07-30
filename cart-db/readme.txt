docker build --no-cache -t cart-database .
docker volume create cart-db-postgres
docker run -dp 5603:5432 -v cart-db-postgres:/var/lib/postgresql/data cart-database
