import os
import logging
from vignere import Vignere


class NetSafe:
    def __init__(self):
        pass

    def file_conv(self, mode, in_dir, out_dir):
        vgn = Vignere('BIGFATCOWJUMPEDOVERTHEMOON')
        for fl in os.scandir(in_dir):
            if fl.is_file():
                try:
                    with open(fl.path, 'r') as fp:
                        cntnt = fp.read()
                        if mode == 'e':
                            enc = vgn.encode_text(cntnt)
                            with open(out_dir + '/' + fl.name, 'w+') as fw:
                                fw.write(enc)
                        else:
                            dec = vgn.decode_text(cntnt)
                            with open(out_dir + '/' + fl.name, 'w+') as fw:
                                fw.write(dec)
                except Exception as e:
                    logging.error('Caught Exception ', fl.name, e)
                    continue
