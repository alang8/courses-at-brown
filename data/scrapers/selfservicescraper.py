import mechanize
from bs4 import BeautifulSoup as bs
import re
import sqlite3

dbPath = "term-project-alang8-fkierzen-jwu175-rdai4/data/courseDatabase.sqlite3"
connection = sqlite3.connect(dbPath)
conn = connection.cursor()

baseURL = 'https://selfservice.brown.edu/ss/bwckctlg.p_display_courses?term_in=202020&one_subj=CSCI&sel_crse_strt=0320&sel_crse_end=0320&sel_subj=&sel_levl=&sel_schd=&sel_coll=&sel_divs=&sel_dept=&sel_attr='
p0 = 'https://selfservice.brown.edu/ss/bwckctlg.p_display_courses?term_in='
p2 = '&one_subj='
p4 = '&sel_crse_strt='
p6 = '&sel_crse_end='
p8 = '&sel_subj=&sel_levl=&sel_schd=&sel_coll=&sel_divs=&sel_dept=&sel_attr='
subj = 'CSCI'
code = '0320'
term = '202020'
curURL = '{0}{1}{2}{3}{4}{5}{6}{7}{8}'.format(p0, term, p2, subj, p4, code, p6, code, p8)

spring_term = '201920'
fall_term = '201910'

br = mechanize.Browser()
br.set_handle_robots(False)

conn.execute('SELECT id, sem FROM courseData')

i = conn.fetchone()
print(i)
print(i[0])
print(i[1])

for i in conn.fetchall():
    curCourse = i[0];
    [dept, num] = curCourse.split(" ")
    subj = dept
    code = num
    sem = i[1]
    if sem == 1:
        term = fall_term
    else:
        term = spring_term

    curURL = '{0}{1}{2}{3}{4}{5}{6}{7}{8}'.format(p0, term, p2, subj, p4, code, p6, code, p8)

    br.open(curURL)
    soup = bs(br.response(), "html.parser")
    info = soup.find('td', class_="ntdefault")

    description = re.split('\d\.\d\d\d', info.text)[0].strip()

    regexp = re.compile(r'[A-Z][A-Z][A-Z][A-Z]')
    sentences = re.split("\.", description)
    rel_sentences = filter(lambda s: regexp.search(s), sentences)
    prereq_info = ' '.join(rel_sentences)
    conn.execute('UPDATE courseData SET rawprereq=?, description=? WHERE id=?', (prereq_info, description, curCourse))

connection.commit()
conn.close()
