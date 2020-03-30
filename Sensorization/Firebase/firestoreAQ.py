import sys
sys.path.append("../")
from Sensorization import config
import requests
import firebase_admin
from firebase_admin import auth, credentials, firestore
import pprint

pp = pprint.PrettyPrinter(indent=4)


cred = credentials.Certificate(config.firestore_key)

# Initialize the default app
default_app = firebase_admin.initialize_app(cred)

#  Initialize another app with a different config
#other_app = firebase_admin.initialize_app(cred, name='other')

print(default_app.name)    # "[DEFAULT]"

db = firestore.client()


# ------------- request openAQ  -------------

city_id = "PT01042"  # Braga

url = f'https://api.openaq.org/v1/locations?location={city_id}'

response = requests.get(url).json()


# ------------- Filter data -------------

results_stats = response["results"][0]
air_stats = results_stats["countsByMeasurement"]

lastUpdated = results_stats["lastUpdated"]

count_no2 = air_stats[0]["count"]
count_o3 = air_stats[1]["count"]
count_pm10 = air_stats[2]["count"]


# ------------- Save data -------------

doc_ref = db.collection(u'AQ').document(f'{lastUpdated}')

doc_ref.set({
    u'no2': f'{count_no2}',
    u'o3': f'{count_o3}',
    u'pm10': f'{count_pm10}'
})


# ------------- Get data -------------

aq_ref = db.collection(u'AQ')
docs = aq_ref.stream()

for doc in docs:
    print(u'{} => {}'.format(doc.id, doc.to_dict()))
