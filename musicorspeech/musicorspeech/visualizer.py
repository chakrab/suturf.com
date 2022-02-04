import os
import glob
import random
import librosa
import librosa.display
import numpy as np
import matplotlib.pyplot as plt

from musicorspeech import MUSIC_FILE_LOCATION, SPEECH_FILE_LOCATION


class Visualizer:
    def get_random_filename(self, dirname):
        files = glob.glob(dirname + '/*')
        return random.sample(files, k=1)[0]

    def plot_spectrogram(self, files):
        plt.figure(figsize=(6, 7), dpi=100)
        plt.subplots_adjust(hspace=0.3)
        for i, filename in enumerate(files):
            plt.subplot(len(files), 1, i+1)
            y, sr = librosa.load(filename)
            spec = librosa.feature.melspectrogram(y=y, sr=sr, n_fft=2048, hop_length=1024)
            mel_spec = librosa.power_to_db(spec, ref=np.max)
            plt.title(os.path.basename(filename))
            librosa.display.specshow(mel_spec, x_axis='time', y_axis='mel', cmap='magma')
            plt.colorbar(format='%+2.0f dB')

        plt.suptitle('Mel Spectrogram')
        plt.show()


if __name__ == '__main__':
    viz = Visualizer()
    array = [viz.get_random_filename(MUSIC_FILE_LOCATION), viz.get_random_filename(SPEECH_FILE_LOCATION)]
    viz.plot_spectrogram(array)
