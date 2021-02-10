import os
import json
import logging
import datetime
import urllib.parse
from http.server import BaseHTTPRequestHandler,HTTPServer
from ucfilereader import UcFileReader
from vignere import Vignere


class RestServer(BaseHTTPRequestHandler):
    PASSWORD = 'BIGFATCOWJUMPEDOVERTHEMOON'
    FILE_LOCATION = '../data'

    static_dir = os.path.join(os.path.dirname(__file__), '../html')
    os.chdir(static_dir)

    def do_GET(self):
        vgn = Vignere(RestServer.PASSWORD)
        flr = UcFileReader(RestServer.FILE_LOCATION)
        if self.path == '/':
            # Version
            self.send_response(200)
            self.send_header('Content-type', 'text/plain')
            self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
            self.end_headers()
            self.wfile.write(bytes('Version: alpha', 'utf-8'))
        elif self.path == '/list':
            # Return a JSON object for all files
            lstfl = flr.list_alldir()
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
            self.end_headers()
            self.wfile.write(bytes(json.dumps(lstfl), 'utf-8'))
        elif self.path.startswith('/files/reg?'):
            # Regular Text
            parms = urllib.parse.parse_qs(self.path[11:])
            try:
                name = parms['name']
                if name is None:
                    raise KeyError('File name missing')
                txt = flr.file_text(name[0])
                if txt is None:
                    raise KeyError('File invalid')
                self.send_response(200)
                self.send_header('Content-type', 'text/plain')
                self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
                self.end_headers()
                self.wfile.write(bytes('%s' % txt, 'utf-8'))
            except KeyError as e:
                logging.error('Error: ', e)
                self.send_response(400)
                self.send_header('Content-type', 'text/plain')
                self.end_headers()
                self.wfile.write(bytes('Server Error, see logs...', 'utf-8'))
        elif self.path.startswith('/files/enc?'):
            # Encode text
            parms = urllib.parse.parse_qs(self.path[11:])
            try:
                name = parms['name']
                if name is None:
                    raise KeyError('File name missing')
                txt = flr.file_text(name[0])
                enc = vgn.encode_text(txt)
                if txt is None:
                    raise KeyError('File invalid')
                self.send_response(200)
                self.send_header('Content-type', 'text/plain')
                self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
                self.end_headers()
                self.wfile.write(bytes('%s' % enc, 'utf-8'))
            except KeyError as e:
                logging.error('Error: ', e)
                self.send_response(400)
                self.send_header('Content-type', 'text/plain')
                self.end_headers()
                self.wfile.write(bytes('Server Error, see logs...', 'utf-8'))
        elif self.path.startswith('/files/dec?'):
            # Decoded text
            parms = urllib.parse.parse_qs(self.path[11:])
            try:
                name = parms['name']
                if name is None:
                    raise KeyError('File name missing')
                txt = flr.file_text(name[0])
                dec = vgn.decode_text(txt)
                if txt is None:
                    raise KeyError('File invalid')
                self.send_response(200)
                self.send_header('Content-type', 'text/plain')
                self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
                self.end_headers()
                self.wfile.write(bytes('%s' % dec, 'utf-8'))
            except KeyError as e:
                logging.error('Error: ', e)
                self.send_response(400)
                self.send_header('Content-type', 'text/plain')
                self.end_headers()
                self.wfile.write(bytes('Server Error, see logs...', 'utf-8'))
        elif self.path.startswith('/find?'):
            # Find
            parms = urllib.parse.parse_qs(self.path[6:])
            try:
                srch = parms['pattern']
                if srch is None:
                    raise KeyError('Pattern missing')
                txt = flr.find_text(srch[0])
                self.send_response(200)
                self.send_header('Content-type', 'text/plain')
                self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
                self.end_headers()
                self.wfile.write(bytes('%s' % txt, 'utf-8'))
            except KeyError as e:
                logging.error('Error: ', e)
                self.send_response(400)
                self.send_header('Content-type', 'text/plain')
                self.end_headers()
                self.wfile.write(bytes('Server Error, see logs...', 'utf-8'))
        else:
            # Return static page
            try:
                with open(self.path[1:], 'r') as fp:
                    cntnt = fp.read()
                self.send_response(200)
                self.send_header('Content-type', 'text/html')
                self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
                self.end_headers()
                self.wfile.write(bytes('%s' % cntnt, 'utf-8'))
            except Exception as e:
                logging.error('Caught Exception ', e)
                self.send_response(400)
                self.send_header('Content-type', 'text/plain')
                self.end_headers()
                self.wfile.write(bytes('Invalid option %s' % self.path, 'utf-8'))

    def do_POST(self):
        if self.path == '/files/save':
            flr = UcFileReader(RestServer.FILE_LOCATION)
            length = int(self.headers['Content-Length'])
            post_data = urllib.parse.parse_qs(self.rfile.read(length).decode('utf-8'))
            if post_data.get('name')[0] == 'NEW':
                now = datetime.datetime.today()
                flname = ('%s/Unclutter Note %s.txt' % (self.FILE_LOCATION, now.strftime('%Y-%m-%d %H.%M.%S')))
            else:
                flname = post_data.get('name')[0]

            # Update file
            if post_data.get('text') is not None:
                rtn = flr.save_text(flname, post_data.get('text')[0])
                if rtn is not None:
                    lstfl = flr.list_alldir()
                    self.send_response(200)
                    self.send_header('Content-type', 'application/json')
                    self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
                    self.end_headers()
                    self.wfile.write(bytes(json.dumps(lstfl), 'utf-8'))
                else:
                    self.send_response(400)
                    self.send_header('Content-type', 'text/plain')
                    self.end_headers()
                    self.wfile.write(bytes('File Save Error', 'utf-8'))
            else:
                self.send_response(400)
                self.send_header('Content-type', 'text/plain')
                self.end_headers()
                self.wfile.write(bytes('Invalid option: Empty String', 'utf-8'))
        else:
            self.send_response(400)
            self.send_header('Content-type', 'text/plain')
            self.end_headers()
            self.wfile.write(bytes('Invalid option %s' % self.path, 'utf-8'))

    def do_DELETE(self):
        if self.path.startswith('/files/del?'):
            # Path
            parms = urllib.parse.parse_qs(self.path[11:])
            try:
                name = parms['name']
                if name is None:
                    raise Exception('Name missing')
                flr = UcFileReader(RestServer.FILE_LOCATION)
                rtn = flr.del_file(name[0])
                if rtn is not None:
                    self.send_response(200)
                    self.send_header('Content-type', 'text/plain')
                    self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
                    self.end_headers()
                    self.wfile.write(bytes('Deleted file %s' % name[0], 'utf-8'))
                else:
                    raise Exception('Error delete %s' % name[0])
            except Exception as e:
                logging.error('Error: ', e)
                self.send_response(400)
                self.send_header('Content-type', 'text/plain')
                self.end_headers()
                self.wfile.write(bytes('Server Error, see logs...', 'utf-8'))
        else:
            self.send_response(400)
            self.send_header('Content-type', 'text/plain')
            self.end_headers()
            self.wfile.write(bytes('Invalid option %s' % self.path, 'utf-8'))

    def do_OPTIONS(self):
        self.send_response(204)
        self.send_header('Access-Control-Allow-Headers', '*')
        self.send_header('Access-Control-Allow-Origin', self.headers['Origin'])
        self.send_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS, DELETE')
        self.send_header('Access-Control-Max-Age', '86400')
        self.end_headers()


if __name__ == '__main__':
    host = 'localhost'
    port = 8080

    logging.basicConfig(level=logging.INFO)
    srv = HTTPServer((host, port), RestServer)
    logging.info('Server started at %s:%d' % (host, port))
    try:
        srv.serve_forever()
    except KeyboardInterrupt as e:
        logging.info('Caught Interrupt, will terminate...')

    srv.server_close()
    logging.info('Server stopped successfully...')