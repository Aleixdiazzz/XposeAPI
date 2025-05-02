# XposeAPI

## Database and Storage Setup with Docker Compose

This project uses Docker Compose to set up PostgreSQL for the database and MinIO for object storage.

### Running with Docker Compose (Recommended)

```bash
# Start the containers
docker-compose up -d

# Stop the containers
docker-compose down
```

### MinIO Configuration

MinIO is configured to use a bind mount to the `./data` directory in your project folder. This allows you to:
1. Directly access and manage the files in the `data` folder on your host machine
2. Persist data between container restarts
3. Pre-populate the MinIO storage with existing files

Access MinIO:
- Console: http://localhost:9001
- API: http://localhost:9000
- Login with:
  - Username: xposeMinio
  - Password: xposeMinio

### PostgreSQL Configuration

Access PostgreSQL:
- Host: localhost
- Port: 5431
- Password: password

### Running PostgreSQL Only (Legacy Method)

If you only need the database, you can use the following command:

```bash
docker run --name XposeContainer -e POSTGRES_PASSWORD=password -p 5431:5432 -d postgres
```

### Troubleshooting MinIO Data Access

If MinIO is not retrieving information from your data folder:

1. Make sure the `data` directory exists in the same location as your docker-compose.yml file:
   ```bash
   mkdir data
   ```
2. Check permissions on the `data` directory (MinIO needs read/write access)
3. Restart the containers after making changes:
   ```bash
   docker-compose down
   docker-compose up -d
   ```
