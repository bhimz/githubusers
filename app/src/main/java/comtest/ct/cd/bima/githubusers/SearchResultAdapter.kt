package comtest.ct.cd.bima.githubusers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import comtest.ct.cd.bima.githubusers.domain.User
import kotlinx.android.synthetic.main.view_user_item.view.*

class SearchResultAdapter : RecyclerView.Adapter<SearchResultViewHolder>() {

    var result = listOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = result.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_user_item, parent, false)
            .let { SearchResultViewHolder(it) }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(result[position])
    }
}

class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(user: User) {
        with(itemView) {
            usernameText.text = user.name
        }
    }
}