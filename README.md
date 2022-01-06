# Producer / Consumer example


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



### Running Producer
```
# In a seperate terminal...

cd ./producer
virtualenv venv 
pip install requirements.txt
python producer.py
```