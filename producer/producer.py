from faker import Faker
from kafka import KafkaProducer
from time import sleep
from random import randint
import json
from dataclasses import dataclass, asdict
from typing import List

fake = Faker("en_GB")


@dataclass
class Rider:
    name: str
    address: str
    ride_length: int


def generate_riders(num: int) -> List[Rider]:
    print(f"Generating {num} riders data...")
    riders = []
    for _ in range(num):
        riders.append(
            Rider(name=fake.name(), address=fake.address(), ride_length=randint(1, 100))
        )
    return riders


def push_to_kafka(producer: KafkaProducer, riders: List[Rider]) -> None:
    print(f"Pushing {len(riders)} to kafka")
    for rider in riders:
        producer.send("riders", value=asdict(rider))


def produce() -> None:
    producer = KafkaProducer(
        bootstrap_servers=["localhost:9092"],
        value_serializer=lambda x: json.dumps(x).encode("utf-8"),
    )

    while True:
        riders = generate_riders(randint(1, 10))
        push_to_kafka(producer, riders)
        sleep(randint(0, 200) / 100)


if __name__ == "__main__":
    produce()
