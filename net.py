import time
from _thread import start_new_thread

import requests


def login(self, smart=False):
    if smart:
        if login(self):
            return True
        else:
            start_new_thread(login_smart, (self,))
            return False
    url = "http://30.30.30.29/drcom/login"
    params = {"callback": "dr1003",
              "DDDDD": str(self.number) + "@" + self.type,
              "upass": self.password}
    if "Auth Error" in requests.get(url, params=params).text:
        return True
    else:
        return False


def login_smart(account):
    every = 10
    print(account)
    while 1:
        if checkTheNetwork():
            print("work")
            login(account)
            time.sleep(every)


class Account(object):
    CAMPUS = ""
    TELECOM = "telecom"

    def __init__(self, number="", password="", type=TELECOM):
        self.number = number
        self.password = password
        self.type = type


def checkTheNetwork():
    url = "http://www.baidu.com"
    try:
        requests.get(url=url, timeout=5)
    except:
        return True
    return False
