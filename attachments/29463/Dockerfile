FROM ruby:2.2-slim
MAINTAINER Gireesh Kumar "gireesh@bluejeansnet.com"

#Install franz
RUN buildDeps=' \
        autoconf \
        bison \
        gcc \
        libbz2-dev \
        libgdbm-dev \
        libglib2.0-dev \
        libncurses-dev \
        libreadline-dev \
        libxml2-dev \
        libxslt-dev \
        make \
        g++ \
    '; \
    set -x \
    && apt-get update \
    && apt-get install -y --no-install-recommends $buildDeps nodejs npm \
    && gem install franz --no-rdoc --no-ri \
    && apt-get purge -y --auto-remove $buildDeps \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* 

COPY src /home/src
RUN cd /home/src && npm install && npm cache clear

#Start Franz
CMD ["node", "/home/src/start.js"]
