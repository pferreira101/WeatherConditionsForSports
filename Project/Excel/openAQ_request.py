import sys
sys.path.append("../")
from Sensorization import config
import requests
import xlsxwriter
import pprint

pp = pprint.PrettyPrinter(indent=4)

# ------------- xls global data  -------------

workbook = xlsxwriter.Workbook('openAQ.xlsx')
worksheet = workbook.add_worksheet("Data")

row = 0
column = 0

# ------------- request openAQ  -------------

city_id = "PT01042"  # Braga

url3 = f'https://api.openaq.org/v1/locations?location={city_id}'

response3 = requests.get(url3).json()

pp.pprint(response3)

# ------------- response handling  -------------


results_stats = response3["results"][0]
air_stats = results_stats["countsByMeasurement"]

print(air_stats)

for item in air_stats:
    worksheet.write(row, column, item["parameter"])
    worksheet.write(row+1, column, item["count"])
    column += 1

workbook.close()
