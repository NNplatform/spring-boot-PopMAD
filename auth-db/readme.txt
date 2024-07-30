docker build --no-cache -t auth-database .
docker volume create auth-db-postgres
docker run -dp 5600:5432 -v auth-db-postgres:/var/lib/postgresql/data auth-database