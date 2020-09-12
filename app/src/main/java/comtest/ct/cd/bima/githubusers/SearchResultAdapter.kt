package comtest.ct.cd.bima.githubusers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import comtest.ct.cd.bima.githubusers.domain.User
import kotlinx.android.synthetic.main.view_user_item.view.*

class SearchResultAdapter : RecyclerView.Adapter<SearchResultViewHolder>() {

    var result = listOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isLoadingNext: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = result.size + if (isLoadingNext) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return when {
            isLoadingNext && position == result.size -> TYPE_FOOTER
            else -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder =
        when (viewType) {
            TYPE_ITEM -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_user_item, parent, false)
                    .let { SearchResultViewHolder.Item(it) }
            }
            else -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_footer_item, parent, false)
                    .let { SearchResultViewHolder.Footer(it) }
            }
        }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        if (holder is SearchResultViewHolder.Item) {
            holder.bind(result[position])
        }
    }

    companion object {
        const val TYPE_FOOTER = 0
        const val TYPE_ITEM = 1
    }
}

sealed class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class Item(view: View) : SearchResultViewHolder(view) {
        fun bind(user: User) {
            with(itemView) {
                usernameText.text = user.name
                user.avatarUrl?.let {
                    val options = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .skipMemoryCache(false)
                        .override(100, 100)
                        .placeholder(R.drawable.avatar)
                    Glide.with(this).load(it).apply(options).into(imgAvatar)
                }
            }
        }
    }

    class Footer(view: View) : SearchResultViewHolder(view)
}