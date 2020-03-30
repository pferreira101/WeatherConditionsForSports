import sys
sys.path.append("../")
from Sensorization import config
import requests
import xlsxwriter
import pprint

pp = pprint.PrettyPrinter(indent=4)

# ------------- xls global data  -------------

workbook = xlsxwriter.Workbook('openUV.xlsx')
worksheet = workbook.add_worksheet("Data")

row = 0
column = 0

# ------------- request  -------------

coordinates = (41.55032, -8.42005) # Braga

url = f'https://api.openuv.io/api/v1/uv?lat={coordinates[0]}&lng={coordinates[1]}'

headers = {'content-type': 'application/json',
           'x-access-token': config.openuv_api_key}

response = requests.get(url, headers=headers).json()["result"]

pp.pprint(response)

# ------------- response handling  -------------

uv_data = {
    'time' : 'uv_time',
    'uv' : 'uv',
    'day_max_uv' : 'uv_max',
    'day_max_uv_time' : 'uv_max_time'
}

for name, id in uv_data.items():
    worksheet.write(row, column, name)
    worksheet.write(row+1, column, response[id])
    column += 1

exposure_time = response["safe_exposure_time"]
for st, max_time in exposure_time.items():
    worksheet.write(row, column, st)
    worksheet.write(row+1, column, max_time)
    column += 1


workbook.close()
