package comtest.ct.cd.bima.githubusers.util

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class RecyclerViewMatcher(
    private val id: Int,
    private val position: Int,
    private val targetId: Int
) : TypeSafeMatcher<View>() {
    private var resources: Resources? = null

    override fun describeTo(description: Description?) {
        val resourceIdName = resources?.let {
            runCatching { it.getResourceName(targetId) }.getOrElse { id.toString() }
        }
        description?.appendText("with id $resourceIdName")
    }

    override fun matchesSafely(item: View?): Boolean {
        val view = item ?: return false
        resources = view.resources
        val recyclerView = view.rootView.findViewById<RecyclerView?>(id) ?: return false
        val childView = recyclerView.findViewHolderForAdapterPosition(position)?.itemView
        return recyclerView.id == id && childView?.findViewById<View>(targetId) == view
    }
}

class RecyclerViewMatcherBuilder(private val id: Int) {
    fun atPositionWithId(position: Int, viewId: Int) = RecyclerViewMatcher(id, position, viewId)
}

fun onRecyclerView(id: Int) = RecyclerViewMatcherBuilder(id)