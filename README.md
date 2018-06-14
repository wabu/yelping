When working with an unknown datasets, it's a good idea to be able to interactivly
look into it and document the specifics about it. Therefore I decided for a
docker base image containing ipython notebook alongside spark. Furthermore the
notebook helps showing some query results.

As this exercise's data is static and the data is not too big, we don't need
the rest of the SMACK stack for now.

The Exploration.ipynb documents the process of getting to know the dataset.
After that, it is straightforward to develop the Convert.scala spark app to
import the dataset into parquet files for analysis.

My example queries can be found inside the Queries.ipynb notebook.


Creating the Container
----------------------

The docker file is straight forward, mainly compiling the converter, seting
some options and extracting the dataset.

Normally you'd have a CI/CD pipeline to package your programs speratly, but
setting up a complete pipeline would be out of scope for this exercies.

The dataset path can be passed to docker via a build arg:
```
docker build -t wabu/yelping --build-arg datset=yelp_dataset.tar.gz . 
```

After this, the container can be run, letting you access spark noteboks on localhost:8888:
```
docker run --name yelping -p 8888:8888 -p 4040:4040 wabu/yelping
```


Using the Converter
-------------------
To start the converter inside the container, you can run:

```
docker exec yelping /usr/local/spark/bin/spark-submit build/target/scala-2.11/yelping_2.11-1.0.jar
```

Executing the Example Queries
-----------------------------

My example quries can be found inside the Queries.ipynb. To access the notebook,
look for the a link with a token in the output of the docker command.

Just open the URL in your browser, perhaps exchanging the docker id with localhost.
Then navigate to notebooks and open the Queries.ipynb.

You can run the code either by using the menu Run/All or by pressing Shift-Enter
on individual cells.


Resume
------

It took me around 4 hours, about 1.5 hour to get a nice docker image with all
needed components, 30 min to explore the dataset, 20 minutes to write the
parser app and the rest to show some examle quires and pack everything together.

All togher I enjoyed this exercies. It was quite opened, so I hope this
solution didn't miss the expectations. The ETL Process itself was unexpectedly
simple from a Data Engineering perspective, as it is a static dataset and no
complex transformations should be applied.

