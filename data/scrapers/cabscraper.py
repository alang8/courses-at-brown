print("-- CAB SCRAPER --")
import json
import requests
import sqlite3

# Scraper for getting course information from C@B. Huge thanks to Jack Wrenn Brown CS PHD for help with this!!

url = "https://cab.brown.edu/api/?page=fose&route=search"
dbPath = "term-project-alang8-fkierzen-jwu175-rdai4/data/courseDatabase.sqlite3"
connection = sqlite3.connect(dbPath)
conn = connection.cursor()
conn.execute('PRAGMA foreign_keys = ON')

#The department we are currently scraping for.

cur_dept = 'APMA'
def get_courses():
    #The codes for the semesters we are interested in.
    spring1920 = "201920"
    fall1920 = "201910"

    req_data = {
        "other": {"srcdb": fall1920},
        "criteria": []
    }

    response = requests.post(url=url, json=req_data)
    print(response)

    sem_courses = response.json()["results"]
    #Only interested in the courses, not labs or sections or anything else.
    fall_courses = list(filter(lambda x: x["code"].startswith(cur_dept) and not x["code"].startswith('2', 5) and x["no"] == "S01", sem_courses))

    req_data['other']['srcdb'] = spring1920
    response = requests.post(url=url, json=req_data)
    spring_courses = response.json()["results"]
    spring_courses = list(filter(lambda x: x["code"].startswith(cur_dept) and not x["code"].startswith('2', 5) and x["no"] == "S01", spring_courses))

    cs_course_objs = {}
    # The info looks like this at this point...
    # {'key': '1820',
    #'code': 'CSCI 0330',
    #'title': 'Introduction to Computer Systems',
    #'crn': '16126',
    #'no': 'S01',
    #'total': '1',
    #'schd': 'S',
    #'stat': 'A',
    #'isCancelled': '',
    #'meets': 'MWF 2-2:50p',
    #'mpkey': '2053',
    #'meetingTimes': '[{"meet_day":"0","start_time":"1400","end_time":"1450"},{"meet_day":"2","start_time":"1400","end_time":"1450"},{"meet_day":"4","start_time":"1400","end_time":"1450"}]',
    #'instr': 'T. Doeppner',
    #'start_date': '2019-09-04',
    #'end_date': '2019-12-21',
    #'permreq': 'N',
    #'rpt': 'N',
    #'rfam': '458',
    #'lrfam': '',
    #'changehash': 'omT9n9UlCynaUWtDwvHDKw',
    #'srcdb': '201910'}

#sem = 0 if both, 1 is fall, 2 is spring
    for c in fall_courses:
        conn.execute('SELECT * FROM courseData WHERE id = ?', (c['code'],))
        data = conn.fetchone()
        if data is None:
            #course not in table yet
            conn.execute('INSERT INTO courseData VALUES (?, ?, ?, ?, ?, ?, ?)', (c['code'], c['title'], c['instr'], 1, None, None, None))

        else:
            #course in table already
            print('error inserting')

    for c in spring_courses:
        conn.execute('SELECT * FROM courseData WHERE id = ?', (c['code'],))
        data = conn.fetchone()
        if data is None:
            #course not in table yet
            conn.execute('INSERT INTO courseData VALUES (?, ?, ?, ?, ?, ?, ?)', (c['code'], c['title'], c['instr'], 2, None, None, None))

        else:
            #course in table already, set sem = 0 since must be offered in both
            conn.execute('UPDATE courseData SET sem=? WHERE id=?', (0, c['code']))

    connection.commit()
    conn.close()

get_courses()
