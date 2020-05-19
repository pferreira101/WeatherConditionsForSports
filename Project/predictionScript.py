import sys
import firebase_admin
from firebase_admin import auth, credentials, firestore
import numpy as np
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_squared_error, mean_absolute_error
import tensorflow as tf
import math
import pytz
from datetime import datetime

# Get the key
firestore_key = {
  "type": "service_account",
  "project_id": "sa-tp2-1920",
  "private_key_id": "8fdf8d38dca9f654f81acbaf8e5e196f1025d1c2",
  "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCw5nHR1Y/QCo/o\nrpMiVbKBB13eJc1KH55R+N63WdBaGeN3Gul4x0ay46VrVyUnOc98s6YBQ4ilgI0r\nKTLKB7PnIPoZrqlCIz3O8ZhwR5wFTfEjojB/7+KNUSsgWL+VRk6C3Y53SwekxaXK\nz5urXRSZVxM0oU58/tCTsQN9b2cKj958c6wl8MsmhRC0s+2RMREOFVRP2uANolSR\nC9ELCfdB6dOu6eYmZTqZtJwaqxwVd0AxGRaiu4vWdGtfYcwJ6AZtP91hc3hXg4oQ\n1OorCoKW9vSWl25LGygIeziZjjJbRf2uceoHdR/tZ2yaEQOeicB2aZaKjHfKmBgi\nqAWOphC/AgMBAAECggEAPQN+hm0uaQb7k7mCrfj41GACFDjdkY9OP9+ikYK8xhbf\nyZjSpgG1dMXnrBomilz6H3ym6RCt9Mdd6WEQT3ZQqotJN8pyPomBK/tQJInOWlYD\nsdKi/71xWmSAh2uUyJuzQJfCJ4sNW9t/z2/DtVspW6oYybJJK/uHXSlUltLxq0ZX\n2MIb2yuvpr6hZDofa+d3UVALTUeP31wG68qlz/jfYHc+BU+BkCuSgronyj+CBei0\nyFt88ssj80o7/QTMQ3TilpO8e567Ffz+iSJxbwcEMUWesVC27L0wLuH/3ysl7oOz\nFYo3C6JlP1S/JzYCn/GLJDrzlGeSB/aLYnjw+AHlgQKBgQDyt/ua3OMJeQi+RjoY\nWhR5/2sRHVZ+Y8atX05WmQpITXPQTUJIpCG7aPoLX1EP0BeGKWl+eXxStn/Z6n9u\nF88SZ6EtYfybGorDkCJakNCW72YzOgdInry4tF6GRIZmAnKaK3quocLgLgm+7mfH\nYBTu8BNqc2gJ9DIubXYtOZnb8QKBgQC6lHjr2gY/84qiw7GmCIJBhNG2nHIFcQFo\nrhNaKSsimFU7Xj1lP25GzxiVTYDHH+DljYym51T7cRDj0tTDDmXlzaVbhHqjj5no\nXUKuknl81+HBVA2Vu7mSTPmaFR7vWKveFYCI7s7qPl6OabBIF0S36MDnvMZmgP9y\npLOBQGcnrwKBgAKKZnnSghXhTHQhbA5BMyGryMUfF36vMK8z5jAF3hOvq9ysUltM\nTIYXrTZdQw31Uam32UDXqLN88Y2oZLkUSriYmRlOg0RkXLt7UMC971EFJH64xsa9\n0p/kU3D4WqfKssRmXBxj2RRbpwJ2oGN34AA4RxvAJYClK7lMsAX9Kl0hAoGBALJT\noddaRMruzLyQacSVMVnJxl7Q3DHTNlOEvT5ZRr8D9cKOagPRQmcvHQHmV4T6NQtx\n5NLJG3JPOKNnKK9dnYqsqLT1w/l8ENAEAu2zVaY696y911YbXFw2cgIQ2cZNa8cR\nqg42oVFFVz6qSTA9/RIBxg43YTErbft265JHqdBFAoGBAJZnII4TC7ifY5i3Zc7I\nYA+Q/HUz42eltDyF2+WRW0qJigtLIdcFBx2U2FEBz41M3Inlth0WBw02nyQvHrWc\n+C/rYt9CnoOMLpzJpeE8r0+FR9nbg72tJDH/6hU3C1Mrk6h/PrGs6HiPG03KKCj4\nkrj5CGWrpOEbG6mWk6Z1mRrk\n-----END PRIVATE KEY-----\n",
  "client_email": "firebase-adminsdk-l7g7d@sa-tp2-1920.iam.gserviceaccount.com",
  "client_id": "113224319339108551056",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-l7g7d%40sa-tp2-1920.iam.gserviceaccount.com"
}

def prepare_data(df):
    feels_like_history = df[['feels_like']]
    humidity_history = df[['humidity']]
    return feels_like_history, humidity_history

def prepare_uv_data(df):
    uv_history = df[['uv']]
    return uv_history

def normalize_weather_data(df):
    scaler = MinMaxScaler(feature_range=(0, 1))
    df = scaler.fit_transform(df[['feels_like']])
    return scaler, df

def normalize_humidity_data(df):
    scaler = MinMaxScaler(feature_range=(0, 1))
    df = scaler.fit_transform(df[['humidity']])
    return scaler, df

def normalize_uv_data(df):
    scaler = MinMaxScaler(feature_range=(0, 1))
    df = scaler.fit_transform(df[['uv']])
    return scaler, df

def to_supervised(df, timesteps):
    data = df  # array de arrays com os valores
    X, y = list(), list()
    dataset_size = len(data)  # nr de linhas
    for curr_pos in range(dataset_size):
        input_index = curr_pos+timesteps
        label_index = input_index+1
        if label_index < dataset_size:
            X.append(data[curr_pos:input_index, :])
            y.append(data[input_index:label_index, 0])
    return np.array(X), np.array(y)

def build_model(timesteps, features, dropout_rate=0):
    model = tf.keras.Sequential()
    model.add(tf.keras.layers.LSTM(64, return_sequences=True,
                                   input_shape=(timesteps, features)))
    model.add(tf.keras.layers.LSTM(
        128, return_sequences=True, dropout=dropout_rate))
    model.add(tf.keras.layers.LSTM(
        128, return_sequences=True, dropout=dropout_rate))
    model.add(tf.keras.layers.LSTM(
        128, return_sequences=False, dropout=dropout_rate))
    model.add(tf.keras.layers.Dense(64, activation='sigmoid'))
    model.add(tf.keras.layers.Dropout(dropout_rate))
    model.add(tf.keras.layers.Dense(features, activation='linear'))
    model.compile(
        loss=tf.keras.losses.mse,
        optimizer=tf.keras.optimizers.Adam(),
        metrics=['accuracy'])
    return model

def weather_classifier(temp, humidity, uv):
    labels = []

    for i in range(24):
        score = 0
        
        if(temp[i] >= 15 and temp[i] <= 20):
            score = score + 3
        elif(temp[i] >= 10 and temp[i] <= 15):
            score = score + 2
        elif(temp[i] < 10 or temp[i] > 20):
            score = score - 1
        elif(temp[i] < 5 or temp[i] > 25):
            score = score - 2
        
        if(humidity[i] < 50):
            score = score + 1
        elif(humidity[i] > 80):
            score = score - 2

        if(uv[i] <= 3.0):
            score = score + 2
        elif(uv[i] <= 5.9):
            score = score + 1
        elif(uv[i] >= 8.0):
            score = score - 1
        
        if(score >= 4):
            labels.append(3)
        elif(score < 4 and score >= 3):
            labels.append(2)
        elif(score < 3):
            labels.append(1)
    
    return labels

def forecast(model, df, timesteps, multisteps, scaler):
    input_seq = df[-timesteps:]
    inp = input_seq
    predictions = list()
    
    for step in range(1, multisteps+1):
        inp = inp.reshape(1, timesteps, 1)
        yhat = model.predict(inp, verbose=0)
        yhat_inversed = scaler.inverse_transform(yhat)
        predictions.append(yhat_inversed[0][0])
        inp = np.append(inp[0], yhat)
        inp = inp[-timesteps:]
    return predictions


def make_predictions(): 

    cred = credentials.Certificate(firestore_key)

    # Initialize/Get the app
    try:
        app = firebase_admin.initialize_app(cred)
    except:
        app = firebase_admin.get_app()

    # Get the database
    db = firestore.client()

    pd.set_option('display.max_columns', None)

    # ------- Open Weather Map -------

    dataWM = pd.DataFrame(columns=['feels_like','general_weather','humidity','pressure','temp','temp_min','temp_max','wind_speed'])

    wm_ref = db.collection(u'WM')
    docsWM = wm_ref.stream()

    for doc in docsWM:
        params = doc.to_dict()
        dataWM = dataWM.append({'feels_like': float(params['feels_like'])-273.15, 'general_weather':params['general_weather'], 'humidity': int(params['humidity']), 'pressure': int(params['pressure']),'temp_min': float(params['temp_min'])-273.15, 'temp_max': float(params['temp_max'])-273.15, 'wind_speed': float(params['wind_speed'])}, ignore_index=True) # falta "'temp_min': float(params['temp_min'])-273.15,"


    # ------- Open UV -------

    dataUV = pd.DataFrame(columns=['uv','uv_time','uv_max','uv_max_time','st1','st2','st3','st4','st5','st6'])

    uv_ref = db.collection(u'UV')
    docsUV = uv_ref.stream()

    for doc in docsUV:
        params = doc.to_dict()
        dataUV = dataUV.append({'uv': float(params['uv']), 'uv_time': params['uv_time'], 'uv_max': float(params['uv_max']), 'uv_max_time': params['uv_max_time'], 'st1': params['st1'], 'st2': params['st2'], 'st3': params['st3'], 'st4': params['st4'], 'st5': params['st5'], 'st6': params['st6']}, ignore_index=True)

    # -----------------------



    df, df_h = prepare_data(dataWM)
    df_u = prepare_uv_data(dataUV)

    scaler, df = normalize_weather_data(df)
    scaler_h, df_h = normalize_humidity_data(df_h)
    scaler_u, df_u = normalize_uv_data(df_u)


    timesteps = 168 # 24 registos de temperatura capturados ao longo de 7 dias
    features = 1

    X, y = to_supervised(df, timesteps)
    X_h, y_h = to_supervised(df_h, timesteps)
    X_u, y_u = to_supervised(df_u, timesteps)


    model = build_model(timesteps, features)
    model.load_weights('best_weights_weather.hdf5')

    model_h = build_model(timesteps, features)
    model_h.load_weights('best_weights_humidity.hdf5')

    model_u = build_model(timesteps, features)
    model_u.load_weights('best_weights_uv.hdf5')

    multistep = 24


    predictionsWeatherNext24 = forecast(model, df, timesteps, multistep, scaler)
    predictionsHumidityNext24 = forecast(model_h, df_h, timesteps, multistep, scaler_h)
    predictionsUVNext24 = forecast(model_u, df_u, timesteps, multistep, scaler_u)


    scores = weather_classifier(predictionsWeatherNext24, predictionsHumidityNext24, predictionsUVNext24)

    date_time = datetime.now()

    last_hour = date_time.hour

    hours = []
    for i in range(1,25):
        hours.append((last_hour + i) % 24)


    data = {
        u'hours': list(map(float, hours)),
        u'weather_predictions': list(map(float,predictionsWeatherNext24)),
        u'humidity_predictions': list(map(float,predictionsHumidityNext24)),
        u'uv_predictions': list(map(float,predictionsUVNext24)),
        u'scores': list(map(float,scores))
    }



    date_time = datetime.now()

    db.collection(u'Predictions').document(f'{date_time}').set(data)
    return f'Previsões feitas com sucesso!'
 
if __name__ == "__main__":
    make_predictions()

