import os
import numpy as np
import pandas as pd
from tensorflow.keras import models, layers, callbacks
from sklearn.model_selection import train_test_split

from musicorspeech import DATA_FILE_LOCATION, FEATURE_FILE, LABEL_FILE, \
    EXPORT_FILE, MEL_VECTOR_SIZE, H5_FILE_NAME


class DataTrainer:
    def load_data(self):
        """
        This method will be used to load the data and create features/ labels
        from the CSV file we had generated earlier. There is no need for
        rebalancing as both contains 64 items
        """
        # Return if files were already created
        npy_feature_file = os.path.join(DATA_FILE_LOCATION, FEATURE_FILE + '.npy')
        npy_label_file = os.path.join(DATA_FILE_LOCATION, LABEL_FILE + '.npy')
        if os.path.isfile(npy_feature_file) and os.path.isfile(npy_label_file):
            X = np.load(npy_feature_file)
            y = np.load(npy_label_file)
            return X, y

        df = pd.read_csv(os.path.join(DATA_FILE_LOCATION, EXPORT_FILE))
        total_size = len(df)
        music_size = len(df[df['type'] == 1])
        speech_size = len(df[df['type'] == 0])
        print('%d music + %d speech = %d total' % (music_size, speech_size, total_size))
        X = np.zeros((total_size, MEL_VECTOR_SIZE))
        # 1 as size because we will have a 0 or 1 in this (boolean)
        y = np.zeros((total_size, 1))
        for i, (filepath, ftype) in enumerate(zip(df['filepath'], df['type'])):
            features = np.load(filepath)
            X[i] = features
            y[i] = ftype
        np.save(os.path.join(DATA_FILE_LOCATION, FEATURE_FILE), X)
        np.save(os.path.join(DATA_FILE_LOCATION, LABEL_FILE), y)
        return X, y

    def split_data(self, X, y, test_pct=0.1, valid_pct=0.1):
        random_state = 36
        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=test_pct, random_state=random_state)
        X_train, X_valid, y_train, y_valid = train_test_split(
            X_train, y_train, test_size=valid_pct, random_state=random_state)
        return {
            'X_train': X_train,
            'X_test': X_test,
            'X_valid': X_valid,
            'y_train': y_train,
            'y_test': y_test,
            'y_valid': y_valid
        }

    @staticmethod
    def create_ann_model():
        model = models.Sequential()
        model.add(layers.Dense(128, input_dim=MEL_VECTOR_SIZE, activation='relu'))
        model.add(layers.Dropout(0.3))
        model.add(layers.Dense(256, activation='relu'))
        model.add(layers.Dropout(0.3))
        model.add(layers.Dense(128, activation='relu'))
        model.add(layers.Dropout(0.3))
        model.add(layers.Dense(64, activation='relu'))
        model.add(layers.Dropout(0.3))
        model.add(layers.Dense(1, activation='sigmoid'))
        model.compile(loss='binary_crossentropy', metrics=['accuracy'], optimizer='adam')
        model.summary()
        return model

    def start_training(self, X, y, batch_size=32, epochs=10):
        data = self.split_data(X, y)
        model = DataTrainer.create_ann_model()

        early_stopping = callbacks.EarlyStopping(mode='min', patience=5, restore_best_weights=True)
        model.fit(data['X_train'], data['y_train'], epochs=epochs, batch_size=batch_size,
                  validation_data=(data['X_valid'], data['y_valid']), callbacks=[early_stopping])

        # save the model to a file
        save_file = os.path.join(DATA_FILE_LOCATION, H5_FILE_NAME)
        model.save(save_file)

        # Calculate loss
        print('Fit done... continuing to calculate loss...')
        loss, accuracy = model.evaluate(data['X_test'], data['y_test'], verbose=0)
        print('Loss: %.4f, Accuracy: %.2f %%' % (loss, accuracy * 100))


if __name__ == '__main__':
    dt = DataTrainer()
    print('Loading data...')
    Xpart, ypart = dt.load_data()
    print('Start training...')
    dt.start_training(Xpart, ypart, 32, 100)
