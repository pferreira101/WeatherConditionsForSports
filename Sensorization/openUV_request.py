import requests
import xlsxwriter
import config
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

worksheet.write(row, column, "time")
worksheet.write(row+1, column, response["uv_time"])
column += 1
worksheet.write(row, column, "uv")
worksheet.write(row+1, column, response["uv"])
column += 1
worksheet.write(row, column, "day_max_uv")
worksheet.write(row+1, column, response["uv_max"])
column += 1
worksheet.write(row, column, "day_max_uv_time")
worksheet.write(row+1, column, response["uv_max_time"])

exposure_time = response["safe_exposure_time"]
for st, max_time in exposure_time.items():
    worksheet.write(row, column, st)
    worksheet.write(row+1, column, max_time)
    column += 1


workbook.close()
