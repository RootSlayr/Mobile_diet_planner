package com.example.assignment_1

import android.app.Application
import android.content.Context
import androidx.compose.foundation.clickable
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@Entity
data class Person(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val name: String,
    val address: String,
    val age: Int,
    val gender: String,
    val height: Int,
    val weight: Double
)

@Dao
interface PersonDAO {
    @Query("SELECT * FROM Person")
    fun getAllPersons(): Flow<List<Person>>

    @Insert
    suspend fun insertPerson(person: Person)

    @Update
    suspend fun updatePerson(person: Person)

    @Delete
    suspend fun deletePerson(person: Person)
}

@Database(entities = [Person::class], version = 1, exportSchema = false)
abstract class PersonDatabase : RoomDatabase() {
    abstract fun personDAO(): PersonDAO

    companion object {
        @Volatile
        private var INSTANCE: PersonDatabase? = null

        fun getDatabase(context: Context): PersonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PersonDatabase::class.java,
                    "person_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class PersonRepository(application: Application) {
    private var personDao: PersonDAO =
        PersonDatabase.getDatabase(application).personDAO()

    val allPersons: Flow<List<Person>> = personDao.getAllPersons()

    suspend fun insert(person: Person) {
        personDao.insertPerson(person)
    }

    suspend fun delete(person: Person) {
        personDao.deletePerson(person)
    }

    suspend fun update(person: Person) {
        personDao.updatePerson(person)
    }
}

class PersonViewModel(application: Application) : AndroidViewModel(application) {
    private val personRepository: PersonRepository

    init {
        personRepository = PersonRepository(application)
    }

    val allPersons: LiveData<List<Person>> = personRepository.allPersons.asLiveData()

    fun insertPerson(person: Person) = viewModelScope.launch(Dispatchers.IO) {
        personRepository.insert(person)
    }

    fun updatePerson(person: Person) = viewModelScope.launch(Dispatchers.IO) {
        personRepository.update(person)
    }

    fun deletePerson(person: Person) = viewModelScope.launch(Dispatchers.IO) {
        personRepository.delete(person)
    }
}

@Composable
fun PersonItem(person: Person, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(300.dp)
            .clickable { onEdit.invoke() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Name: ${person.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp // Set the font size to 20 sp
            )
            Text(text = "Address: ${person.address}", fontSize = 20.sp)
            Text(text = "Age: ${person.age}", fontSize = 20.sp)
            Text(text = "Gender: ${person.gender}", fontSize = 20.sp)
            Text(text = "Height: ${person.height} cm", fontSize = 20.sp)
            Text(text = "Weight: ${person.weight} kg", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController, personViewModel: PersonViewModel) {
    val persons by personViewModel.allPersons.observeAsState(emptyList())
    val selectedPerson = remember { mutableStateOf<Person?>(null) }
    val insertDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = {
            if (persons.isNotEmpty()) {
                selectedPerson.value = persons.first() // Display the first person's profile for editing
            } else {
                insertDialog.value = true// Provide default empty person
            }
        }){
            Text("Edit Profile")
        }
        LazyColumn {
            itemsIndexed(persons) { _, person ->
                PersonItem(
                    person = person,
                    onEdit = { selectedPerson.value = person },
                    onDelete = { personViewModel.deletePerson(person) }
                )
            }
        }
        BottomNavigationComponent(navController = navController)
    }

    if (insertDialog.value) {
        InsertPersonDialog(
            onDismiss = { insertDialog.value = false },
            onSave = { name, address, age, gender, height, weight ->
                personViewModel.insertPerson(
                    Person(
                        name = name,
                        address = address,
                        age = age,
                        gender = gender,
                        height = height,
                        weight = weight
                    )
                )
            }
        )
    }

    selectedPerson.value?.let { person ->
        EditPersonDialog(
            person = person,
            onDismiss = { selectedPerson.value = null },
            onSave = { updatedPerson ->
                personViewModel.updatePerson(updatedPerson)
                selectedPerson.value = null
            }
        )
    }
}

@Composable
fun InsertPersonDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Int, String, Int, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var age by remember { mutableStateOf(0) }
    var gender by remember { mutableStateOf("") }
    var height by remember { mutableStateOf(0) }
    var weight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        confirmButton = {
            Button(
                onClick = {
                    onSave(name, address, age, gender, height, weight.toDouble())
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = age.toString(),
                    onValueChange = { age = it.toIntOrNull() ?: 0 },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = height.toString(),
                    onValueChange = { height = it.toIntOrNull() ?: 0 },
                    label = { Text("Height (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = weight.toString(),
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
    )
}

@Composable
fun EditPersonDialog(
    person: Person,
    onDismiss: () -> Unit,
    onSave: (Person) -> Unit
) {
    var editedPerson by remember { mutableStateOf(person.copy()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        confirmButton = {
            Button(
                onClick = {
                    onSave(editedPerson)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = editedPerson.name,
                    onValueChange = { editedPerson = editedPerson.copy(name = it) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedPerson.address,
                    onValueChange = { editedPerson = editedPerson.copy(address = it) },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedPerson.age.toString(),
                    onValueChange = { editedPerson = editedPerson.copy(age = it.toIntOrNull() ?: 0) },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedPerson.gender,
                    onValueChange = { editedPerson = editedPerson.copy(gender = it) },
                    label = { Text("Gender") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedPerson.height.toString(),
                    onValueChange = { editedPerson = editedPerson.copy(height = it.toIntOrNull() ?: 0) },
                    label = { Text("Height (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedPerson.weight.toString(),
                    onValueChange = { editedPerson = editedPerson.copy(weight = it.toDoubleOrNull() ?: 0.0) },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
@Composable
fun BigProfileItem(person: Person, onSave: (Person) -> Unit) {
    var editedPerson by remember { mutableStateOf(person.copy()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = editedPerson.name,
                onValueChange = { editedPerson = editedPerson.copy(name = it) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = editedPerson.address,
                onValueChange = { editedPerson = editedPerson.copy(address = it) },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = editedPerson.age.toString(),
                onValueChange = { editedPerson = editedPerson.copy(age = it.toIntOrNull() ?: 0) },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = editedPerson.gender,
                onValueChange = { editedPerson = editedPerson.copy(gender = it) },
                label = { Text("Gender") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = editedPerson.height.toString(),
                onValueChange = { editedPerson = editedPerson.copy(height = it.toIntOrNull() ?: 0) },
                label = { Text("Height (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = editedPerson.weight.toString(),
                onValueChange = { editedPerson = editedPerson.copy(weight = it.toDoubleOrNull() ?: 0.0) },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onSave(editedPerson) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Profile")
            }
        }
    }
}


//---------------------------------------------------------------------------------------------
//@Entity
//data class Subject(
//    @PrimaryKey(autoGenerate = true)
//    val uid: Int = 0,
//    val name: String
//)
//
//@Dao
//interface SubjectDAO {
//    @Query("SELECT * FROM Subject")
//    fun getAllSubjects(): Flow<List<Subject>>
//    @Insert
//    suspend fun insertSubject(subject: Subject)
//    @Update
//    suspend fun updateSubject(subject: Subject)
//    @Delete
//    suspend fun deleteSubject(subject: Subject)
//}
//
//@Database(entities = [Subject::class], version = 1, exportSchema = false)
//abstract class SubjectDatabase : RoomDatabase() {
//    abstract fun subjectDAO(): SubjectDAO
//    companion object {
//        @Volatile
//        private var INSTANCE: SubjectDatabase? = null
//        fun getDatabase(context: Context): SubjectDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    SubjectDatabase::class.java,
//                    "subject_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
//
//class SubjectRepository (application: Application) {
//    private var subjectDao: SubjectDAO =
//        SubjectDatabase.getDatabase(application).subjectDAO()
//    val allSubjects: Flow<List<Subject>> = subjectDao.getAllSubjects()
//    suspend fun insert(subject: Subject) {
//        subjectDao.insertSubject(subject)
//    }
//    suspend fun delete(subject: Subject) {
//        subjectDao.deleteSubject(subject)
//    }
//    suspend fun update(subject: Subject) {
//        subjectDao.updateSubject(subject)
//    }
//}
//
//
//class SubjectViewModel(application: Application) : AndroidViewModel(application) {
//    private val cRepository: SubjectRepository
//    init{
//        cRepository = SubjectRepository(application)
//    }
//    val allSubjects: LiveData<List<Subject>> = cRepository.allSubjects.asLiveData()
//    fun insertSubject(subject: Subject) = viewModelScope.launch(Dispatchers.IO) {
//        cRepository.insert(subject)
//    }
//    fun updateSubject(subject: Subject) = viewModelScope.launch(Dispatchers.IO) {
//        cRepository.update(subject)
//    }
//    fun deleteSubject(subject: Subject) = viewModelScope.launch(Dispatchers.IO) {
//        cRepository.delete(subject)
//    }
//}
//
//@Composable
//fun SubjectItem(subject: Subject, onEdit: () -> Unit, onDelete: () -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(text = subject.name.toString(), modifier = Modifier.weight(1f))
//        IconButton(onClick = onEdit) {
//            Icon(Icons.Default.Edit, contentDescription = "Edit")
//        }
//        IconButton(onClick = onDelete) {
//            Icon(Icons.Default.Delete, contentDescription = "Delete")
//        }
//    }
//}
//
//@Composable
//fun MainScreen(subjectViewModel: SubjectViewModel) {
//    val subjects by subjectViewModel.allSubjects.observeAsState(emptyList())
//    val selectedSubject = remember { mutableStateOf<Subject?>(null) }
//    val insertDialog = remember { mutableStateOf(false) }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Button(onClick = { insertDialog.value = true }) {
//            Text("Add Subject")
//        }
//        LazyColumn {
//            itemsIndexed(subjects) { index, subject ->
//                SubjectItem(
//                    subject = subject,
//                    onEdit = { selectedSubject.value = subject },
//                    onDelete = { subjectViewModel.deleteSubject(subject) }
//                )
//                Divider(color = Color.Gray, thickness = 5.dp)
//            }
//        }
//    }
//    if (insertDialog.value) {
//        InsertSubjectDialog(
//            onDismiss = { insertDialog.value = false },
//            onSave = { subjectName ->
//                subjectViewModel.insertSubject(Subject(name = subjectName))
//            }
//        )
//    }
//    selectedSubject.value?.let { subject ->
//        EditSubjectDialog(
//            subject = subject,
//            onDismiss = { selectedSubject.value = null },
//            onSave = { updatedSubject ->
//                subjectViewModel.updateSubject(updatedSubject)
//                selectedSubject.value = null
//            }
//        )
//    }
//}
//@Composable
//fun InsertSubjectDialog(
//    onDismiss: () -> Unit,
//    onSave: (String) -> Unit
//) {
//    var subjectName by remember { mutableStateOf("") }
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Add Subject") },
//        confirmButton = {
//            Button(
//                onClick = {
//                    onSave(subjectName)
//                    onDismiss()
//                }
//            ) {
//                Text("Save")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        },
//        text = {
//            TextField(
//                value = subjectName,
//                onValueChange = { subjectName = it },
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    )
//}
//@Composable
//fun EditSubjectDialog(subject: Subject, onDismiss: () -> Unit, onSave: (Subject) -> Unit)
//{
//    var editedSubject by remember { mutableStateOf(subject) }
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Edit Subject") },
//        confirmButton = {
//            Button(
//                onClick = {
//                    onSave(editedSubject)
//                    onDismiss()
//                }
//            ) {
//                Text("Save")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        },
//        text = {
//            TextField(
//                value = editedSubject.name.toString(),
//                onValueChange = { editedSubject = editedSubject.copy(name = it) },
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    )
//}