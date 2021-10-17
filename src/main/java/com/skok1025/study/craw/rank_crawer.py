import json
import sys
from urllib.request import Request, urlopen
from bs4 import BeautifulSoup

type = sys.argv[1]

# 영화 순위
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

# 프리미어리그 순위
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
            'points': tr.find('td', class_='points').text,
            'badge-image' : tr.find('img', class_='badge-image')['src']
        }

        print(json.dumps(data, ensure_ascii=False))

    print("==============================")

# 뉴스 순위
def NewsRank():
    request = Request('https://news.daum.net/ranking/popular')
    response = urlopen(request)
    html = response.read().decode('UTF-8')
    # print(html)

    bs = BeautifulSoup(html, 'html.parser')
    # print(bs.prettify())
    newsList = bs.findAll('li', attrs={'data-tiara-layer': 'news_list'})
    # print(divs)
    for index, news in enumerate(newsList):
        newsItem = news.find('a', class_='link_txt')
        newsImg = news.find('img', class_='thumb_g')

        data = {
            'rank': index + 1,
            'title': newsItem.text,
            'href' : newsItem['href'],
            'img' : newsImg['src']
        }

        print(json.dumps(data, ensure_ascii=False))

    print("==============================")

# 뮤지컬 순위
def MusicalRank():
    request = Request('http://ticket.interpark.com/Contents/Ranking')
    response = urlopen(request)
    html = response.read().decode('UTF-8')
    # print(html)

    bs = BeautifulSoup(html, 'html.parser')
    # print(bs.prettify())
    div = bs.find('div', class_='rankingGenre genre1').findAll('a', attrs={'class': 'prdName'})

    for index, tr in enumerate(div):
        data = {
            'rank': index + 1,
            'name': tr.next
        }
        print(json.dumps(data, ensure_ascii=False))

    print("==============================")


if type == 'movie':
    movieRank()
elif type == 'premier':
    PremierRank()
elif type == 'news':
    NewsRank()
elif type == 'musical':
    MusicalRank()

# 배달 순위
# 여행지 순위
# 립스틱 순위