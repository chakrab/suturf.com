import os
import librosa
import numpy as np
from tensorflow.keras import models

from musicorspeech import DATA_FILE_LOCATION, H5_FILE_NAME


class DataTester:
    def run_test(self, files):
        # First load Model
        model_file = os.path.join(DATA_FILE_LOCATION, H5_FILE_NAME)
        model = models.load_model(model_file)
        model.summary()

        for afile in files:
            print('Analyzing: %s' % afile)
            X, sample_rate = librosa.core.load(afile)
            mel_spec = np.mean(librosa.feature.melspectrogram(X, sr=sample_rate).T, axis=0).reshape(1, -1)
            music_prob = model.predict(mel_spec)
            type_media = 'Music' if float(music_prob[0][0]) > 0.50 else 'Voice'
            print('%s is %s. Probability it is music: %.3f' % (
                afile, type_media, music_prob)
            )


if __name__ == '__main__':
    dt = DataTester()
    dt.run_test(
        ('/Users/suvendra/Downloads/gettysburg.wav',
         '/Users/suvendra/Downloads/CantinaBand60.wav',
         '/Users/suvendra/Downloads/taunt.wav',
         '/Users/suvendra/Downloads/PinkPanther30.wav'
        ))
