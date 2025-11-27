FROM ubuntu:16.04
RUN apt-get update -y
RUN apt install gcc g++ -y
RUN apt install python-pip python-dev python-setuptools -y
RUN pip install eve
COPY . /app
RUN g++ /app/pdfmake.cpp -o pdfmake
VOLUME /app/data
EXPOSE 80
CMD ["python", "/app/pythonApi.py"]