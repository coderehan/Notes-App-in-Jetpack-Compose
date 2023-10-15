package com.example.notesappinjetpackcompose.features.notes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notesappinjetpackcompose.R
import com.example.notesappinjetpackcompose.data.model.NotesRequest
import com.example.notesappinjetpackcompose.data.model.NotesResponse
import com.example.notesappinjetpackcompose.features.notes.ui.viewmodels.NotesViewModel
import com.example.notesappinjetpackcompose.features.notes.ui.viewmodels.events.NotesEvent
import com.example.notesappinjetpackcompose.ui.theme.BackgroundColor
import com.example.notesappinjetpackcompose.ui.theme.ContentColor
import com.example.notesappinjetpackcompose.ui.theme.Red
import com.example.notesappinjetpackcompose.utils.ApiState
import com.example.notesappinjetpackcompose.utils.showToast
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale


// This composable function is parent composable function.
// Inside this, we will keep child composable functions that we created separately.
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(notesViewModel: NotesViewModel = hiltViewModel()) {

    val notesResponse = notesViewModel.getNoteEventStateFlow.value

    // remember keyword is used to retain value in the state
    var search by remember {
        mutableStateOf("")
    }

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var isAddDialog by remember {
        mutableStateOf(false)       // By default, it will be false
    }

    var isUpdateDialog by remember {
        mutableStateOf(false)       // By default, it will be false
    }

    var id by remember {
        mutableStateOf(0)           // By default, it will be 0
    }

    var isLoading by remember {
        mutableStateOf(false)       // By default, it will be false
    }

    val context = LocalContext.current

    // for get notes
    LaunchedEffect(key1 = true){
        notesViewModel.onEvent(NotesEvent.GetNoteEvent)
    }

    // for add notes
    LaunchedEffect(key1 = true){
        notesViewModel.addNoteEventSharedFlow.collectLatest {
           isLoading = when(it){
               is ApiState.Success -> {
                   notesViewModel.onEvent(NotesEvent.GetNoteEvent)
                   isAddDialog = false
                   context.showToast("Notes Added Successfully")
                   false
               }
                is ApiState.Failure -> {
                    context.showToast(it.msg)
                    false
                }
                ApiState.Loading -> {
                    true
                }
            }
        }
    }

    // for delete notes
    LaunchedEffect(key1 = true){
        notesViewModel.deleteNoteEventSharedFlow.collectLatest {
            isLoading = when(it){
                is ApiState.Success -> {
                    notesViewModel.onEvent(NotesEvent.GetNoteEvent)
                    context.showToast("Notes Deleted Successfully")
                    false
                }
                is ApiState.Failure -> {
                    context.showToast(it.msg)
                    false
                }
                ApiState.Loading -> {
                    true
                }
            }
        }
    }

    // for update notes
    LaunchedEffect(key1 = true){
        notesViewModel.updateNoteEventSharedFlow.collectLatest {
            isLoading = when(it){
                is ApiState.Success -> {
                    notesViewModel.onEvent(NotesEvent.GetNoteEvent)
                    isUpdateDialog = false
                    context.showToast("Notes Updated Successfully")
                    false
                }
                is ApiState.Failure -> {
                    context.showToast(it.msg)
                    false
                }
                ApiState.Loading -> {
                    true
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = {
                    // We will open alert dialog box here by making it true
                    isAddDialog = true
                },
                containerColor = Color.Red,
                shape = CircleShape,
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)
            ) {
                // Icon comes under floating action button
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }) {       // Scaffold start

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(BackgroundColor)
        ) { // Column start

            // First we will show AppSearchBar
            AppSearchBar(
                search = search,
                onValueChange = {
                    search = it
                },
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 50.dp)
            )

            // Loading case
            if (notesResponse.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Center
                ) {
                    CircularProgressIndicator(color = Red)
                }
            }

            // Successful data case
            if (notesResponse.data.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(15.dp)
                ) {

                    // Search functionality
                    val filteredNotes: List<NotesResponse> = if (search.isEmpty())
                        notesResponse.data
                    else {
                        val resultData: ArrayList<NotesResponse> = arrayListOf()
                        for (temp in notesResponse.data) {
                            if (temp.title.lowercase(Locale.getDefault()).contains(
                                    search.lowercase(Locale.getDefault())
                                ) || temp.description.lowercase(Locale.getDefault()).contains(
                                    search.lowercase(Locale.getDefault())
                                )
                            ) {
                                resultData.add(temp)
                            }
                        }
                        resultData
                    }


                    items(filteredNotes, key = {   // key provides unique key for every items
                        it.id
                    }) {
                        // Child composable view which we created for LazyVerticalGrid parent composable view
                        DisplayNotes(notesResponse = it, onUpdate = {
                            isUpdateDialog = true
                            title = it.title
                            description = it.description
                            id = it.id
                        }) {
                            // Delete notes
                            notesViewModel.onEvent(NotesEvent.DeleteNoteEvent(it.id))
                        }
                    }
                }
            }

        }       // Column end
    }       // Scaffold end

    if(isLoading) LoadingDialog()

    if(isUpdateDialog)
        ShowDialogBox(
            title = title,
            description = description,
            onTitleChange = {
                title = it
            },
            onDescriptionChange = {
                description = it
            },
            // On save button click
            onClick = {
                notesViewModel.onEvent(
                    NotesEvent.UpdateNoteEvent(
                        id,
                        NotesRequest(title, description)
                    )
                )
            },
            onClose = {
                isUpdateDialog = it
            }
        )

    // If isAddDialog is true, we will show alert dialog box
    if (isAddDialog)
        ShowDialogBox(
            title = title,
            description = description,
            onTitleChange = {
                title = it
            },
            onDescriptionChange = {
                description = it
            },
            // On save button click
            onClick = {
                notesViewModel.onEvent(
                    NotesEvent.AddNoteEvent(
                        NotesRequest(title, description)
                    )
                )
            },
            onClose = {
                isAddDialog = it
            }
        )

}

// This composable function is child composable function.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    search: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = search,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = ContentColor,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        // Staring icon in search bar
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                tint = Color.Red
            )
        },
        // Ending icon in search bar
        trailingIcon = {
            // We will show this close icon only when user enters something in search bar
            if (search.isNotEmpty()) {
                IconButton(onClick = {
                    // Whatever the text was there onValueChange will be cleared on this button click and "" denotes empty
                    onValueChange("")
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = Color.Red
                    )

                }
            }
        },
        placeholder = {
            Text(
                text = "Search Notes...",
                style = TextStyle(
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        }
    )
}


// This composable function is child composable function.
@Composable
fun DisplayNotes(
    notesResponse: NotesResponse,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,      // We have delete icon also while displaying notes. So that is why we created onDelete here.
    onUpdate: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onUpdate()
            }
            .background(
                color = ContentColor,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)     // 15dp margin top bottom left right
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {     // Row start
                Text(
                    text = notesResponse.title,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.W600,
                        fontSize = 22.sp
                    ),
                    modifier = Modifier
                        .weight(0.7f)
                )
                IconButton(
                    onClick = {
                        onDelete()
                    },
                    modifier = Modifier
                        .weight(0.3f)
                        .align(CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = Color.Red
                    )
                }
            }       // Row end

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = notesResponse.description,
                style = TextStyle(
                    color = Color.Black.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = notesResponse.updated_at.split("T")[0],  // Date looks in api like this 2023-03-25T06:58:48.435450z. So here T divides into two indexes. Before T, index would be 0 which we will show in UI and after T, index would be 1 which we don't want to show in UI.
                style = TextStyle(
                    color = Color.Black.copy(alpha = 0.3f),
                    fontSize = 10.sp
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

        }
    }
}

// This composable function is child composable function.
@Composable
fun ShowDialogBox(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onClick: () -> Unit,
    onClose: (Boolean) -> Unit
) {

    // When we open screen, this will apply focusable to our first edit text.
    val focusRequester = FocusRequester()
    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = {},
        // Here we will do save button functionality
        confirmButton = {
            Button(
                onClick = {
                    onClick()
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(15.dp)   // Space around the content in button
            ) {
                Text(text = "Save")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = BackgroundColor,
        // We have two things in alert dialog box. (1) Title (2) Text.
        // In title attribute, we have to show closing icon to close the alert dialog box.
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = TopEnd       // We will show closing icon in top end side
            ) {
                IconButton(onClick = {
                    // Here we will close alert dialog box
                    onClose(false)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = Color.Red
                    )
                }
            }
        },

        // In text attribute, we have to show the actual content that we want to show.
        // Here we will show two edittext that we have created already.
        text = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                // This is for title
                AppTextFields(
                    text = title,
                    placeholder = stringResource(R.string.title),
                    onValueChange = onTitleChange,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                )

                Spacer(modifier = Modifier.height(15.dp))

                // This is for description
                AppTextFields(
                    text = description,
                    placeholder = stringResource(R.string.description),
                    onValueChange = onDescriptionChange,
                    modifier = Modifier.height(300.dp)
                )
            }
        }

    )
}

// This composable function is for edittext
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextFields(
    text: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {

    TextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Black.copy(alpha = 0.4f)
            )
        },

        )
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        CircularProgressIndicator(color = Red)
    }
}
