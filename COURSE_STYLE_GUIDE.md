# Course Style Guide (for AI assistants)

Reference for any AI writing code in `DogApp`. Goal: **use only what was taught in class** (Univalle – Mobile Apps, Ing. Walter Medina) and **write it the way the professor writes it**. When in doubt, copy the patterns from `clase8_univalle` / `clase9_univalle` (the inventory app), which is the closest example to this project.

Sources: `docs/clase1.pdf` … `docs/clase10.pdf`, `docs/HU_miniproyectoDogApp_Pro1.pdf`, and the repositories in `docs/class_repositories.md`.

> Project rules in `AGENTS.md` (Data Binding, `COLOR_PALETTE.md`, README sync) still apply and take priority over this guide.

---

## 1. Topics seen in class (allowed toolbox)

| Class | Topics |
|-------|--------|
| 1 | Kotlin basics: `val`/`var`, `lateinit`, nullability `?`, `const`, `when`, lists/`for`, classes, data classes, lambdas |
| 2–3 | Activity lifecycle, `Toast`, `Snackbar`, `TextView`, `EditText` (password/number/multiline), `AutoCompleteTextView`, `Button`, `ImageButton`, `RadioGroup`, `CheckBox`, `ToggleButton`, `Switch`, `FloatingActionButton` |
| 4 | Widgets: `ImageView`, `DatePicker`, `TimePicker`, `NumberPicker`, `ProgressBar` (circular/horizontal), `SeekBar`, `RatingBar`, `WebView` / `Intent` |
| 5 | Layouts: `LinearLayout`, `ConstraintLayout` (+ `Guideline`), `GridLayout` |
| 6 | `Toolbar` + menu, `Navigation Drawer`, Tabs with `ViewPager2`, `BottomNavigationView` |
| 7 | `Fragment`, Navigation Component (`nav_graph.xml`, `findNavController()`), passing data with `Bundle`, standard / custom `AlertDialog`, `DatePickerDialog`, `TimePickerDialog`, `CardView` |
| 8 | `RecyclerView` (Adapter + ViewHolder), runtime permissions, notifications, user preferences |
| 9 | MVVM + Repository, `LiveData`, `ViewModel`, Room (SQLite), coroutines |
| 10 | Debugging (Logcat, breakpoints), REST with Retrofit + Gson, images with Glide |

### Required by the DogApp HU but NOT seen in class
- **Biometric login** (`androidx.biometric`) and **Lottie** animation (HU 1). Implement them in the simplest possible way, isolated in their own fragment, following the same Fragment/Data Binding style.

### Do NOT introduce (not seen in class)
Jetpack Compose, Hilt/Dagger/Koin, Flow/StateFlow (use `LiveData`), RxJava, Safe Args, Paging, DataStore, WorkManager, Ktor/OkHttp interceptors, Coil/Picasso (use Glide), Moshi/kotlinx.serialization (use Gson), `ListAdapter`/`DiffUtil`, `findViewById`, plain View Binding without `<layout>`, sealed `UiState` / `Result` wrappers, use cases / clean-architecture layers beyond Repository.

---

## 2. Architecture (MVVM + Repository)

Package layout used in class and already in this repo:

```
com.example.dogapp
├── data/            # Room: XxxDB (RoomDatabase) + XxxDao
├── model/           # data classes: Room @Entity and API responses (XxxResponse)
├── repository/      # XxxRepository: talks to Dao and ApiService
├── utils/           # Constants object (DB name, BASE_URL, endpoints)
├── view/
│   ├── MainActivity.kt   # only hosts the NavHostFragment
│   ├── adapter/          # RecyclerView adapters
│   ├── fragment/         # one Fragment per screen
│   └── viewholder/       # RecyclerView ViewHolders
├── viewmodel/       # XxxViewModel (AndroidViewModel)
└── webservice/      # ApiService, ApiUtils, RetrofitClient
```

Flow: **Fragment → ViewModel → Repository → Dao / ApiService**. The Fragment never touches the Dao or Retrofit directly.

---

## 3. Professor's coding style

### 3.1 Activity
Single Activity that only hosts navigation:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```
When an Activity needs views, it uses `DataBindingUtil`:
```kotlin
lateinit var binding: ActivityMainBinding
binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
```

### 3.2 Fragment (canonical template)
- `private lateinit var binding` (not the `_binding` nullable pattern).
- `inflate(inflater)` + `binding.lifecycleOwner = this` in `onCreateView`.
- ViewModel via `by viewModels()`.
- `onViewCreated` only calls small private functions: `controladores()` (listeners) and `observerViewModel()` (LiveData observers).
- One small private function per action.

```kotlin
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores()
        observerViewModel()
    }

    private fun controladores() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newAppointmentFragment)
        }
    }

    private fun observerViewModel() {
        observerListAppointment()
        observerProgress()
    }

    private fun observerListAppointment() {
        appointmentViewModel.getListAppointment()
        appointmentViewModel.listAppointment.observe(viewLifecycleOwner) { list ->
            val recycler = binding.recyclerview
            recycler.layoutManager = LinearLayoutManager(context)
            val adapter = AppointmentAdapter(list, findNavController())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun observerProgress() {
        appointmentViewModel.progresState.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = status
        }
    }
}
```

### 3.3 Form validation (enable button only when all fields are filled)
```kotlin
private fun validarDatos() {
    val listEditText = listOf(binding.etName, binding.etBreed, binding.etOwner)
    for (editText in listEditText) {
        editText.addTextChangedListener {
            val isListFull = listEditText.all { it.text.isNotEmpty() }
            binding.btnSave.isEnabled = isListFull
        }
    }
}
```

### 3.4 Navigation and passing data
- All screens are Fragments declared in `res/navigation/nav_graph.xml`; action ids follow `action_<from>_to_<to>`.
- Navigate with `findNavController().navigate(R.id.action_x_to_y)`; go back with `findNavController().popBackStack()`.
- To return to Home clearing the stack, use `app:popUpTo` + `app:popUpToInclusive="true"` on the action.
- Pass data with a `Bundle` + `putSerializable` (models implement `Serializable`); read it with `arguments`:

```kotlin
val bundle = Bundle()
bundle.putSerializable("clave", appointment)
navController.navigate(R.id.action_homeFragment_to_detailFragment, bundle)

// destination
val receivedBundle = arguments
receivedAppointment = receivedBundle?.getSerializable("clave") as Appointment
```

### 3.5 RecyclerView
Adapter receives a `MutableList` and the `NavController`; the ViewHolder keeps the binding and exposes `setItemXxx()`.

```kotlin
class AppointmentAdapter(
    private val listAppointment: MutableList<Appointment>,
    private val navController: NavController
) : RecyclerView.Adapter<AppointmentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding, navController)
    }
    override fun getItemCount(): Int = listAppointment.size
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.setItemAppointment(listAppointment[position])
    }
}

class AppointmentViewHolder(binding: ItemAppointmentBinding, navController: NavController) :
    RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding
    val navController = navController

    fun setItemAppointment(appointment: Appointment) {
        bindingItem.tvName.text = appointment.petName
        bindingItem.cvAppointment.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", appointment)
            navController.navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }
}
```

### 3.6 ViewModel
- Extends `AndroidViewModel(application)`; creates the Repository with the application context.
- Private `MutableLiveData` + public `LiveData` (backing property `_name` / `name`).
- A `_progresState` LiveData toggled around each call.
- Every call: `viewModelScope.launch { try { ... } catch (e: Exception) { ... } }`.

```kotlin
class AppointmentViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val appointmentRepository = AppointmentRepository(context)

    private val _listAppointment = MutableLiveData<MutableList<Appointment>>()
    val listAppointment: LiveData<MutableList<Appointment>> get() = _listAppointment

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    fun getListAppointment() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listAppointment.value = appointmentRepository.getListAppointment()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }
}
```

### 3.7 Repository
Takes `Context`, builds the Dao and ApiService itself, wraps every call in `withContext(Dispatchers.IO)`.

```kotlin
class AppointmentRepository(val context: Context) {
    private var appointmentDao: AppointmentDao = AppointmentDB.getDatabase(context).appointmentDao()
    private var apiService: ApiService = ApiUtils.getApiService()

    suspend fun saveAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) { appointmentDao.saveAppointment(appointment) }
    }

    suspend fun getListAppointment(): MutableList<Appointment> {
        return withContext(Dispatchers.IO) { appointmentDao.getListAppointment() }
    }

    suspend fun getBreeds(): BreedsResponse? {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getBreeds()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
```

### 3.8 Room
```kotlin
@Entity
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petName: String,
    val breed: String,
    val ownerName: String,
    val phone: String,
    val symptom: String
) : Serializable

@Dao
interface AppointmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppointment(appointment: Appointment)

    @Query("SELECT * FROM Appointment")
    suspend fun getListAppointment(): MutableList<Appointment>

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    @Update
    suspend fun updateAppointment(appointment: Appointment)
}

@Database(entities = [Appointment::class], version = 1)
abstract class AppointmentDB : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
    companion object {
        fun getDatabase(context: Context): AppointmentDB {
            return Room.databaseBuilder(
                context.applicationContext,
                AppointmentDB::class.java,
                NAME_BD
            ).build()
        }
    }
}
```
- Dao functions are `suspend` and return `MutableList`.
- Room compiler via **KSP**.

### 3.9 Retrofit (webservice package)
```kotlin
object Constants {
    const val NAME_BD: String = "app_data.db"
    const val BASE_URL = "https://dog.ceo/api/"
    const val END_POINT_BREEDS = "breeds/list/all"
}

interface ApiService {
    @GET(END_POINT_BREEDS)
    suspend fun getBreeds(): BreedsResponse
}

object RetrofitClient {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

class ApiUtils {
    companion object {
        fun getApiService(): ApiService {
            return RetrofitClient.getRetrofit().create(ApiService::class.java)
        }
    }
}
```
- Response models use `@SerializedName` on every field and are named `XxxResponse` / `XxxModelResponse`.
- Images are loaded with Glide: `Glide.with(binding.root.context).load(url).into(binding.ivImage)`.

### 3.10 Dialogs
Helpers in a class with a `companion object` function that builds the `AlertDialog`; custom layouts inflated with their Data Binding class:

```kotlin
class DialogoPersonalizado {
    companion object {
        fun showDialogPersonalizado(context: Context) {
            val binding = DialogoPersonalizadoBinding.inflate(LayoutInflater.from(context))
            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.setCancelable(false)
            alertDialog.setView(binding.root)
            binding.btnAceptar.setOnClickListener { alertDialog.dismiss() }
            alertDialog.show()
        }
    }
}
```

### 3.11 User feedback
- Short messages: `Toast.makeText(context, "...", Toast.LENGTH_SHORT).show()`.
- Debug logs: `Log.d("TAG", value.toString())`.

---

## 4. XML style

- **Every layout used from Kotlin has a `<layout>` root** (Data Binding), with an empty or used `<data>` block. Exception: `activity_main.xml` when it only hosts the `NavHostFragment`.
- Root container is almost always `ConstraintLayout`; use `Guideline` for proportional layouts.
- Single-activity host:
  ```xml
  <androidx.fragment.app.FragmentContainerView
      android:id="@+id/navigationContainer"
      android:name="androidx.navigation.fragment.NavHostFragment"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:defaultNavHost="true"
      app:navGraph="@navigation/nav_graph" />
  ```
- List items: `CardView` (with `cardCornerRadius`, `elevation`, `?selectableItemBackground`) wrapping a `ConstraintLayout`. File name `item_<thing>.xml`.
- Lists: `RecyclerView` with `tools:listitem="@layout/item_..."`.
- Loading: centered `ProgressBar` with `android:visibility="gone"`, toggled from `progresState`.
- Add button: `FloatingActionButton`.
- Include `tools:context=".view.fragment.XxxFragment"`.
- Texts in `res/values/strings.xml` (`@string/...`); colors from `@color/...` (see `COLOR_PALETTE.md`, never hardcoded hex).

### View id prefixes
| Prefix | View |
|--------|------|
| `tv` | TextView |
| `et` | EditText |
| `btn` | Button |
| `fb` / `fab` | FloatingActionButton |
| `iv` | ImageView |
| `cv` | CardView |
| `rv` / `recyclerview` | RecyclerView |
| `cb` | CheckBox |
| `rb` / `rg` | RadioButton / RadioGroup |
| `sw` | Switch |
| `at` | AutoCompleteTextView |
| `progress` | ProgressBar |

### File naming
- Layouts: `activity_<name>.xml`, `fragment_<name>.xml`, `item_<name>.xml`, `dialogo_<name>.xml`.
- Kotlin: `XxxFragment`, `XxxAdapter`, `XxxViewHolder`, `XxxViewModel`, `XxxRepository`, `XxxDao`, `XxxDB`.

---

## 5. Gradle

- `buildFeatures { dataBinding = true }`.
- Plugins seen in class: `com.android.application`, `org.jetbrains.kotlin.android`, `com.google.devtools.ksp`, `kotlin-kapt`.
- Libraries seen in class: navigation-fragment-ktx / navigation-ui-ktx, cardview, recyclerview, kotlinx-coroutines-android, lifecycle-viewmodel-ktx, lifecycle-livedata-ktx, activity-ktx, fragment-ktx, room-runtime / room-ktx / room-compiler (KSP), retrofit + converter-gson, glide.
- Testing seen in class: JUnit 4, Mockito (`mockito-core`, `mockito-inline`), `kotlinx-coroutines-test`, `core-testing` (`InstantTaskExecutorRule`), Jacoco.
- In **this** repo, declare versions through `gradle/libs.versions.toml` (the class repos hardcode them; keep the catalog here).

---

## 6. Checklist before finishing a change

- [ ] Only uses topics from section 1 (or the HU-required biometric/Lottie).
- [ ] Layout has `<layout>` root and is accessed through its generated binding class.
- [ ] Fragment follows template 3.2 (`lateinit binding`, `lifecycleOwner`, `controladores()`, `observerViewModel()`).
- [ ] Data flows Fragment → ViewModel (LiveData) → Repository → Dao / ApiService.
- [ ] Navigation through `nav_graph.xml` actions; data via `Bundle` + `Serializable`.
- [ ] Strings in `strings.xml`, colors from `colors.xml` / `COLOR_PALETTE.md`.
- [ ] `README.md` updated if structure, dependencies, or HU status changed.
