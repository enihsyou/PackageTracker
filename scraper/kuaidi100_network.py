# -*- coding: utf-8 -*-
import requests

url = "https://www.kuaidi100.com/network/www/searchapi.do"

payload = {
    'area': '上海-上海市',
    # 'auditStatus': '0',
    # 'company': '',
    # 'from': 'null',
    'keyword': '上海理工大学',
    'method': 'searchnetwork',
    'offset': '0',
    'size': '8',
}

headers = {
    # 'x-requested-with': "XMLHttpRequest",
    'content-type': "application/x-www-form-urlencoded",
}

response = requests.request("POST", url, data=payload, headers=headers)

print(response.text)
print('{:_<80}'.format(''))

url = "https://www.kuaidi100.com/courier/searchapi.do"

payload = {
    'method': 'courieraround',
    'json': '{"xzqname":"上海-上海市","keywords":"上海理工大学"}'
}
# response = requests.request("POST", url, data=payload, headers=headers)

# print(response.text)
