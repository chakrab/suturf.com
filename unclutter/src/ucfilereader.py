"""
Load files
"""
import os
import logging


class UcFileReader:
    def __init__(self, basedir):
        self.basedir = basedir

    def list_alldir(self):
        fl_list = []
        for fl in os.scandir(self.basedir):
            if fl.is_file():
                # Just read first line
                with open(fl.path, 'r') as fp:
                    txt = fp.readline()
                fl_list.append({
                    'name': fl.name.replace(' ', '_')[:-4] + ' ((' + txt[:30] + '))',
                    'path': fl.path
                })
        return sorted(fl_list, key=lambda k: k['name'], reverse=True)

    def file_text(self, fpath):
        try:
            with open(fpath, 'r') as fp:
                cntnt = fp.read()
            return cntnt
        except Exception as e:
            logging.error('Caught Exception ', e)
            return None

    def find_text(self, ptrn):
        txt = ''
        srch = ptrn.lower()
        all_files = self.list_alldir()
        for fl in all_files:
            ftxt = self.file_text(fl['path'])
            if ftxt is not None:
                if srch in ftxt.lower():
                    txt += '<<<<* %s *>>>>\n' % fl['name']
                    txt += ftxt + '\n\n'
        return txt

    def save_text(self, name, text):
        try:
            with open(name, 'w') as fp:
                fp.write(text)
            return name
        except Exception as e:
            logging.error('Caught Exception ', e)
            return None

    def del_file(self, name):
        try:
            os.remove(name)
            return name
        except OSError as e:
            logging.error('Caught Exception ', e)
            return None