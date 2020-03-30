import sys
sys.path.append("../")
from Sensorization import config
import requests
import xlsxwriter
import pprint

pp = pprint.PrettyPrinter(indent=4)

# ------------- xls global data  -------------

workbook = xlsxwriter.Workbook('openWeatherMap.xlsx')
worksheet = workbook.add_worksheet("Data")

row = 0
column = 0

# ------------- request openweathermap  -------------

city_id = 2742032 # Braga

url2 = f'https://api.openweathermap.org/data/2.5/weather?id={city_id}&appid={config.openweathermap_api_key}'

response2 = requests.get(url2).json()

pp.pprint(response2)

# ------------- response handling  -------------

general_weather = response2["weather"][0]["description"]
worksheet.write(row, column, "description")
worksheet.write(row+1, column, general_weather)

weather_stats = response2["main"]
for stat, value in weather_stats.items():
    worksheet.write(row, column, stat)
    worksheet.write(row+1, column, value)
    column += 1

wind = response2["wind"]
for stat, value in wind.items():
    worksheet.write(row, column, stat)
    worksheet.write(row+1, column, value)
    column += 1

workbook.close()