import os


class MiscUtils:
    @staticmethod
    def ensure_path(full_path):
        os.makedirs(full_path, exist_ok=True)
