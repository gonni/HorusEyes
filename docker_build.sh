mvn clean package
docker build --tag horus.crawl:0.1 .
docker run -p 18070:8070 -d --name horus.crawld --net horus_net horus.crawl:0.1