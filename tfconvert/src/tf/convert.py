import os
import random
import hashlib
import tensorflow as tf
from PIL import Image


class TfRecordCreator:
    """
    A class to convert YOLO format to TF record

    YOLO record (SRC: https://cloudxlab.com/blog/label-custom-images-for-yolo/):
        Label_ID X_CENTER_NORM Y_CENTER_NORM WIDTH_NORM HEIGHT_NORM
            X_CENTER_NORM = X_CENTER_ABS/IMAGE_WIDTH
            Y_CENTER_NORM = Y_CENTER_ABS/IMAGE_HEIGHT
            WIDTH_NORM = WIDTH_OF_LABEL_ABS/IMAGE_WIDTH
            HEIGHT_NORM = HEIGHT_OF_LABEL_ABS/IMAGE_HEIGHT

        Tf equivalent:
            BBOX_XMIN = X_CENTER_NORM - WIDTH_NORM/2.0
            BBOX_YMIN = Y_CENTER_NORM - HEIGHT_NORM/2.0
            BBOX_XMAX = X_CENTER_NORM + WIDTH_NORM/2.0
            BBOX_YMAX = Y_CENTER_NORM - HEIGHT_NORM/2.0
    """
    clazzes = (
        'book',
        'open book',
        'laptop',
        'mobile',
        'headphone',
        'earphone',
        'face'
    )

    @staticmethod
    def add_int_feature(val):
        """
        Add an integer feature to TF record
        :param val:
        :return:
        """
        return tf.train.Feature(int64_list=tf.train.Int64List(value=[val]))

    @staticmethod
    def add_bytes_feature(val):
        """
        Add a byte feature to TF record
        :param val:
        :return:
        """
        return tf.train.Feature(bytes_list=tf.train.BytesList(value=[val]))

    @staticmethod
    def add_float_feature(val):
        """
        Add a float feature to TF record
        :param val:
        :return:
        """
        return tf.train.Feature(float_list=tf.train.FloatList(value=[val]))

    @staticmethod
    def iterate_path(path):
        """
        Start with given Path, iterate and add all Images and Text in the directory
        :param path:
        :return:
        """
        fl_lst = []
        for fn in os.listdir(path):
            if fn.endswith('.jpg') or fn.endswith('.png'):
                fname, ext = os.path.splitext(fn)
                tn = fname + '.txt'
                fl_lst.append([fn, tn])
        return fl_lst

    @staticmethod
    def split_train_eval(full_list):
        """
        Do a 80-20 split of Array to get Training and Test Set
        :param full_list:
        :return:
        """
        tr_list = []
        ev_list = []
        random.shuffle(full_list)
        tot = len(full_list)
        tot80 = int(0.8 * tot)
        for rg in range(tot):
            if rg < tot80:
                tr_list.append(full_list[rg])
            else:
                ev_list.append(full_list[rg])
        return [tr_list, ev_list]

    def create_tfrec(self, path, imgfile, txtfile):
        """
        Return a TF record
        :param path:
        :param imgfile:
        :param txtfile:
        :return:
        """
        ifname = os.path.join(path, imgfile)
        tfname = os.path.join(path, txtfile)

        # Size
        with Image.open(ifname) as img:
            width, height = img.size

        with tf.gfile.GFile(ifname, 'rb') as eimg:
            enc_img = eimg.read()
            hashed = hashlib.sha256(enc_img).hexdigest().encode('utf8')

        # Class and BBox
        with open(tfname, 'r') as fp:
            aline = fp.read()
            aline_splt = aline.split()
            imclazztxt = self.clazzes[int(aline_splt[0])].encode('utf8')
            imclazzint = int(aline_splt[0])
            cx, cy = float(aline_splt[1]), float(aline_splt[2])
            wx, wy = float(aline_splt[3]), float(aline_splt[4])
            xtl = (cx - wx/2.0)
            ytl = (cy - wy / 2.0)
            xbr = (cx + wx / 2.0)
            ybr = (cy + wy / 2.0)

        fname, ext = os.path.splitext(imgfile)
        imtype = 'jpeg' if (ext == 'jpg') else 'png'

        # Create TF Record
        trec = tf.train.Example(features=tf.train.Features(feature={
            'image/height': self.add_int_feature(height),
            'image/width': self.add_int_feature(width),
            'image/filename': self.add_bytes_feature(imgfile.encode('utf8')),
            'image/source_id': self.add_bytes_feature(imgfile.encode('utf8')),
            'image/key/sha256': self.add_bytes_feature(hashed),
            'image/encoded': self.add_bytes_feature(enc_img),
            'image/format': self.add_bytes_feature(imtype.encode('utf8')),
            'image/object/bbox/xmin': self.add_float_feature(xtl),
            'image/object/bbox/xmax': self.add_float_feature(xbr),
            'image/object/bbox/ymin': self.add_float_feature(ytl),
            'image/object/bbox/ymax': self.add_float_feature(ybr),
            'image/object/class/text': self.add_bytes_feature(imclazztxt),
            'image/object/class/label': self.add_int_feature(imclazzint)
        }))
        return trec

    def create_tffile(self, path, fl_list, outpath):
        """
        Write TF records to TF file
        :param path:
        :param fl_list:
        :param outpath:
        :return:
        """
        with tf.python_io.TFRecordWriter(outpath) as wr:
            for vl in fl_list:
                exam = self.create_tfrec(path, vl[0], vl[1])
                wr.write(exam.SerializeToString())

    def create_label_map(self, outpath):
        """
        Create Label Map file
        :param outpath:
        :return:
        """
        cnt = 1
        with open(outpath, 'w') as fp:
            for itm in self.clazzes:
                fp.write('item {\n')
                fp.write('\tname: "{}"\n'.format(itm))
                fp.write('\tid: {}\n'.format(cnt))
                fp.write('}\n')
                cnt += 1


if __name__ == '__main__':
    img_dir = '/Volumes/ext/codes/ML/exmtr/images'
    evl_pth = '/Volumes/ext/tmp/examev.tfrecord'
    trg_pth = '/Volumes/ext/tmp/examtr.tfrecord'

    trec = TfRecordCreator()
    trec.create_label_map('/Volumes/ext/tmp/examtr.pbtxt')
    file_list = trec.iterate_path(img_dir)
    splt_list = trec.split_train_eval(file_list)
    trec.create_tffile(img_dir, splt_list[0], evl_pth)
    trec.create_tffile(img_dir, splt_list[1], trg_pth)
