import random
from urllib.parse import urlencode
from urllib.request import Request, urlopen


# give a short name to random.choice
def r(l):
    return random.choice(l)


# short name for generating floating point from interval (a,b)
def f(a, b):
    return random.uniform(a,b)


# short name for generating integer from interval [a,b]
def n(a, b):
    return random.randint(a,b)


# short name for random boolean
def b():
    return bool(random.getrandbits(1))


# short name for random subset with n elements
def rs(set, n):
    subset = []
    for _ in range(n):
        choice = r(set)
        while choice in subset:
            choice = r(set)

        subset.append(choice)

    return subset

# Let everyone have 3 names
PATIENT_NAME_1 = ['Oliver', 'George', 'Harry', 'Jack', 'Jacob', 'Noah', 'Charlie', 'Muhammad', 'Thomas', 'Oscar', 'Olivia', 'Amelia', 'Emily', 'Isla', 'Ava', 'Jessica', 'Isabella', 'Lily', 'Ella', 'Mia']
PATIENT_NAME_2 = ['Louise', 'Rose', 'Grace', 'Elizabeth', 'Ann', 'Anne', 'May', 'Mae', 'Marie', 'Mary', 'Amy', 'Catherine', 'Victoria', 'Kate', 'James', 'John', 'William', 'Thomas', 'David', 'Robert', 'Edward', 'Peter', 'Lee', 'Christopher', 'Alexander', 'Michael', 'Daniel']
PATIENT_NAME_3 = ['Smith', 'Jones', 'Taylor', 'Brown', 'Williams', 'Wilson', 'Johnson', 'Davies', 'Robinson', 'Wright', 'Thompson', 'Evans', 'Walker', 'White', 'Roberts', 'Green', 'Hall', 'Wood', 'Jackson', 'Clarke']

GENDER = ['Male', 'Female']

COMMUNITY_NAME = ['Test-Community']
VILLAGE_NAME = ['Osebi', 'Abakaliki', 'Ekerigwe', 'Ezzagu', 'Ejekwe', 'Iboko', 'Ezamgbo']
TOWN_NAME = ['Test-Town']
STATE_NAME = ['Test-State']
LGA_NAME = ['Test-LGA']

STORE_GPS = [(6.451918, 8.299228),
             (6.323483, 8.104392),
             (6.430496, 8.152413),
             (6.353080, 8.178103),
             (6.570226, 8.423917),
             (6.557846, 8.306297),
             (6.397840, 7.970839)
             ]

GPS_ID = ['Test-GPS-ID']
OCCUPATION = ['Test-Occupation']
OCCUPATION_1 = ['Test-Occupation-1']
OCCUPATION_2 = ['Test-Occupation-2']
OCCUPATION_9 = ['Test-Occupation-9']

FARMER_TYPE = ['Test-Farmer-Type']
FARMER_TYPE_4 = ['Test-Farmer-Type-4']
FARMER_TYPE_OTHER = ['Test-Farmer-Type-Other']

TRADER_OCCUPATION = ['Test-Trader-Occupation']
OTHER_OCCUPATION = ['Test-Other-Occupation']

EDUCATION = ['Test-Education']

KNOWS_OF_LASSA_1 = ['Test-Knows-Of-Lassa-1']
INFO_ON_LASSA = ['Test-Info-On-Lassa']
INFO_ON_LASSA_7 = ['Test-Info-On-Lassa-7']
OTHER_INFO_ON_LASSA = ['Test-Other-Info-On-Lassa']

IF_LOCAL_NAME = ['Yes', 'No', "Doesn't know"]
IF_LOCAL_NAME_1 = ['Test-If-Local-Name']

LOCAL_NAME = ['Test-Local-Name']
CAUSE = ['Test-Cause']

all_names = []


def gen_person():
    person = {}

    # Generate nice unique name
    full_rand_name = r(PATIENT_NAME_1) + " " + r(PATIENT_NAME_2) + " " + r(PATIENT_NAME_3)

    while full_rand_name in all_names:
        full_rand_name = r(PATIENT_NAME_1) + " " + r(PATIENT_NAME_2) + " " + r(PATIENT_NAME_3)

    person['patient_name'] = full_rand_name
    all_names.append(full_rand_name)

    # Generate nice other info
    person['age'] = n(18,70)
    person['gender'] = r(GENDER)
    person['temperature'] = f(36.0, 40.0)
    person['village_name'] = r(VILLAGE_NAME)

    # Generate sensible GPS
    (lat, long) = r(STORE_GPS)
    eps = 0.07
    person['store_gps_latitude'] = f(lat-eps, lat+eps)
    person['store_gps_longitude'] = f(long-eps, long+eps)

    person['sick'] = f(0, 1)
    person['risk'] = f(0, 1)

    return person


def add_relations_to_person_json(person):
    person['who_live_with'] = rs(all_names, n(0,5))
    person['who_sharefood_with'] = rs(all_names, n(0,3))
    person['who_work_with'] = rs(all_names, n(0,10))

    return person


def send_person_to_api(person):
    url = 'http://localhost:8000/api/people/'
    post_fields = person

    request = Request(url, urlencode(post_fields).encode())
    response = urlopen(request).read().decode()
    print(response)


def main():
    # Generate trivial info (i.e. a 1000 people) and then add relations
    N = 100
    data = [add_relations_to_person_json(p) for p in [gen_person() for _ in range(N)]]

    # Now send them all to the API
    for p in data:
        send_person_to_api(p)


if __name__ == "__main__":
    main()
