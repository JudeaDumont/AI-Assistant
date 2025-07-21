package org.example.vosk.text.to.speech;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.IOException;

public class SpeechToText {

    public static void main(String[] args) throws IOException, LineUnavailableException {
        LibVosk.setLogLevel(LogLevel.DEBUG);
        Model model = new Model("models/vosk-model-en-us-0.22");

        Recognizer recognizer = new Recognizer(model, 16000.0f);
        AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, false);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Microphone not supported");
            System.exit(1);
        }

        TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        microphone.start();

        System.out.println("Start speaking...");

        byte[] buffer = new byte[4096];
        while (true) {
            int bytesRead = microphone.read(buffer, 0, buffer.length);
            if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                System.out.println("Result: " + recognizer.getResult());
            } else {
                System.out.println("Partial: " + recognizer.getPartialResult());
            }
        }
    }
}