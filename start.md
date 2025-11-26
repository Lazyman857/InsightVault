```
mysql
mysqld --console

redis
redis-server.exe

elasticsearch
D:\App\Devtools\elasticsearch-8.10.0\bin>elasticsearch

minIO
D:\App\Devtools\minIO>minio.exe server D:\App\Devtools\minIO\data

kafka
bin\windows\kafka-server-start.bat config\kraft\server.properties
```
---
---
Quick start with Docker
```bash
# Start all services
cd docs && docker-compose up -d

# Backend
mvn spring-boot:run

# Frontend
cd frontend && pnpm install && pnpm dev
```

docker command
```bash
如何退出（停止服务）

优雅停止但保留网络与卷：
docker compose stop

完全关闭并删除容器/网络（保留数据卷，数据不丢）：
docker compose down

如需同时删除数据卷（慎用，会清空数据）：
docker compose down -v

查看当前容器状态：
docker compose ps

以后如何使用（再次启动与日常操作）

后续再次启动所有服务（后台运行）：
docker compose up -d

更新到最新镜像后再启动：
docker compose pull

查看某个服务日志（例如 Kafka），便于排查：
docker logs -f kafka

重启单个服务（例如 Elasticsearch）：
docker compose restart es

仅重新创建有更新的服务（Compose 文件改动后）：
docker compose up -d

服务连通性与健康检查（常用验证）
MySQL（进入容器并登录）：
docker exec -it mysql mysql -uroot -p

Redis（带密码 Ping）：
docker exec -it redis redis-cli -a PaiSmart2025 ping

Kafka（列出主题，确认就绪）：
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --list

Elasticsearch（查看集群健康）：
curl -s http://localhost:9200/_cluster/health?pretty
```
