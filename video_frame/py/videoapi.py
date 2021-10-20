import sys
import cv2
import math
import signal
import logging
import datetime
from flask_cors import CORS
from werkzeug.utils import secure_filename
from flask import Flask, request, jsonify


"""
POC python program to process video data coming from frontend. One frame is extracted
and saved from every chunk that is received from the browser. Entire video will be
also saved to specified FNAME directory location.

Libraries used:
  flask
  flask-cors
  opencv
"""


PORT = 8000
FNAME = '/Users/suvendra/temp/poc/'
APP = Flask(__name__)
CORS(APP)


class VideoApi:
  def __init__(self):
    logging.info('OpenCV version: %s' % cv2.__version__)
    self.save_file = ''
    self.last_frame_count = 0
    self.counter = 0
  
  def create_file(self):
    sfx = datetime.datetime.now().strftime("%H%M%S")
    self.counter = 0
    self.save_file = FNAME + 'F_' + sfx + '.mp4'
    logging.info('Filename generated: %s' % self.save_file)

  def create_return(self, code, msg):
    resp_body = {
      'code': code,
      'msg': msg
    }
    return resp_body

  def process_image(self):
    cap = cv2.VideoCapture(self.save_file)
    if cap.isOpened() == True:
      frame_count = cap.get(cv2.CAP_PROP_FRAME_COUNT)
      skip_frame = self.last_frame_count + math.ceil(frame_count/2)
      self.last_frame_count = frame_count
      cap.set(cv2.CAP_PROP_POS_FRAMES, skip_frame)
      logging.info('Frame Count: %d, reading: %d' % (frame_count, skip_frame))
      self.counter += 1
      ret, frame = cap.read()
      fname = FNAME + str(self.counter) + '.jpg'
      cv2.imwrite(fname, frame)

  def save_data(self, flr):
    chunk_data = flr.read()
    filename = secure_filename(flr.filename)
    with open(self.save_file, 'ab') as fw:
      fw.write(chunk_data)
    self.process_image()
    
  def sigint_handler(self, sig, frame):
    logging.info('Bye, until next time...')
    sys.exit(0)

  #########
  ## REST
  #########
  def hello(self):
    return jsonify(
    	self.create_return(0, 'Hello, recording test')
    ), 200
  
  def start_video(self):
    self.create_file()
    return jsonify(
    	self.create_return(0, 'Hello, created new file: ' + self.save_file)
    ), 200
  
  def save_video(self):
    if request.method == 'POST' and self.save_file != '':
      if 'file' not in request.files:
        logging.error('File not found...')
        return jsonify (
          self.create_return(1, 'File not found')
        ), 500
      
      files = request.files['file']
      self.save_data(files)
      return jsonify(
    	self.create_return(0, 'Saved in file: ' + self.save_file)
      ), 200
    else:
      return jsonify(
    	self.create_return(0, 'Response is closed')
      ), 200

  def end_video(self):
    logging.info('Video send complete, server ready for next file...')
    self.save_file = ''
    self.last_frame_count = 0
    return jsonify(
      self.create_return(0, 'Stopped...')
    ), 200


#########
## MAIN
#########
if __name__ == "__main__":
  logging.basicConfig(format='%(levelname)s:%(message)s', level=logging.DEBUG)

  va = VideoApi()
  signal.signal(signal.SIGINT, va.sigint_handler)
  APP.add_url_rule('/', view_func=va.hello, methods=["GET"])
  APP.add_url_rule('/v/strt', view_func=va.start_video, methods=["POST"])
  APP.add_url_rule('/v/data', view_func=va.save_video, methods=["POST"])
  APP.add_url_rule('/v/done', view_func=va.end_video, methods=["POST"])
  
  logging.info('Starting on port: %d' % PORT)
  APP.run(host="0.0.0.0", port=PORT, debug=True)

