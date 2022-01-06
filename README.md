# Producer / Consumer example


## Producer
- Created using `python`
- Simulates some junk data representing riders, their location and the start / end time of journeys
- Pushes this to the kafka topic `riders`

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