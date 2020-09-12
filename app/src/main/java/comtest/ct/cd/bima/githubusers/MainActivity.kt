package comtest.ct.cd.bima.githubusers

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import comtest.ct.cd.bima.githubusers.domain.AppSettings
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

    private val viewModel by inject<UserListViewModel>()

    private val appSettings by inject<AppSettings>()

    private val resultAdapter = SearchResultAdapter()

    var isLoadingMore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()
        pageTitleText.text = getString(R.string.github_users)
        pageSubTitleText.text = getString(R.string.ctcd)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val manager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    when (resultAdapter.getItemViewType(position)) {
                        SearchResultAdapter.TYPE_FOOTER -> 2
                        else -> 1
                    }
            }
        }
        with(resultListView) {
            layoutManager = manager
            adapter = resultAdapter
        }

        searchView.setOnQueryTextListener {
            searchJob?.cancel()
            searchJob = launch {
                delay(1000)
                viewModel.updateSearch(it ?: "", SortType.ASC)
            }
        }
        searchView.setOnCloseListener {
            viewModel.clearResults()
            true
        }

        viewModel.users.observe(this) {
            it ?: return@observe
            resultAdapter.result = it
        }

        resultListView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (resultAdapter.result.size < appSettings.rowPerPage) return
                    val lastIndex = manager.findLastVisibleItemPosition()
                    if (!isLoadingMore && lastIndex >= resultAdapter.result.size - 1) {
                        viewModel.nextPage()
                    }
                }
            }
        )

        viewModel.uiState.observe(this) {
            when (it) {
                is UserListState.Loading -> {
                    loadingView.isVisible = true
                    resultListView.isVisible = false
                }
                is UserListState.LoadNext -> {
                    isLoadingMore = true
                    resultAdapter.isLoadingNext = true
                }
                is UserListState.Ready, is UserListState.Error -> {
                    loadingView.isVisible = false
                    resultListView.isVisible = true
                    resultAdapter.isLoadingNext = false
                    isLoadingMore = false
                    if (it is UserListState.Error) {
                        it.error.consumeOnce { e ->
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.app_menu, menu)
        val sortAsc = menu?.findItem(R.id.actionSortAsc)
        val sortDesc = menu?.findItem(R.id.actionSortDesc)
        if (viewModel.sortType == SortType.ASC) {
            sortAsc?.isChecked = true
        } else {
            sortDesc?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.isChecked) return super.onOptionsItemSelected(item)
        item.isChecked = true
        when (item.itemId) {
            R.id.actionSortAsc -> {
                viewModel.sortBy(SortType.ASC)
                return true
            }
            R.id.actionSortDesc -> {
                viewModel.sortBy(SortType.DESC)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}