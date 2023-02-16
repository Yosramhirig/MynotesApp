package com.example.mynotesapp.feature_note.domain.use_case

data class NoteUseCases(
    val GetNotesUseCase : GetNotesUseCase,
    val DeleteNotesUseCase : DeleteNotesUseCase,
    val AddNoteUseCase : AddNoteUseCase,
    val getNote : GetNote
)
