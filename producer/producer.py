from faker import Faker
from kafka import KafkaProducer
from time import sleep
from random import randint
import json
from dataclasses import dataclass, asdict
from typing import List
from datetime import datetime, timedelta

fake = Faker("en_GB")

total_riders = 0
total_time = 0


@dataclass
class Rider:
    name: str
    address: str
    event_time: str
    event_type: str


def generate_start_time(num: int) -> List[Rider]:
    print(f"Generating {num} riders data...")
    riders = []
    for _ in range(num):
        riders.append(
            Rider(
                name=fake.name(),
                address=fake.address(),
                event_time=datetime.now().isoformat(),
                event_type="START",
            )
        )
    return riders


def push_end_times(producer: KafkaProducer, riders: List[Rider]) -> None:

    global total_riders
    global total_time

    for rider in riders:
        rider.event_type = "END"
        cur_time = datetime.fromisoformat(rider.event_time)
        extra_minutes = randint(1, 100)
        new_time = cur_time + timedelta(minutes=extra_minutes)
        rider.event_time = new_time.isoformat()

        total_riders += 1
        total_time += extra_minutes
        print(rider)

    push_to_kafka(producer, riders)


def push_to_kafka(producer: KafkaProducer, riders: List[Rider]) -> None:
    print(f"Pushing {len(riders)} to kafka")
    for rider in riders:
        producer.send("riders", value=asdict(rider))


def report_average() -> None:
    global total_riders
    global total_time

    print(
        f"Total riders: {total_riders} - Total time: {total_time} - Avg time: {total_time/total_riders}"
    )


def produce() -> None:
    producer = KafkaProducer(
        bootstrap_servers=["localhost:9092"],
        value_serializer=lambda x: json.dumps(x).encode("utf-8"),
    )

    while True:
        riders = generate_start_time(randint(1, 10))

        print("BATCH START")
        push_to_kafka(producer, riders)
        sleep(randint(0, 200) / 100)
        push_end_times(producer, riders)
        print("BATCH END")
        report_average()
        sleep(randint(0, 200) / 100)


if __name__ == "__main__":
    produce()
