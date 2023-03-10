package com.example.mynotesapp.feature_note.presentation.add_edit_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotesapp.feature_note.domain.model.InvalidNoteException
import com.example.mynotesapp.feature_note.domain.model.Note
import com.example.mynotesapp.feature_note.domain.use_case.GetNote
import com.example.mynotesapp.feature_note.domain.use_case.NoteUseCases
import com.example.mynotesapp.feature_note.presentation.notes_screen.util.NoteTextFieldState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _noteTitle = mutableStateOf(
        NoteTextFieldState(
            hint = "Title"
        )
    )
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Note Body"))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf<Int>(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentNoteId: Int = -1

    init {
        if (savedStateHandle.get<Int?>("noteId") != null) {

            savedStateHandle.get<Int?>("noteId")?.let { noteId ->
                if (noteId != -1) {
                    viewModelScope.launch {
                        noteUseCases.getNote.getNote(noteId)?.also { note ->
                            currentNoteId = note.id!!
                            _noteTitle.value = noteTitle.value.copy(
                                text = note.title,
                                isHintVisible = false
                            )
                            _noteContent.value = noteContent.value.copy(
                                text = note.content,
                                isHintVisible = false
                            )
                            _noteColor.value = note.color
                        }

                    }
                }
            }
        }
    }


    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnterTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnterContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        val note = if (currentNoteId == -1) Note(
                            title = noteTitle.value.text,
                            content = noteContent.value.text,
                            date = System.currentTimeMillis(),
                            color = _noteColor.value,
                            ) else Note(
                            title = noteTitle.value.text,
                            content = noteContent.value.text,
                            date = System.currentTimeMillis(),
                            color = _noteColor.value,
                            id = currentNoteId
                            )
                        noteUseCases.AddNoteUseCase(
                            note

                        )
                        _eventFlow.emit(UiEvent.SaveNote)

                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.showSnackbar(
                                message = e.message ?: "Couldnt save note"
                            )
                        )
                    }
                }
            }

        }
    }


    sealed class UiEvent {
        data class showSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }


}