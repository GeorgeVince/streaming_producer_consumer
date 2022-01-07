# Producer / Consumer example

I wanted to experiment with Kafka and Scala - two technologies I have not had a chance
to play with yet! :) 

## Producer
- Simulates some log data representing riders, their location and the start / end time of journeys
- Pushes this to the `kafka` topic named `riders`
- Prints out some statistics so we can cross reference these with the consumer
  
### Data format

A thirty minute event would look like this:

```
# Event Start
{
   "name":"Joe Bloggs",
   "address":"123 Flower Lane",
   "event_time":"2022-01-06T12:00:00.000000",
   "event_type":"START"
}

# Event end
{
   "name":"Joe Bloggs",
   "address":"123 Flower Lane",
   "event_time":"2022-01-06T12:30:00.000000",
   "event_type":"END"
}


```

### Running Kafka
Note - Kafka topic is automatically created with `KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"`

```
# Ensure `kafka` is up and running first

docker-compose up - d
```


#### Useful Kafka references:
- https://kafka.apache.org/quickstart
- https://docs.confluent.io/platform/current/quickstart/ce-docker-quickstart.html
- https://rmoff.net/2018/08/02/kafka-listeners-explained/
- https://medium.com/@sdjemails/spark-structured-streaming-joins-bc7f065b2e7
- https://github.com/sylvesterdj/LearningScala


### Running Producer
```
# In a seperate terminal...

cd ./producer
virtualenv venv 
pip install requirements.txt
python producer.py
```

### Consumer
Picks up messagse from kafka and outputs the total average rider time.

Note - I had to run this in a seperate vscode workspace to get scala to work with sbt.
e.g. open /streaming_producer_consumer/consumer in a seperate vscode window and metals should work correctly.

#### Useful Scala references:
- https://shunsvineyard.info/2020/11/20/setting-up-vs-code-for-scala-development-on-wsl/
- https://scalameta.org/metals/docs/editors/vscode/
- https://www.udemy.com/course/taming-big-data-with-spark-streaming-hands-on/

Example Launch configuration (.vscode/launch.json):
```
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "scala",
            "name": "Debug ",
            "request": "launch",
            "mainClass": "com.gv.sparkstreaming.Consume",
            "args": []
        }
    ]
}
```
