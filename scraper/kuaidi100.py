# -*- coding: utf-8 -*-

import requests
from bs4 import BeautifulSoup, Tag

url = r"https://www.kuaidi100.com/"


def get_html(url):
    response = requests.get(url=url)
    return response.text


def get_soup(response_body):
    return BeautifulSoup(response_body, "lxml")


def get_links(soup: Tag):
    lists = soup.find_all("div", class_="com-list")
    par = {}
    for each_list in lists:  # type: Tag
        for item in each_list.find_all("a"):
            if not item.has_attr("data-code"): continue
            if item["data-code"] == "default": continue

            code = item["data-code"]
            code_text = item.text

            par[code] = code_text
            print(code, code_text)

    return par


if __name__ == "__main__":
    par = get_links(get_soup(get_html(url)))
    with open("code.txt", "w", encoding="utf-8") as file:
        for key, value in par.items():
            file.write("{} {}\n".format(key, value))
