import requests
import config
from firebase_admin import auth
import firebase_admin
from firebase_admin import credentials, auth
from firebase_admin import firestore
import pprint

pp = pprint.PrettyPrinter(indent=4)


cred = credentials.Certificate(config.firestore_key)

# Initialize the default app
default_app = firebase_admin.initialize_app(cred)

#  Initialize another app with a different config
#other_app = firebase_admin.initialize_app(cred, name='other')

# print(default_app.name)    # "[DEFAULT]"

db = firestore.client()

# ------------- request openweathermap  -------------

city_id = 2742032  # Braga

url = f'https://api.openweathermap.org/data/2.5/weather?id={city_id}&appid={config.openweathermap_api_key}'

response = requests.get(url).json()


# ------------- Filter data -------------

general_weather = response["weather"][0]["description"]

weather_stats = response["main"]
feels_like = weather_stats["feels_like"]
temp_min = weather_stats["temp_min"]
temp_max = weather_stats["temp_max"]
pressure = weather_stats["pressure"]
humidity = weather_stats["humidity"]

wind_stats = response["wind"]
speed = wind_stats["speed"]
deg = wind_stats["deg"]
gust = wind_stats["gust"]


# ------------- Save data -------------

doc_ref = db.collection(u'WM').document(f'?key?')

doc_ref.set({
    u'general_weather': f'{general_weather}',
    u'feels_like': f'{feels_like}',
    u'temp_min': f'{temp_min}',
    u'temp_max': f'{temp_max}',
    u'pressure': f'{pressure}',
    u'humidity': f'{humidity}',
    u'wind_speed': f'{speed}',
    u'wind_deg': f'{deg}',
    u'wind_gust': f'{gust}',
})


# ------------- Get data -------------

aq_ref = db.collection(u'WM')
docs = aq_ref.stream()

for doc in docs:
    print(u'{} => {}'.format(doc.id, doc.to_dict()))
