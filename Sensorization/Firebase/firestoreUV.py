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


# ------------- request UV  -------------

coordinates = (41.55032, -8.42005)  # Braga

url = f'https://api.openuv.io/api/v1/uv?lat={coordinates[0]}&lng={coordinates[1]}'

headers = {'content-type': 'application/json',
           'x-access-token': config.openuv_api_key}

response = requests.get(url, headers=headers).json()["result"]


# ------------- Filter data -------------

ozone_time = response["ozone_time"]

uv = response["uv"]
uv_max = response["uv_max"]
uv_max_time = response["uv_max_time"]
uv_time = response["uv_time"]

exposure_time = response["safe_exposure_time"]

st1 = exposure_time["st1"]
st2 = exposure_time["st2"]
st3 = exposure_time["st3"]
st4 = exposure_time["st4"]
st5 = exposure_time["st5"]
st6 = exposure_time["st6"]


# ------------- Save data -------------

doc_ref = db.collection(u'UV').document(f'{ozone_time}')

doc_ref.set({
    u'uv': f'{uv}',
    u'uv_max': f'{uv_max}',
    u'uv_max_time': f'{uv_max_time}',
    u'uv_time': f'{uv_time}',
    u'st1': f'{st1}',
    u'st2': f'{st2}',
    u'st3': f'{st3}',
    u'st4': f'{st4}',
    u'st5': f'{st5}',
    u'st6': f'{st6}'
})


# ------------- Get data -------------

uv_ref = db.collection(u'UV')
docs = uv_ref.stream()

for doc in docs:
    print(u'{} => {}'.format(doc.id, doc.to_dict()))
