//package com.example.task
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//
//data class Todo(
//    val userId: Int,
//    val id: Int,
//    val title: String,
//    val completed: Boolean
//)
//
//const val BASE_URL = "https://api.transport-x.ir/api/general/v1/"
//
//interface APIService {
//    @GET("echo")
//    suspend fun getTodos(): List<Todo>
//
//    companion object {
//        private var apiService: APIService? = null
//
//        fun getInstance(): APIService {
//            if (apiService == null) {
//                apiService = Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build().create(APIService::class.java)
//            }
//            return apiService!!
//        }
//    }
//}
//
//class TodoViewModel : ViewModel() {
//    private val _todoList = mutableStateListOf<Todo>()
//    var errorMessage: String by mutableStateOf("")
//    val todoList: List<Todo>
//        get() = _todoList
//
//    fun getTodoList() {
//        viewModelScope.launch {
//            try {
//                _todoList.clear()
//                val apiService = APIService.getInstance()
//                _todoList.addAll(apiService.getTodos())
//            } catch (e: Exception) {
//                errorMessage = e.message.toString()
//            }
//        }
//    }
//}
//
//@Composable
//fun TodoView(vm: TodoViewModel) {
//    val context = LocalContext.current
//
//    val todoViewModel = TodoViewModel()
//    LaunchedEffect(Unit) {
//        todoViewModel.getTodoList()
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text("Todos")
//                }
//            )
//        },
//        content = { padding ->
//            if (vm.errorMessage.isEmpty()) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    LazyColumn(modifier = Modifier.fillMaxHeight()) {
//                        items(vm.todoList) { todo ->
//                            Column {
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(16.dp),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Box(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(end = 16.dp)
//                                    ) {
//                                        Text(
//                                            text = todo.title,
//                                            maxLines = 1,
//                                            overflow = TextOverflow.Ellipsis
//                                        )
//                                    }
//                                    Checkbox(
//                                        checked = todo.completed,
//                                        onCheckedChange = null
//                                    )
//                                }
//                                Divider()
//                            }
//                        }
//                    }
//                }
//            } else {
//                Text(text = vm.errorMessage)
//            }
//        }
//    )
//}