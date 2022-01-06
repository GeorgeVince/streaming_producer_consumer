# Producer / Consumer example


## Producer
- Created using `python`
- Simulates some junk data representing riders, their location and journey length.#
- Pushes this to the kafka topic `riders`

### Running Kafka

```
# Ensure `kafka` is up and running first

docker-compose up - d
```

### Running Producer
```
# In a seperate terminal...

cd ./producer
virtualenv venv 
pip install requirements.txt
python producer.py
```