"""
Vignere Cypher implementation
"""
import math


class Vignere:
    def __init__(self, pwd):
        self.pwd = pwd
        self.tableau = '' . join([chr(i) for i in range(33, 127)])

    def generate_key(self, ln):
        return self.pwd * math.ceil(ln/len(self.pwd))

    def vignere(self, txt, mde):
        cnt = -1
        enc_txt = []
        tbl_len = len(self.tableau)
        key_txt = self.generate_key(len(txt))
        for ch in txt:
            cnt += 1
            idx = self.tableau.find(ch)
            if idx != -1:
                if mde == 'e':
                    idx += self.tableau.find(key_txt[cnt])
                else:
                    idx -= self.tableau.find(key_txt[cnt])
                idx %= tbl_len
                enc_txt.append(self.tableau[idx])
            else:
                enc_txt.append(ch)

        return ''.join(enc_txt)

    def encode_text(self, txt):
        if txt.startswith('#@@#%s'):
            return txt
        else:
            return '#@@#%s' % self.vignere(txt, 'e')

    def decode_text(self, txt):
        if txt.startswith('#@@#'):
            return self.vignere(txt[4:], 'd')
        else:
            return txt
