import json
import requests
from bs4 import BeautifulSoup as bs
import sqlite3
import mechanize
from selenium import webdriver

#Scraper for scraping relevent information from the criticalreview, uses mechanize to automate logging into CR.

dbPath = "term-project-alang8-fkierzen-jwu175-rdai4/data/courseDatabase.sqlite3"
connection = sqlite3.connect(dbPath)
conn = connection.cursor()
conn.execute('PRAGMA foreign_keys = ON')
baseURL = "https://thecriticalreview.org/search/"

conn.execute('DROP TABLE courseCR')

conn.execute('CREATE TABLE IF NOT EXISTS courseCR (id TEXT, course_rating REAL, prof_rating REAL, avg_hours REAL, max_hours REAL, class_size INTEGER, FOREIGN KEY (id) REFERENCES courseData(id))')

br = mechanize.Browser()

#My cookies - can be found after logging into CR -> Inspect -> Applications -> Cookies
br.addheaders = [('Cookie', 'Cookie: _ga=GA1.2.2125847968.1617162908; connect.sid=s%3ACkGxLGsaHv-4A7yfbmD_c3fqyhvYMpf0.rr0tfWy6lPrc8jD2aXeo2ygNzztVpnjRGGsxGKUKHbc; _gid=GA1.2.82536098.1617162908; _gat=1')]

#Want to get critical review info for every course in the database
conn.execute('SELECT id FROM courseData')
for i in conn.fetchall():
    curCourse = i[0]
    [dept, num] = curCourse.split(" ")

    #The url where this specific course info is stored.
    scrapeURL = baseURL + dept + "/" + num

    br.open(scrapeURL)

    soup = bs(br.response(), "html.parser")
    most_recent = soup.find('div', class_="ui tab")

    if most_recent is None:
        crs_rating = None
        prof_rating = None
        rating = None
        avg_hrs = None
        max_hrs = None
        class_size = None
    else:
        #All the info is inside a div called 'ui tiny statistic'
        stats = most_recent.find_all('div', class_="ui tiny statistic")

        #Grab the numbers!
        crs_rating = stats[0].get_text().strip().split(None, 1)[0]
        prof_rating = stats[1].get_text().strip().split(None, 1)[0]
        avg_hrs = stats[2].get_text().strip().split(None, 1)[0]
        max_hrs = stats[3].get_text().strip().split(None, 1)[0]
        class_size = stats[5].get_text().strip().split(None, 1)[0]

        try:
            crs_rating = float(crs_rating)
        except ValueError:
            # Handle the exception
            crs_rating = None

        try:
            prof_rating = float(prof_rating)
        except ValueError:
            # Handle the exception
            prof_rating = None

        try:
            avg_hrs = float(avg_hrs)
        except ValueError:
            # Handle the exception
            avg_hrs = None

        try:
            max_hrs = float(max_hrs)
        except ValueError:
            # Handle the exception
            max_hrs = None

        try:
            class_size = int(class_size)
        except ValueError:
            # Handle the exception
            class_size = None
    #Insert the info we found
    conn.execute('INSERT INTO courseCR VALUES (?, ?, ?, ?, ?, ?)', (curCourse, crs_rating, prof_rating, avg_hrs, max_hrs, class_size))
connection.commit()
conn.close()
