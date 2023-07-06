from bs4 import BeautifulSoup
import time
import requests
from datetime import datetime
import mysql.connector

mydb = mysql.connector.connect(
  host="localhost",
  user="root",
  password="",
  database="test"
)

mycursor = mydb.cursor()

url1 = 'http://192.168.43.246/'
url2 = 'http://192.168.43.208/'
url3 = 'http://192.168.43.234/'

data1 = 0
data2 = 0
data3 = 0

while True:
    now = datetime.now().strftime("%d/%m/%Y %H:%M:%S")

    page1 = requests.get(url1)
    page2 = requests.get(url2)
    page3 = requests.get(url3)

    soup1 = BeautifulSoup(page1.text, "html.parser")
    all_p1 = soup1.find_all('p')
    new_data1 = int(all_p1[1].contents[0])
    if new_data1 != data1:
        data1 = new_data1

    soup2 = BeautifulSoup(page2.text, "html.parser")
    all_p2 = soup2.find_all('p')
    new_data2 = int(all_p2[1].contents[0])
    if new_data2 != data2:
        data2 = new_data2

    soup3 = BeautifulSoup(page3.text, "html.parser")
    all_p3 = soup3.find_all('p')
    new_data3 = int(all_p3[1].contents[0])
    if new_data3 != data3:
        data3 = new_data3

    sql = "INSERT INTO all_bins (date_time, distance_1, percent_1, distance_2, percent_2, distance_3, percent_3) " \
          "VALUES (%s, %s, %s, %s, %s, %s, %s)"
    val = (f"{now}", data1, data1/0.16, data2, data2/0.16, data3, data3/0.16)

    print(val)

    mycursor.execute(sql, val)
    mydb.commit()

    time.sleep(5)
