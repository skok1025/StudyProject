import json
import sys
from urllib.request import Request, urlopen
from bs4 import BeautifulSoup

type = sys.argv[1]

def movieRank():
    crawLink = 'https://movie.naver.com/movie/sdb/rank/rmovie.nhn'
    request = Request(crawLink)
    response = urlopen(request)
    html = response.read().decode('UTF-8')
    # print(html)

    bs = BeautifulSoup(html, 'html.parser')
    # print(bs.prettify())
    divs = bs.findAll('div', attrs={'class': 'tit3'})

    movieRank = {
        'crawLink' : crawLink,
        'rankInfo' : {}
    };

    # print(divs)
    for index, div in enumerate(divs):
        data = {
            'rank' : index + 1,
            'name' : div.a.text,
            'link' : 'https://movie.naver.com' + div.a['href']
        }
        movieRank['rankInfo'][index] = data
        #print(json.dumps(data, ensure_ascii=False))
    print(json.dumps(movieRank, ensure_ascii=False))
    # print("==============================")

def PremierRank():
    request = Request('https://www.premierleague.com/tables?co=1&se=42&mw=-1&ha=-1')
    response = urlopen(request)
    html = response.read().decode('UTF-8')
    # print(html)

    bs = BeautifulSoup(html, 'html.parser')
    # print(bs.prettify())
    trs = bs.findAll('tr', attrs={'data-compseason': '418'})
    # print(divs)
    for index, tr in enumerate(trs):
        data = {
            'rank': index + 1,
            'name': tr['data-filtered-table-row-name'],
            'points': tr.find('td', class_='points').text
        }

        print(json.dumps(data, ensure_ascii=False))

    print("==============================")

if type == 'movie' :
    movieRank()
elif type == 'premier':
    PremierRank()
