import sys
from signal import signal, SIGINT
from pymongo import MongoClient


class InterruptHandler:
    def __init__(self, clnt):
        self.mongo_client = clnt

    def __call__(self, signum, sigframe):
        if self.mongo_client:
            self.mongo_client.close()
        sys.exit(1)


class MongoCommon:
    @staticmethod
    def get_db_connection(url, dbname):
        client = MongoClient(url)
        # Put a Ctrl-C handler
        # signal(SIGINT, InterruptHandler(client))
        conn = client[dbname]
        return client, conn

    @staticmethod
    def close_connection(clnt):
        if clnt:
            clnt.close()
