package comtest.ct.cd.bima.githubusers

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import comtest.ct.cd.bima.githubusers.domain.SortType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(R.layout.activity_main), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    private lateinit var job: Job

    private var searchJob: Job? = null

    private val viewModel: UserListViewModel by inject()

    private val resultAdapter = SearchResultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()

        with(resultListView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = resultAdapter
        }

        searchView.setOnQueryTextListener {
            searchJob?.cancel()
            searchJob = launch {
                delay(1000)
                viewModel.updateSearch(it ?: "", SortType.ASC)
            }
        }

        viewModel.users.observe(this) {
            it ?: return@observe
            resultAdapter.result = it
        }
    }

    private fun SearchView.setOnQueryTextListener(block: (String?) -> Unit) {
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                block.invoke(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean = false
        }
        setOnQueryTextListener(listener)
    }
}