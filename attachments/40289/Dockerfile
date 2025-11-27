FROM debian

RUN dpkg --add-architecture i386 && \
    apt-get update && \
    apt-get -y install wine && \
    rm -rf /var/lib/apt/lists/*

ADD dependencies/helloworld.exe /usr/wine_test/helloworld.exe