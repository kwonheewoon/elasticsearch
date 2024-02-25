# Elasticsearch 기본 이미지 사용
FROM elasticsearch:8.4.2

# elasticsearch.yml 파일을 컨테이너에 복사
COPY ./elasticsearch.yml /usr/share/elasticsearch/config/elasticsearch.yml

# JVM 옵션을 설정하는 새 파일을 jvm.options.d 디렉토리에 추가
COPY ./heap_options /usr/share/elasticsearch/config/jvm.options.d/heap_options

VOLUME ["/my/path/to/elasticsearch/data", "/my/path/to/elasticsearch/logs"]


#docker build -t elasticsearch .

#docker run -d --name elasticsearch \
#    -p 9200:9200 -p 9300:9300 \
#    -v /Users/kwonheewoon/Workspace/elasticsearch/data:/my/path/to/elasticsearch/data \
#    -v /Users/kwonheewoon/Workspace/elasticsearch/logs:/my/path/to/elasticsearch/logs \
#    elasticsearch
