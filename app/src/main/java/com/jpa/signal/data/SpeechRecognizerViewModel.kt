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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SpeechRecognizerViewModel: ViewModel() {

    private var speechRecognizer: SpeechRecognizer? = null

    private val _textState = MutableStateFlow("")
    val textState: StateFlow<String> = _textState.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.S)
    fun startListening(context: Context) {
        if (!SpeechRecognizer.isOnDeviceRecognitionAvailable(context)){
            _textState.value = "El dispositivo no soporta reconocimiento en voz a texto"
            return
        }
    }

}