import mechanize
from bs4 import BeautifulSoup as bs
import re
import sqlite3

#Scraper for scraping relevent course info from self service - for course descriptions and prereq info.

dbPath = "term-project-alang8-fkierzen-jwu175-rdai4/data/courseDatabase.sqlite3"
connection = sqlite3.connect(dbPath)
conn = connection.cursor()

#The url is brutally long...use string builder to put it all together.
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

#Codes for each semester
spring_term = '201920'
fall_term = '201910'

#Mechanize...again
br = mechanize.Browser()
br.set_handle_robots(False)

conn.execute('SELECT id, sem FROM courseData')

i = conn.fetchone()
print(i)
print(i[0])
print(i[1])

#Interested want to do this process for every course in our database.
for i in conn.fetchall():
    curCourse = i[0]
    [dept, num] = curCourse.split(" ")
    subj = dept
    code = num
    sem = i[1]
    if sem == 1:
        term = fall_term
    else:
        term = spring_term

    #Get the self service url.
    curURL = '{0}{1}{2}{3}{4}{5}{6}{7}{8}'.format(p0, term, p2, subj, p4, code, p6, code, p8)

    #Open the page and get the text info using beautiful soup.
    br.open(curURL)
    soup = bs(br.response(), "html.parser")
    info = soup.find('td', class_="ntdefault")

    description = re.split('\d\.\d\d\d', info.text)[0].strip()
    #Regex to catch all sentences that contain a department code (in theory, every sentence that might have info about prereqs)
    # -> this will be used to make manually entering prereq info easier.
    regexp = re.compile(r'[A-Z][A-Z][A-Z][A-Z]')
    sentences = re.split("\.", description)
    rel_sentences = filter(lambda s: regexp.search(s), sentences)
    prereq_info = ' '.join(rel_sentences)
    conn.execute('UPDATE courseData SET rawprereq=?, description=? WHERE id=?', (prereq_info, description, curCourse))

connection.commit()
conn.close()
