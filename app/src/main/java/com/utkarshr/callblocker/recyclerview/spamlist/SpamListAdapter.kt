package com.utkarshr.callblocker.recyclerview.spamlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.utkarshr.callblocker.R
import com.utkarshr.callblocker.database.RegexType
import com.utkarshr.callblocker.database.SpamNumber
import com.utkarshr.callblocker.databinding.ItemSpamNumberBinding
import kotlinx.android.synthetic.main.item_spam_number.view.*

class SpamListAdapter : RecyclerView.Adapter<SpamListAdapter.SpamListViewHolder>() {

    var spamNumbers: List<SpamNumber> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpamListViewHolder {
        val withDataBinding: ItemSpamNumberBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            SpamListViewHolder.LAYOUT,
            parent,
            false)
        return SpamListViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        return spamNumbers.count()
    }

    override fun onBindViewHolder(holder: SpamListViewHolder, position: Int) {
        val spamNumber = spamNumbers[position]
        val displayText = when (spamNumber.regexType) {
            RegexType.START -> spamNumber.rawNumber + "*"
            RegexType.END -> "*" + spamNumber.rawNumber
            RegexType.MIDDLE -> "*" + spamNumber.rawNumber + "*"
        }
        holder.viewDataBinding.executePendingBindings()
        holder.itemView.textView_number.text = displayText
        holder.itemView.setOnClickListener {
//            TODO("on click listener")
        }
    }

    class SpamListViewHolder(val viewDataBinding: ItemSpamNumberBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_spam_number
        }
    }
}