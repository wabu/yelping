# simple container with spark & jupyter
# based on spark notebook distribution from jupyter
FROM jupyter/all-spark-notebook:03b897d05f16

MAINTAINER Daniel Davis <daniel.wabu.davis@gmail.com>

ARG dataset=data/yelp_dataset.tar.gz

USER root

# setup sbt
RUN wget -O- "https://github.com/sbt/sbt/releases/download/v0.13.15/sbt-0.13.15.tgz" \
    |  tar xzf - -C /usr/local --strip-components=1

# setup spark options
COPY notebooks/ notebooks/
COPY build.sbt build/
COPY src/ build/src/
RUN echo "spark.driver.memory=4G" >> /usr/local/spark/conf/spark-defaults.conf

# setup directories used in image
RUN mkdir /data && chown -R $NB_UID /data notebooks build

USER $NB_UID

# build the converter
RUN cd build/ && sbt package

# extract the dataset
COPY $dataset /data
RUN cd /data && tar xvzf *.tar.gz && rm *.tar.gz

