package com.example.mobile_auth.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_auth.R
import com.example.mobile_auth.Repository
import kotlinx.android.synthetic.main.item_load_new.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class HeaderViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            loadNewBtn.setOnClickListener {
                loadNewBtn.isEnabled = false
                progressbar.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    val response = Repository.getPostsAfter(adapter.list[0].id)
                    if (response.body()?.isEmpty()!!){
                        context.toast(R.string.no_new_content)
                    }
                    progressbar.visibility = View.INVISIBLE
                    loadNewBtn.isEnabled = true
                    if (response.isSuccessful) {
                        val newItems = response.body()!!
                        adapter.list.addAll(0, newItems)
                        adapter.notifyItemRangeInserted(0, newItems.size)
                    } else {
                        context.toast(R.string.error_occured)
                    }
                }
            }
        }
    }
}