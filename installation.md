# Xpose API Deployment Guide

This guide explains how to build and deploy the Spring Boot API to a Hetzner VPS using Docker and Nginx, including how to update it as development progresses.

---

## 🧱 Prerequisites

- VPS running Ubuntu with Docker, Docker Compose, and Nginx installed
- Domain (`api.xpose.es`) pointing to your VPS IP via A record (managed in GoDaddy)
- Initial deployment already completed (see previous setup)

---

## 🚀 Deployment Structure

- Your Spring Boot app is built with Java 21
- Deployed as a Docker container
- Reverse-proxied through Nginx (optionally with HTTPS via Certbot)

---

## 🔄 How to Update Deployment After Changes

When you make changes to the API locally and want to redeploy:

### 1. **Build the JAR locally**

In your project root:

```bash
./mvnw clean package
```

This creates a new `target/XposeAPI.jar`.

---

### 2. **Upload Updated Project to VPS**

You can use `scp` or `rsync`. Example with `scp`:

```bash
scp -r * youruser@your-vps-ip:~/xposeApi/
```

> Alternatively, upload only the updated JAR:
>
> ```bash
> scp target/XposeAPI.jar youruser@your-vps-ip:~/xposeApi/target/
> ```

---

### 3. **SSH into the VPS**

```bash
ssh youruser@your-vps-ip
cd ~/xposeApi
```

---

### 4. **Rebuild the Docker Image and Restart**

```bash
docker compose down
docker compose up -d --build
```

This will:

- Rebuild the Docker image with the new JAR
- Restart the container with the latest code

---

## 🧪 Test the API

Access your API at:

```
http://api.xpose.es
```

Or, if HTTPS is set up:

```
https://api.xpose.es
```

Use `curl`, Postman, or a browser to test endpoints.

---

## 🛠️ Useful Docker Commands

- View logs:

  ```bash
  docker logs -f xpose-api
  ```

- Rebuild manually (alternative to Compose):

  ```bash
  docker build -t xpose-api .
  docker run -d -p 8080:8080 --name xpose-api xpose-api
  ```

---

## 🔐 (Optional) Set Up HTTPS (One-Time)

If not done yet:

```bash
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d api.xpose.es
```

Certificates auto-renew with `certbot` installed via systemd timer.

---

## 📂 Project Structure Example

```
xposeApi/
├── Dockerfile
├── docker-compose.yml
├── target/
│   └── XposeAPI.jar
└── README.md
```

---

## 📌 Notes

- Make sure your JAR filename in `Dockerfile` matches exactly (e.g. `XposeAPI.jar`)
- If you rename your app or change ports, update `docker-compose.yml` and Nginx config
- You can also version your builds using image tags

---

## 📬 Questions?

Reach out to your deployment manager or the maintainer of this repo.
