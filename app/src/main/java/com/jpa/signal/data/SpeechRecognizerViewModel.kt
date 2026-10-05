package com.jpa.signal.data

import android.speech.SpeechRecognizer
import androidx.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.*
import java.util.Locale

//ViewModel para el reconocimiento de voz
class SpeechRecognizerViewModel: ViewModel() {

    //Variable para el reconocimiento de voz
    private var speechRecognizer: SpeechRecognizer? = null

    //Variables para el estado del reconocimiento de voz
    private val _textState = MutableStateFlow("")
    val textState: StateFlow<String> = _textState.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    //Función para iniciar el reconocimiento de voz
    @RequiresApi(Build.VERSION_CODES.S)
    fun startListening(context: Context) {
        if (!SpeechRecognizer.isOnDeviceRecognitionAvailable(context)){
            _textState.value = "El dispositivo no soporta reconocimiento en voz a texto"
            return
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply{
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    _isListening.value = true
                    _textState.value = "Escuchando..."

                }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    _isListening.value = false
                }
                override fun onError(error: Int) {
                    _isListening.value = false
                    _textState.value = "Error al reconocer la voz código: $error"
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        _textState.value = matches[0]
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }

        //Configuración del reconocimiento de voz
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        speechRecognizer?.startListening(intent)

        }
        //Función para detener el reconocimiento de voz
        fun stopListening() {
            speechRecognizer?.stopListening()
            _isListening.value = false
        }
        //Función para destruir el reconocimiento de voz
        override fun onCleared() {
            speechRecognizer?.destroy()
        }

}