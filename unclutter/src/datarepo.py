class DataRepository:
    _instance = None
    _file_list = []

    @staticmethod
    def instance():
        if DataRepository._instance is None:
            DataRepository()
        return DataRepository._instance

    def __init__(self):
        if DataRepository._instance is not None:
            raise Exception('Singleton! Please get an Instance')
        else:
            DataRepository._instance = self

    def store_files(self, lst):
        DataRepository._file_list = lst

    def get_files(self):
        return DataRepository._file_list

    def get_file(self, name):
        flt = list(filter(lambda s: s['name'] == name, DataRepository._file_list))
        return flt[0] if len(flt) != 0 else None
